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

  private boolean     detailGefilterd = false;
  private boolean     gefilterd       = false;
  private char        aktie           = PersistenceConstants.RETRIEVE;
  private char        detailAktie     = PersistenceConstants.RETRIEVE;
  private String      detailSubTitel  = "";
  private String      detailType;
  private String      subTitel        = "";
  private String      type;

  /**
   * @return de aktie
   */
  public char getAktie() {
    return aktie;
  }

  /**
   * @return de aktie van de details.
   */
  public char getDetailAktie() {
    return detailAktie;
  }

  /**
   * @return de sub-titel van de details.
   */
  public String getDetailSubTitel() {
    return detailSubTitel;
  }

  /**
   * @return de detailType
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
   * In 'Bekijk' mode?
   * 
   * @return
   */
  public boolean isBekijk() {
    return (aktie == PersistenceConstants.RETRIEVE);
  }

  /**
   * In 'Bekijk' mode?
   * 
   * @return
   */
  public boolean isBekijkDetail() {
    return (detailAktie == PersistenceConstants.RETRIEVE);
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
   * In 'Gefilterde' mode?
   * 
   * @return detailGefilterd
   */
  public boolean isGefilterdDetail() {
    return detailGefilterd;
  }

  /**
   * In 'Nieuw' mode?
   * 
   * @return
   */
  public boolean isNieuw() {
    return (aktie == PersistenceConstants.CREATE);
  }

  /**
   * Detail in 'Nieuw' mode?
   * 
   * @return
   */
  public boolean isNieuwDetail() {
    return (detailAktie == PersistenceConstants.CREATE);
  }

  /**
   * In read-only mode?
   * 
   * @retrun
   */
  public boolean isReadonly() {
    return (aktie == PersistenceConstants.DELETE)
        || (aktie == PersistenceConstants.RETRIEVE);
  }

  /**
   * In read-only mode?
   * 
   * @retrun
   */
  public boolean isReadonlyDetail() {
    return (detailAktie == PersistenceConstants.DELETE)
        || (detailAktie == PersistenceConstants.RETRIEVE);
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
   * @return
   */
  public boolean isWijzig() {
    return (aktie == PersistenceConstants.UPDATE);
  }

  /**
   * Detail in 'Wijzig' mode?
   * 
   * @return
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
    return (aktie == PersistenceConstants.SEARCH);
  }

  /**
   * Detail in 'Zoek' mode?
   * 
   * @return
   */
  public boolean isZoekDetail() {
    return (detailAktie == PersistenceConstants.SEARCH);
  }

  /**
   * @param aktie
   */
  public void setAktie(char aktie) {
    this.aktie  = aktie;
  }

  /**
   * @param aktie
   */
  public void setDetailAktie(char detailAktie) {
    this.detailAktie  = detailAktie;
  }

  /**
   * @param detailGefilterd
   */
  public void setDetailGefilterd(boolean detailGefilterd) {
    this.detailGefilterd  = detailGefilterd;
  }

  /**
   * @param detailSubTitel de detailSubTitel
   */
  public void setDetailSubTitel(String detailSubTitel) {
    this.detailSubTitel = detailSubTitel;
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
   * @param subTitel de subTitel
   */
  public void setSubTitel(String subTitel) {
    this.subTitel = subTitel;
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
