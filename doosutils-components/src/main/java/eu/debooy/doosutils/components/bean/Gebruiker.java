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

import eu.debooy.jaas.UserPrincipal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Locale;
import java.util.TimeZone;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Deze class wordt gebruikt om de versie informatie van een applicatie te laten
 * zien. Tevens bevat het de locale, de user-id en de volledige naam van de
 * gebruiker zodat niet steeds deze informatie uit de request informatie gehaald
 * moet worden.
 * 
 * @author Marco de Booij
 */
@Named
@SessionScoped
public class Gebruiker implements Serializable {
  private static final  long      serialVersionUID      = 1L;

  private static final  Logger    LOGGER                =
      LoggerFactory.getLogger(Gebruiker.class);
  private static final  String    DATE_UNKNOWN      = "UNKNOWN";
  private static final  String    MANIFEST          = "/META-INF/MANIFEST.MF";
  private static final  String    VERSION_UNSTABLE  = "UNSTABLE";
  private static final  String    WEB_INF           = "/WEB-INF/lib";

  private static  String          versie;
  private static  String          bouwdatum;

  private ExternalContext externalContext = null;
  private FacesContext    facesContext    = null;
  private Locale          locale          = null;
  private String          userId          = null;
  private String          userName        = null;

  static {
    buildDetails();
  }

  public Gebruiker() {
    LOGGER.trace("Gebruiker gemaakt.");
  }

  /**
   * Haal de Build details (datum en versie) op uit de MANIFEST.MF.
   */
  private static void buildDetails() {
    try {
      URL       manifestUrl     = null;
      Manifest  manifest        = null;
      String    classContainer  = Gebruiker.class.getProtectionDomain()
                                           .getCodeSource()
                                           .getLocation().toString();

      if (classContainer.indexOf(WEB_INF) >= 0) {
        classContainer  =
          classContainer.substring(0, classContainer.indexOf(WEB_INF));
        manifestUrl     =
          new URL((new StringBuilder()).append(classContainer)
                                       .append(MANIFEST)
                                       .toString());

      } else {
        manifestUrl     =
          new URL((new StringBuilder()).append("jar:")
                                       .append(classContainer)
                                       .append("!").append(MANIFEST)
                                       .toString());

      }
      try {
        manifest        = new Manifest(manifestUrl.openStream());
      } catch (FileNotFoundException e) {
        classContainer  =
          classContainer.substring(0, classContainer.indexOf(WEB_INF));
        manifestUrl     =
          new URL((new StringBuilder()).append(classContainer)
                                       .append(MANIFEST)
                                       .toString());
        manifest        = new Manifest(manifestUrl.openStream());
      }
      Attributes  attr  = manifest.getMainAttributes();
      versie    = attr.getValue("Implementation-Version");
      bouwdatum = attr.getValue("Build-Time");
    } catch (IOException e) {
      versie    = VERSION_UNSTABLE;
      bouwdatum = DATE_UNKNOWN;
    }
  }

  /**
   * Geef de Build Datum van de applicatie.
   * 
   * @return String
   */
  public String getBouwdatum() {
    return bouwdatum;
  }

  /**
   * Geeft de Build Versie van de applicatie.
   * 
   * @return String
   */
  public String getVersie() {
    return versie;
  }

  /**
   * Haal de ExternalContext op.
   * 
   * @return ExternalContext
   */
  protected ExternalContext getExternalContext() {
    if (null == externalContext) {
      externalContext = getFacesContext().getExternalContext();
    }

    return externalContext;
  }

  /**
   * Haal de FacesContext op.
   * 
   * @return FacesContext
   */
  protected FacesContext getFacesContext() {
    if (null == facesContext) {
      facesContext  = FacesContext.getCurrentInstance();
    }

    return facesContext;
  }

  /**
   * Geef de Locale zodat de taal van de gebruiker bekend is.
   * 
   * @return Locale
   */
  public Locale getLocale() {
    if (null == locale) {
      locale  = getExternalContext().getRequestLocale();
    }

    return locale;
  }

  /**
   * Geef de Tijdszone.
   * 
   * @return TimeZone
   */
  // TODO Zorg voor de TimeZone van de gebruiker.
  public TimeZone getTimeZone() {
    return TimeZone.getDefault();
  }

  /**
   * Geef de user-id.
   * 
   * @return String userId
   */
  public String getUserId() {
    if (null == userId) {
      userId  = getExternalContext().getRemoteUser();
      if (null == userId) {
        userId  = "";
      }
    }

    return userId;
  }

  /**
   * Geef de volledige naam van de gebruiker. Indien de gebruiker niet is
   * aangemeld wordt een lege String gegeven. Di om te voorkomen dat de
   * methode die oproept een NullPointerException krijgt.
   * 
   * @return String userName
   */
  public String getUserName() {
    if (null == userName) {
      UserPrincipal principal =
          (UserPrincipal) getExternalContext().getUserPrincipal();
      if (null != principal) {
        userName  = principal.getVolledigeNaam();
      }
    }
    if (null == userName) {
      userName  = getUserId();
    }

    return userName;
  }

  /**
   * Zet de Locale als die anders moet zijn dan die van de browser.
   * 
   * @param Locale de waarde van locale
   */
  public void setLocale(Locale locale) {
    this.locale   = locale;
  }

  /**
   * Zet de user-id als die niet uit de UserPrincipal gehaald kan worden.
   * 
   * @param String de waarde van userId
   */
  public void setUserId(String userId) {
    this.userId   = userId;
  }

  /**
   * Zet de user-name als die niet uit de UserPrincipal gehaald kan worden.
   * 
   * @param String de waarde van userName
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }
}
