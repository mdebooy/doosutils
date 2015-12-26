/**
 * Copyright 2015 Marco de Booij
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
package eu.debooy.doosutils.service;

import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.components.I18nTeksten;
import eu.debooy.doosutils.components.Properties;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * @author Marco de Booij
 */
public class DoosService {
  private String[]    exportTypes = new String[] {"ONBEKEND"};
  private I18nTeksten i18nTekst   = null;
  private Properties  property    = null;

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
   * Zet de kleuren voor de JasperReport.
   * 
   * @param String De naam van de applicatie
   * @return Map<String, String>
   */
  protected Map<String, String> getLijstKleuren(String applicatie) {
    Map<String, String> kleuren = new HashMap<String, String>();
    String[]            params  = new String[] {"columnheader.background",
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
      String  kleur = getParameter(applicatie + ".lijst." + param);
      if (DoosUtils.isNotBlankOrNull(kleur)) {
        kleuren.put(param, kleur);
      }
    }

    return kleuren;
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
//      addWarning("errors.notfound.parameter", parameter);
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
  public String getTekst(String code, Object... params) {
    String  tekst = getI18nTekst().tekst(code);

    if (null == params) {
      return tekst;
    }

    MessageFormat formatter = new MessageFormat(tekst, new Locale("nl"));
//                                                getGebruiker().getLocale());

    return formatter.format(params);
  }

  /**
   * Is het exportType toegestaan?
   * 
   * @param exportType
   * @return boolean
   */
  public boolean isGeldigExportType(String exportType) {
    boolean geldig  = false;

    for (int i = 0; i < exportTypes.length; i++) {
      if (exportType.equalsIgnoreCase(exportTypes[i])) {
        geldig  = true;
      }
    }

    return geldig;
  }

  /**
   * @param exportTypes
   */
  // TODO Test de exportTypes.
  public void setExportTypes(String... exportTypes) {
    this.exportTypes  = new String[exportTypes.length];
    for (int i = 0; i < exportTypes.length; i++) {
      this.exportTypes[i] = exportTypes[i];
    }
  }
}
