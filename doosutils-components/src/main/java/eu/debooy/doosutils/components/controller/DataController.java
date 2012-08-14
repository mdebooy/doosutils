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
package eu.debooy.doosutils.components.controller;

import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.bean.JsfBean;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Marco de Booij
 */
public class DataController extends JsfBean {
  private static final  long  serialVersionUID  = 1L;

  protected boolean     detailGefilterd = false;
  protected boolean     gefilterd       = false;
  protected char        aktie           = PersistenceConstants.RETRIEVE;
  protected char        detailAktie     = PersistenceConstants.RETRIEVE;
  protected String      detailSubTitel  = "";
  protected String      detailType;
  protected String      subTitel        = "";
  protected String      type;

  /**
   * @return de sub-titel van de datails.
   */
  public String getDetailSubTitel() {
    return detailSubTitel;
  }

  /**
   * @return the detailType
   */
  public String getDetailType() {
    return detailType;
  }

  /**
   * Zet de kleuren voor de JasperReport.
   * 
   * @param applicatie
   * @return
   */
  protected Map<String, String> getLijstKleuren(String applicatie) {
    Map<String, String> kleuren   = new HashMap<String, String>();

    String  kleur = getParameter(applicatie + ".lijst.columnheader.background");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("columnheader.background", kleur);
    }
    kleur = getParameter(applicatie + ".lijst.columnheader.foreground");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("columnheader.foreground", kleur);
    }
    kleur = getParameter(applicatie + ".lijst.footer.background");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("footer.background", kleur);
    }
    kleur = getParameter(applicatie + ".lijst.footer.foreground");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("footer.foreground", kleur);
    }
    kleur = getParameter(applicatie + ".lijst.row.background");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("row.background", kleur);
    }
    kleur = getParameter(applicatie + ".lijst.row.foreground");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("row.foreground", kleur);
    }
    kleur = getParameter(applicatie + ".lijst.row.conditional.background");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("row.conditional.background", kleur);
    }
    kleur = getParameter(applicatie + ".lijst.row.conditional.foreground");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("row.conditional.foreground", kleur);
    }
    kleur = getParameter(applicatie + ".lijst.titel.background");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("titel.background", kleur);
    }
    kleur = getParameter(applicatie + ".lijst.titel.foreground");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("titel.foreground", kleur);
    }

    return kleuren;
  }

  /**
   * @return de sub-titel
   */
  public String getSubTitel() {
    return subTitel;
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * In 'Gefilterde' mode?
   * 
   * @return detailGefilterd
   */
  public boolean isDetailGefilterd() {
    return detailGefilterd;
  }

  /**
   * In 'Gefilterde' mode?
   * 
   * @return gefilterd
   */
  public boolean isGefilterd() {
    return gefilterd;
  }

  /**
   * In 'Nieuw' mode?
   * 
   * @return the nieuw
   */
  public boolean isNieuw() {
    return (aktie == PersistenceConstants.CREATE);
  }

  /**
   * Detail in 'Nieuw' mode?
   * 
   * @return the nieuw
   */
  public boolean isNieuwDetail() {
    return (detailAktie == PersistenceConstants.CREATE);
  }

  /**
   * In 'Verwijder' mode?
   * 
   * @return
   */
  public boolean isVerwijder() {
    return (aktie == PersistenceConstants.DELETE);
  }

  /**
   * Detail in 'Verwijder' mode?
   * 
   * @return
   */
  public boolean isVerwijderDetail() {
    return (detailAktie == PersistenceConstants.DELETE);
  }

  /**
   * In 'Wijzig' mode?
   * 
   * @return the wijzig
   */
  public boolean isWijzig() {
    return (aktie == PersistenceConstants.UPDATE);
  }

  /**
   * Detail in 'Wijzig' mode?
   * 
   * @return the wijzig
   */
  public boolean isWijzigDetail() {
    return (detailAktie == PersistenceConstants.UPDATE);
  }

  /**
   * In 'Zoek' mode?
   * 
   * @return the zoek
   */
  public boolean isZoek() {
    return (aktie == PersistenceConstants.RETRIEVE);
  }

  /**
   * Detail in 'Zoek' mode?
   * 
   * @return the zoek
   */
  public boolean isZoekDetail() {
    return (detailAktie == PersistenceConstants.RETRIEVE);
  }

  /**
   * @param detailGefilterd
   */
  public void setDetailGefilterd(boolean detailGefilterd) {
    this.detailGefilterd  = detailGefilterd;
  }

  /**
   * @param detailType de detailType
   */
  public void setDetailType(String detailType) {
    this.detailType = detailType;
  }

  /**
   * @param gefilterd
   */
  public void setGefilterd(boolean gefilterd) {
    this.gefilterd  = gefilterd;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Valideer de invoer.
   */
  public boolean valideerForm() {
    return true;
  }

  /**
   * Valideer de detail invoer.
   */
  public boolean valideerDetailForm() {
    return true;
  }
}
