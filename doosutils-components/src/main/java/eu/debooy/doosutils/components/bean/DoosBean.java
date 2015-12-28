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
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.I18nTeksten;
import eu.debooy.doosutils.components.Message;
import eu.debooy.doosutils.components.Properties;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.service.CDI;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
 * 
 * Methodes mogen niet final zijn omdat CDI dit niet toelaat.
 */
public class DoosBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private static  Logger    LOGGER    =
      LoggerFactory.getLogger(DoosBean.class.getName());
  public  static  String    BEAN_NAME = "doosBean";

  private boolean             adminRole       = false;
  private char                aktie           = PersistenceConstants.RETRIEVE;
  private String              applicatieNaam  = "DoosBean";
  private Gebruiker           gebruiker       = null;
  private I18nTeksten         i18nTekst       = null;
  private Map<String, String> kleuren         = null;
  private Properties          property        = null;
  private String              type            = null;
  private String              subTitel        = null;
  private boolean             userRole        = false;

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
  protected void addError(String code, Object... params) {
    addMessage(FacesMessage.SEVERITY_ERROR, code, params);
  }

  /**
   * Voeg een JSF FATAL melding toe.
   * 
   * @param summary
   */
  protected void addFatal(String code, Object... params) {
    addMessage(FacesMessage.SEVERITY_FATAL, code, params);
  }

  /**
   * Voeg een JSF INFO melding toe.
   * 
   * @param summary
   */
  protected void addInfo(String code, Object... params) {
    addMessage(FacesMessage.SEVERITY_INFO, code, params);
  }

  /**
   * Voeg een JSF melding toe.
   * 
   * @param severity
   * @param code
   * @param params
   */
  protected void addMessage(Severity severity, String code,
                                  Object... params) {
    String        detail  = getTekst(code, params);
    String        summary = getTekst(code, params);
    FacesMessage  msg     = new FacesMessage(severity, summary, detail);

    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  /**
   * Voeg een JSF melding toe.
   * 
   * @param severity
   * @param code
   * @param params
   */
  protected void addMessage(List<Message> messages) {
    for (Message message : messages) {
      Object[]  params  = message.getParams();
      // Parameters die beginnen met "_I18N." moeten 'vertaald' worden.
      for (int i = 0; i < params.length; i++) {
        if (params[i] instanceof String) {
          String  param = (String) params[i];
          if (param.startsWith("_I18N.")) {
            params[i] = getTekst(param.substring(6));
          }
        }
      }
      String    code    = message.getMessage();
      switch (message.getSeverity()) {
      case Message.ERROR:
        addError(code, params);
        break;
      case Message.FATAL:
        addFatal(code, params);
        break;
      case Message.INFO:
        addInfo(code, params);
        break;
      default:
        addWarning(code, params);
        break;
      }
    }
  }

  /**
   * Voeg een JSF WARN melding toe.
   * 
   * @param summary
   */
  protected void addWarning(String code, Object... params) {
    addMessage(FacesMessage.SEVERITY_WARN, code, params);
  }

  /**
   * Genereer een exceptie message.
   * 
   * @param exception
   */
  protected void generateExceptionMessage(Exception exception) {
    addError("generic.Exception", new Object[] {exception.getMessage(),
                                                exception });
  }

  /**
   * @return de aktie
   */
  public char getAktie() {
    return aktie;
  }

  /**
   * Geef de naam van de applicatie.
   * @return
   */
  public String getApplicatieNaam() {
    return applicatieNaam;
  }
  /**
   * Zet de kleuren voor de JasperReport.
   * 
   * @return Map<String, String>
   */
  protected Map<String, String> getLijstKleuren() {
    if (null == kleuren) {
      kleuren           = new HashMap<String, String>();
      String[]  params  = new String[] {"columnheader.background",
                                        "columnheader.foreground",
                                        "footer.background",
                                        "footer.foreground",
                                        "row.background",
                                        "row.foreground",
                                        "row.conditional.background",
                                        "row.conditional.foreground",
                                        "titel.background",
                                        "titel.foreground"};

      for (String param : params) {
        String  kleur = getParameter(getApplicatieNaam().toLowerCase()
                                     + ".lijst." + param);
        if (DoosUtils.isNotBlankOrNull(kleur)) {
          kleuren.put(param, kleur);
        }
      }
    }

    return kleuren;
  }

  /**
   * Geef een geformatteerde message.
   * 
   * @param message
   * @param params
   * @return
   */
  public String getMessage(String message, Object... params) {
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
   * Geef de sub-titel.
   * 
   * @return
   */
  public String getSubTitel() {
    return subTitel;
  }

  /**
   * Krijg de tekst die bij de code behoort. De eventuele params worden erin
   * gezet.
   * 
   * @param code
   * @param params
   * @return
   */
  public String getTekst(String code, Object... params) {
    String  tekst = getI18nTekst().tekst(code);

    if (null == params) {
      return tekst;
    }

    MessageFormat formatter = new MessageFormat(tekst,
                                                getGebruiker().getLocale());

    return formatter.format(params);
  }

  /**
   * @return de type
   */
  public String getType() {
    return type;
  }

  protected DoosBean getApplicationBean(String name) {
    return (DoosBean) getExternalContext().getApplicationMap().get(name);
  }

  /**
   * Geef een Managed Bean.
   * 
   * @param clazz een DoosBean.
   * @return DoosBean
   */
  protected DoosBean getBean(Class<?> clazz) {
    return (DoosBean) CDI.getBean(clazz);
  }

  /**
   * Geef een Managed Bean.
   * 
   * @param naam een naam van een DoosBean.
   * @return DoosBean
   */
  protected DoosBean getBean(String naam) {
    return (DoosBean) CDI.getBean(naam);
  }

  /**
   * Geef de huidige ExternalContext.
   * 
   * @return ExternalContext
   */
  protected ExternalContext getExternalContext() {
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
  public String getGebruikerNaam() {
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

  /**
   * In 'Bekijk' mode?
   * 
   * @return boolean
   */
  public boolean isBekijk() {
    return (aktie == PersistenceConstants.RETRIEVE);
  }

  /**
   * In 'Nieuw' mode?
   * 
   * @return boolean
   */
  public boolean isNieuw() {
    return (aktie == PersistenceConstants.CREATE);
  }

  /**
   * In read-only mode?
   * 
   * @return boolean
   */
  public boolean isReadonly() {
    return (aktie == PersistenceConstants.DELETE)
        || (aktie == PersistenceConstants.RETRIEVE);
  }

  /**
   * In 'Verwijder' mode?
   * 
   * @return boolean
   */
  public boolean isVerwijder() {
    return (aktie == PersistenceConstants.DELETE);
  }

  /**
   * In 'Wijzig' mode?
   * 
   * @return boolean
   */
  public boolean isWijzig() {
    return (aktie == PersistenceConstants.UPDATE);
  }

  public void invokeAction(String action) {
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
   * Is de gebruiker een administrator?
   * 
   * @return boolean
   */
  public boolean isAdministrator() {
    return adminRole;
  }

  /**
   * Mag de user de applicatie gebruiken?
   * 
   * @return boolean
   */
  public boolean isGerechtigd() {
    return adminRole || userRole;
  }

  /**
   * Is het een gewone gebruiker?
   * 
   * @return boolean
   */
  public boolean isUser() {
    return userRole;
  }

  /**
   * Redirect naar de applicatie 'Home' pagina.
   */
  protected void redirect() {
    redirect("/index.jsf");
  }

  /**
   * Redirect naar een pagina.
   * @param path
   */
  protected void redirect(String path) {
    try {
      getExternalContext().redirect(getExternalContext().getRequestContextPath()
                                    + path);
    } catch (IOException e) {
      generateExceptionMessage(e);
    }
  }

  /**
   * @param adminRole de waarde van adminRole
   */
  public void setAdminRole(boolean adminRole) {
    this.adminRole = adminRole;
  }

  /**
   * @param aktie
   */
  public void setAktie(char aktie) {
    this.aktie  = aktie;
  }

  /**
   * @param String de naam van de applicatie.
   */
  public void setApplicatieNaam(String applicatieNaam) {
    this.applicatieNaam = applicatieNaam;
  }

  public void setSubTitel(String subTitel) {
    this.subTitel = subTitel;
  }

  /**
   * @param type de waarde van type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * @param userRole de waarde van userRole
   */
  public void setUserRole(boolean userRole) {
    this.userRole = userRole;
  }
}
