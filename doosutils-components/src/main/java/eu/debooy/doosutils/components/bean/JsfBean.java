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
package eu.debooy.doosutils.components.bean;

import eu.debooy.doosutils.components.I18nTekst;
import eu.debooy.doosutils.components.Messages;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
public class JsfBean implements Serializable {
  private static final long      serialVersionUID       = 1L;

  private final transient Logger logger                 =
      LoggerFactory.getLogger(JsfBean.class.getName());
  public  static final  String    BEAN_NAME             = "jsf";
  private static final  String    SESSION_DIRTYPAGE_KEY = "page.dirty";
  private static final  String    VERSION_UNSTABLE      = "UNSTABLE";
  private static final  String    DATE_UNKNOWN          = "UNKNOWN";

  private static  String          buildVersion;
  private static  String          buildDate;

  private I18nTekst               i18nTekst = null;
  private Locale                  locale    = null;
  private String                  userId    = null;
  private String                  userName  = null;

  static {
    buildDetails();
  }

  public JsfBean() {
    if (logger.isTraceEnabled()) {
      logger.trace("JsfBean gemaakt.");
    }
  }

  private static void buildDetails() {
    try {
      URL       manifestUrl     = null;
      Manifest  manifest        = null;
      String    classContainer  = JsfBean.class.getProtectionDomain()
                                         .getCodeSource()
                                         .getLocation().toString();

      if (classContainer.indexOf("/WEB-INF/lib") >= 0) {
        classContainer  =
          classContainer.substring(0, classContainer.indexOf("/WEB-INF/lib"));
        manifestUrl     =
          new URL((new StringBuilder()).append(classContainer)
                                       .append("/META-INF/MANIFEST.MF")
                                       .toString());

      } else {
        manifestUrl     =
          new URL((new StringBuilder()).append("jar:")
                                       .append(classContainer)
                                       .append("!/META-INF/MANIFEST.MF")
                                       .toString());

      }
      try {
        manifest  = new Manifest(manifestUrl.openStream());
      } catch (FileNotFoundException e) {
        classContainer  =
          classContainer.substring(0, classContainer.indexOf("/WEB-INF/lib"));
        manifestUrl     =
          new URL((new StringBuilder()).append(classContainer)
                                       .append("/META-INF/MANIFEST.MF")
                                       .toString());
        manifest        = new Manifest(manifestUrl.openStream());
      }
      Attributes  attr  = manifest.getMainAttributes();
      buildVersion  = attr.getValue("Implementation-Version");
      buildDate     = attr.getValue("Build-Time");
    } catch (IOException e) {
      buildVersion  = VERSION_UNSTABLE;
      buildDate     = DATE_UNKNOWN;
    }
  }

  public final String getBuildVersion() {
    return buildVersion;
  }

  public final String getBuildDate() {
    return buildDate;
  }

  public final Locale getLocale() {
    if (null == locale) {
      locale  = getExternalContext().getRequestLocale();
    }

    return locale;
  }

  public final String getMessage(String message, Object... params) {
    if (null == params) {
      return message;
    }

    MessageFormat formatter = new MessageFormat(message, getLocale());

    return formatter.format(params);
  }

  public final String getUserId() {
    if (null == userId) {
      userId  = getExternalContext().getRemoteUser();
    }

    return userId;
  }

  public final String getUserName() {
    if (null == userName) {
      Principal principal = getExternalContext().getUserPrincipal();
      userName  = principal.getName();
    }

    return userName;
  }

  protected final ExternalContext getExternalContext() {
    FacesContext  facesContext  = FacesContext.getCurrentInstance();

    return facesContext.getExternalContext();
  }

  private I18nTekst getI18nTekst() {
    if (null == i18nTekst) {
      i18nTekst = (I18nTekst) getExternalContext().getSessionMap()
                                                  .get("i18nTeksten");
    }

    return i18nTekst;
  }

  protected final UIViewRoot getViewRoot() {
    return FacesContext.getCurrentInstance().getViewRoot();
  }

  protected final JsfBean getSessionBean(String name) {
    return (JsfBean) getExternalContext().getSessionMap().get(name);
  }

  protected final JsfBean getApplicationBean(String name) {
    return (JsfBean) getExternalContext().getApplicationMap().get(name);
  }

  protected final JsfBean getRequestBean(String name) {
    return (JsfBean) getExternalContext().getRequestMap().get(name);
  }

  protected final JsfBean getBean(String name) {
    JsfBean bean  = getSessionBean(name);
    if (null != bean) {
      return bean;
    }
    bean  = getRequestBean(name);
    if (null != bean) {
      return bean;
    } else {
      return getApplicationBean(name);
    }
  }

  /**
   * Voeg een JSF ERROR melding toe.
   * 
   * @param summary
   */
  protected final void addError(String code, Object... params) {
    addMessage(FacesMessage.SEVERITY_ERROR, code, params);
  }

  /**
   * Voeg een JSF FATAL melding toe.
   * 
   * @param summary
   */
  protected final void addFatal(String code, Object... params) {
    addMessage(FacesMessage.SEVERITY_FATAL, code, params);
  }

  /**
   * Voeg een JSF INFO melding toe.
   * 
   * @param summary
   */
  protected final void addInfo(String code, Object... params) {
    addMessage(FacesMessage.SEVERITY_INFO, code, params);
  }

  protected final void addMessage(Severity severity, String code,
                                  Object... params) {
    String        detail  = getTekst(code, params);
    String        summary = getTekst(code, params);
    FacesMessage  msg     = new FacesMessage(severity, summary, detail);

    Messages.getMessage(msg);
  }

  /**
   * Voeg een JSF WARN melding toe.
   * 
   * @param summary
   */
  protected final void addWarning(String code, Object... params) {
    addMessage(FacesMessage.SEVERITY_WARN, code, params);
  }

  protected final void destroyBean(String name) {
    JsfBean bean  = getSessionBean(name);
    if (null != bean) {
      getExternalContext().getSessionMap().remove(name);
    } else {
      bean  = getRequestBean(name);
      if (null != bean) {
        getExternalContext().getRequestMap().remove(name);
        bean  = getRequestBean(name);
      }
    }
  }

  protected final void generateExceptionMessage(Exception exception) {
    addError("generic.Exception", new Object[] {exception.getMessage(),
                                                exception });
  }

  public static UIComponent findComponent(UIComponent baseComp, String id) {
    if (baseComp.getId().endsWith(id)) {
      return baseComp;
    }
    if (baseComp.getChildCount() <= 0) {
      return null;
    }
    Iterator<?> iter  = baseComp.getChildren().iterator();
    UIComponent component;
    do {
      if (!iter.hasNext()) {
        return null;
      }
      UIComponent comp  = (UIComponent) iter.next();
      component = findComponent(comp, id);
    } while (component == null);

    return component;
  }

  protected final void redirect() {
    redirect("/index.jsf");
  }

  protected final void redirect(String path) {
    try {
      getExternalContext().redirect(getExternalContext().getRequestContextPath()
                                    + path);
    } catch (IOException e) {
      generateExceptionMessage(e);
    }
  }

  protected final void setLocale(Locale locale) {
    this.locale = locale;
  }

  protected final void setPageDirty(Boolean dirty) {
    getExternalContext().getSessionMap().put(SESSION_DIRTYPAGE_KEY, dirty);
  }

  public final boolean isPageDirty() {
    Object  value = getExternalContext().getSessionMap()
                                        .get(SESSION_DIRTYPAGE_KEY);
    if (null == value) {
      return false;
    }

    return ((Boolean) value).booleanValue();
  }

  /**
   * Krijg de tekst die bij de code behoort. De eventuele params worden erin
   * gezet.
   * 
   * @param code
   * @param params
   * @return
   */
  public final String getTekst(String code, Object... params) {
    String  tekst = getI18nTekst().tekst(code);

    if (null == params) {
      return tekst;
    }

    MessageFormat formatter = new MessageFormat(tekst, getLocale());

    return formatter.format(params);
  }

  public final TimeZone getTimeZone() {
    return TimeZone.getDefault();
  }

  protected final void processActionWithCaution(String proceedAction) {
    if (isPageDirty()) {
      ConfirmationBean  confirm =
        (ConfirmationBean) getSessionBean("confirmation");
      confirm.setConfirmReturnAction(proceedAction);
      confirm.setHeader("Warning");
      confirm.setBody("Er zijn niet opgeslagen veranderingen...<br/>Doorgaan "
                      + "betekent dat je de wijzigingen zal verliezen. "
                      + "Weet je zeker dat je dit wilt?");

      confirm.showPopup();
    } else {
      invokeAction(proceedAction);
    }
  }

  public final void invokeAction(String action) {
    if (null != action) {
      StringTokenizer tk          = new StringTokenizer(action, ".", false);
      String          beanName    = tk.nextToken();
      JsfBean         bean        = getBean(beanName);
      String          methodName  = tk.nextToken();
      try {
        Method method = bean.getClass().getMethod(methodName, new Class[0]);
        method.invoke(bean, new Object[0]);
      } catch (Exception exc) {
      }
    }
  }
}
