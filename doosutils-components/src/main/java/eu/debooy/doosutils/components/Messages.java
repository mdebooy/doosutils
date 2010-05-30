/**
 * Copyright 2009 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by
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
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;


/**
 * @author Marco de Booij
 */
public final class Messages {
  public static final String DEFAULT_BUNDLE = "eu.debooy.jsf.messages";

  private Messages() {}

  public static ClassLoader getClassLoader() {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    if (loader == null) {
      loader = ClassLoader.getSystemClassLoader();
    }

    return loader;
  }

  public static FacesMessage getMessage(UIComponent component,
                                        FacesMessage.Severity severity,
                                        String bundleName, String resourceId,
                                        Object[] params) {
    FacesContext  context   = FacesContext.getCurrentInstance();
    Application   app       = context.getApplication();
    String        appBundle = app.getMessageBundle();
    Locale        locale    = getLocale(context);
    ClassLoader   loader    = getClassLoader();
    String        summary   = "";
    if (resourceId != null) {
      summary = getString(appBundle, bundleName, resourceId, locale, loader,
          params);
      if (summary == null) {
        summary = "???" + resourceId + "???";
      }
    }
    String        detail  = getString(appBundle, bundleName,
                                      resourceId + "_detail", locale, loader,
                                      params);
    FacesMessage  msg     = new FacesMessage(severity, summary, detail);
    if (component != null) {
      FacesContext.getCurrentInstance().addMessage(
          component.getClientId(FacesContext.getCurrentInstance()), msg);
    } else {
      FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    return msg;
  }

  public static FacesMessage getMessageForId(String componentId,
                                             FacesMessage.Severity severity,
                                             String bundleName,
                                             String resourceId,
                                             Object[] params) {
    UIComponent component = null;
    if (componentId != null) {
      FacesContext  context   = FacesContext.getCurrentInstance();
      UIViewRoot    viewRoot  = context.getViewRoot();
      component = findComponent(viewRoot, componentId);
    }

    return getMessage(component, severity, bundleName, resourceId, params);
  }

  public static String getString(String bundle, String resourceId,
                                 Object[] params) {
    FacesContext  context   = FacesContext.getCurrentInstance();
    Application   app       = context.getApplication();
    String        appBundle = app.getMessageBundle();
    Locale        locale    = getLocale(context);
    ClassLoader   loader    = getClassLoader();

    return getString(appBundle, bundle, resourceId, locale, loader, params);
  }

  public static String getString(String bundle1, String bundle2,
                                 String resourceId, Locale locale,
                                 ClassLoader loader, Object[] params) {
    String  resource  = null;

    if (bundle1 != null) {
      ResourceBundle  bundle;
      try {
        bundle = ResourceBundle.getBundle(bundle1, locale, loader);
        if (bundle != null)
          resource  = bundle.getString(resourceId);
      } catch (Exception exc) {
      }
      if (resource == null) {
        try {
          bundle = ResourceBundle.getBundle(bundle2, locale, loader);
          if (bundle != null)
            resource = bundle.getString(resourceId);
        } catch (Exception exc) {
        }
      }
    }
    if (resource == null) {
      return null;
    }
    if (params == null) {
      return resource;
    }

    MessageFormat formatter = new MessageFormat(resource, locale);

    return formatter.format(params);
  }

  public static Locale getLocale(FacesContext context) {
    Locale  locale  = null;
    UIViewRoot viewRoot = context.getViewRoot();
    if (viewRoot != null) {
      locale  = viewRoot.getLocale();
    }
    if (locale == null) {
      locale  = Locale.getDefault();
    }
    return locale;
  }

  public static UIComponent findComponent(UIComponent component,
                                          String componentId) {
    if (component.getId().endsWith(componentId)) {
      return component;
    }

    for (UIComponent comp : component.getChildren()) {
      UIComponent tmpComp = findComponent(comp, componentId);
      if (tmpComp != null) {
        return tmpComp;
      }
    }
    return null;
  }
}
