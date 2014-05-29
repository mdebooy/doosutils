/**
 * Copyright 2012 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doosutils.components.bean;

import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.components.I18nTeksten;
import eu.debooy.doosutils.components.Properties;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.service.CDI;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
public class DoosBean implements Serializable {
  private static final  long      serialVersionUID      = 1L;

  private static final  Logger    LOGGER                =
      LoggerFactory.getLogger(DoosBean.class.getName());
  public  static final  String    BEAN_NAME             = "doosBean";
  private static final  String    SESSION_DIRTYPAGE_KEY = "page.dirty";

  private Gebruiker   gebruiker = null;
  private I18nTeksten i18nTekst = null;
  private Properties  property  = null;

  public DoosBean() {
    if (LOGGER.isTraceEnabled()) {
      LOGGER.trace("DoosBean gemaakt.");
    }
  }

  /**
   * Zoek een UI Component.
   * 
   * @param baseComp
   * @param id
   * @return
   */
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

  /**
   * Voeg een JSF melding toe.
   * 
   * @param severity
   * @param code
   * @param params
   */
  protected final void addMessage(Severity severity, String code,
                                  Object... params) {
    String        detail  = getTekst(code, params);
    String        summary = getTekst(code, params);
    FacesMessage  msg     = new FacesMessage(severity, summary, detail);

    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  /**
   * Voeg een JSF WARN melding toe.
   * 
   * @param summary
   */
  protected final void addWarning(String code, Object... params) {
    addMessage(FacesMessage.SEVERITY_WARN, code, params);
  }

  /**
   * Zorg ervoor dat de DoosBean weer in na-init status is.
   * 
   * @param naam
   */
  protected final void destroyBean(String naam) {
    DoosBean  bean  = getBean(naam);
    bean.reset();
  }

  /**
   * Genereer een exceptie message.
   * 
   * @param exception
   */
  protected final void generateExceptionMessage(Exception exception) {
    addError("generic.Exception", new Object[] {exception.getMessage(),
                                                exception });
  }

  /**
   * Geef een geformatteerde message.
   * 
   * @param message
   * @param params
   * @return
   */
  public final String getMessage(String message, Object... params) {
    if (null == params) {
      return message;
    }

    MessageFormat formatter = new MessageFormat(message,
                                                getGebruiker().getLocale());

    return formatter.format(params);
  }

  /**
   * Lees de parameter.
   * 
   * @param parameter
   * @return String
   */
  public String getParameter(String parameter) {
    String  waarde;
    try {
      waarde  = getProperty().value(parameter);
    } catch (ObjectNotFoundException e) {
      addWarning("errors.notfound.parameter", parameter);
      return "";
    }

    return waarde;
  }

  private Properties getProperty() {
    if (null == property) {
      property  = (Properties) CDI.getBean(Properties.class);
    }

    return property;
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

    MessageFormat formatter = new MessageFormat(tekst,
                                                getGebruiker().getLocale());

    return formatter.format(params);
  }

  protected final DoosBean getApplicationBean(String name) {
    return (DoosBean) getExternalContext().getApplicationMap().get(name);
  }

  /**
   * Geef een Managed Bean.
   * 
   * @param clazz een DoosBean.
   * @return DoosBean
   */
  protected final DoosBean getBean(Class<?> clazz) {
    return (DoosBean) CDI.getBean(clazz);
  }

  /**
   * Geef een Managed Bean.
   * 
   * @param naam een naam van een DoosBean.
   * @return DoosBean
   */
  protected final DoosBean getBean(String naam) {
    return (DoosBean) CDI.getBean(naam);
  }

  /**
   * Geef de huidige ExternalContext.
   * 
   * @return ExternalContext
   */
  protected final ExternalContext getExternalContext() {
    FacesContext  facesContext  = FacesContext.getCurrentInstance();

    return facesContext.getExternalContext();
  }

  /**
   * Geef de Gebruiker.
   * 
   * @return Gebruiker
   */
  private Gebruiker getGebruiker() {
    if (null == gebruiker) {
      gebruiker = (Gebruiker) CDI.getBean(Gebruiker.class);
    }

    return gebruiker;
  }

  /**
   * Geef de naam (of id) van de gebruiker.
   * 
   * @return String
   */
  public final String getGebruikerNaam() {
    String  resultaat = getGebruiker().getUserName();
    if (DoosUtils.isNotBlankOrNull(resultaat)) {
      return resultaat;
    }

    return getGebruiker().getUserId();
  }

  /**
   * Geef de I18nTeksten.
   * 
   * @return I18nTeksten
   */
  private I18nTeksten getI18nTekst() {
    if (null == i18nTekst) {
      i18nTekst = (I18nTeksten) CDI.getBean(I18nTeksten.class);
    }

    return i18nTekst;
  }

  public final void invokeAction(String action) {
    if (null != action) {
      StringTokenizer tk          = new StringTokenizer(action, ".", false);
      String          beannaam    = tk.nextToken();
      DoosBean        bean        = getBean(beannaam);
      String          methodName  = tk.nextToken();
      try {
        Method method = bean.getClass().getMethod(methodName, new Class[0]);
        method.invoke(bean, new Object[0]);
      } catch (SecurityException e) {
        LOGGER.error("SecurityException: " + e.getMessage());
      } catch (NoSuchMethodException e) {
        LOGGER.error("NoSuchMethodException: " + e.getMessage());
      } catch (IllegalArgumentException e) {
        LOGGER.error("IllegalArgumentException: " + e.getMessage());
      } catch (IllegalAccessException e) {
        LOGGER.error("IllegalAccessException: " + e.getMessage());
      } catch (InvocationTargetException e) {
        LOGGER.error("InvocationTargetException: " + e.getMessage());
      }
    }
  }

  /**
   * Zijn er wijzigingen op de pagina?
   * 
   * @return boolean
   */
  public final boolean isPageDirty() {
    Object  value = getExternalContext().getSessionMap()
                                        .get(SESSION_DIRTYPAGE_KEY);
    if (null == value) {
      return false;
    }

    return ((Boolean) value).booleanValue();
  }

  protected final void processActionWithCaution(String proceedAction) {
    if (isPageDirty()) {
      ConfirmationBean  confirm =
        (ConfirmationBean) getBean(ConfirmationBean.class);
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

  /**
   * Reset de bean.
   */
  public void reset() {
    setPageDirty(false);
  }

  /**
   * Geeft aan of er wijzigingen op de pagina zijn.
   * 
   * @param Boolean Dirty?
   */
  protected final void setPageDirty(Boolean dirty) {
    getExternalContext().getSessionMap().put(SESSION_DIRTYPAGE_KEY, dirty);
  }
}
