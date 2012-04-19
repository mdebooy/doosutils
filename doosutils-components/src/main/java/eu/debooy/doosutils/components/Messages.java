/**
 * Copyright 2009 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doosutils.components;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;


/**
 * @author Marco de Booij
 */
public final class Messages {
  public static final String DEFAULT_BUNDLE = "eu.debooy.jsf.messages";

  private Messages() {}

  /**
   * Zoek een Component ook in geneste components.
   * 
   * @param component
   * @param componentId
   * @return
   */
  public static UIComponent findComponent(UIComponent component,
                                          String componentId) {
    if (component.getId().endsWith(componentId)) {
      return component;
    }
    UIComponent gevondenComp;
    for (UIComponent comp : component.getChildren()) {
      gevondenComp  = findComponent(comp, componentId);
      if (null != gevondenComp) {
        return gevondenComp;
      }
    }

    return null;
  } 

  /**
   * @return
   */
  public static ClassLoader getClassLoader() {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    if (null == loader) {
      loader = ClassLoader.getSystemClassLoader();
    }

    return loader;
  }

  /**
   * @param context
   * @return
   */
  public static Locale getLocale(FacesContext context) {
    Locale      locale    = null;
    UIViewRoot  viewRoot  = context.getViewRoot();
    if (viewRoot != null) {
      locale  = viewRoot.getLocale();
    }
    if (locale == null) {
      locale  = Locale.getDefault();
    }

    return locale;
  }

  public static void getMessage(UIComponent component, FacesMessage msg) {
    FacesContext.getCurrentInstance()
                .addMessage(
                    component.getClientId(FacesContext.getCurrentInstance()),
                    msg);
  }

  public static void getMessage(FacesMessage msg) {
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  /**
   * @param component
   * @param severity
   * @param bundleName
   * @param resourceId
   * @param params
   * @return
   */
  public static FacesMessage getMessage(UIComponent component,
                                        Severity severity, String bundleName,
                                        String resourceId, Object... params) {
    FacesContext  context   = FacesContext.getCurrentInstance();
    Application   app       = context.getApplication();
    String        appBundle = app.getMessageBundle();
    Locale        locale    = getLocale(context);
    ClassLoader   loader    = getClassLoader();
    String        summary   = "";

    if (null != resourceId) {
      summary = getString(appBundle, bundleName, resourceId, locale, loader,
                          params);
      if (null == summary) {
        summary = "???" + resourceId + "???";
      }
    }

    String        detail  = getString(appBundle, bundleName,
                                      resourceId + "_detail", locale, loader,
                                      params);
    FacesMessage  msg     = new FacesMessage(severity, summary, detail);
    if (null != component) {
      getMessage(component, msg);
    } else {
      getMessage(msg);
    }

    return msg;
  }

  /**
   * @param componentId
   * @param severity
   * @param bundleName
   * @param resourceId
   * @param params
   * @return
   */
  public static FacesMessage getMessageForId(String componentId,
                                             Severity severity,
                                             String bundleName,
                                             String resourceId,
                                             Object... params) {
    UIComponent component = null;
    if (null != componentId) {
      FacesContext  context   = FacesContext.getCurrentInstance();
      UIViewRoot    viewRoot  = context.getViewRoot();
      component = findComponent(viewRoot, componentId);
    }

    return getMessage(component, severity, bundleName, resourceId, params);
  }

  /**
   * @param bundle
   * @param resourceId
   * @param params
   * @return
   */
  public static String getString(String bundle, String resourceId,
                                 Object... params) {
    FacesContext  context   = FacesContext.getCurrentInstance();
    Application   app       = context.getApplication();
    String        appBundle = app.getMessageBundle();
    Locale        locale    = getLocale(context);
    ClassLoader   loader    = getClassLoader();

    return getString(appBundle, bundle, resourceId, locale, loader, params);
  }

  /**
   * @param bundle1
   * @param bundle2
   * @param resourceId
   * @param locale
   * @param loader
   * @param params
   * @return
   */
  public static String getString(String bundle1, String bundle2,
                                 String resourceId, Locale locale,
                                 ClassLoader loader, Object... params) {
    String  resource  = null;

    if (null != bundle1) {
      ResourceBundle  bundle;
      try {
        bundle = ResourceBundle.getBundle(bundle1, locale, loader);
        if (null != bundle) {
          resource  = bundle.getString(resourceId);
        }
      } catch (Exception e) {
      }
      if (null == resource) {
        try {
          bundle = ResourceBundle.getBundle(bundle2, locale, loader);
          if (null != bundle) {
            resource = bundle.getString(resourceId);
          }
        } catch (Exception e) {
        }
      }
    }
    if (null == resource) {
      return null;
    }
    if (null == params) {
      return resource;
    }

    MessageFormat formatter = new MessageFormat(resource, locale);

    return formatter.format(params);
  }
}
