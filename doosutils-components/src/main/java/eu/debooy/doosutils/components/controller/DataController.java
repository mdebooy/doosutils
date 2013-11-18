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
import eu.debooy.doosutils.components.bean.DoosBean;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
public class DataController extends DoosBean {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(DataController.class.getName());

  private boolean     detailGefilterd = false;
  private boolean     gefilterd       = false;
  private boolean     invoer          = true;
  private char        aktie           = PersistenceConstants.RETRIEVE;
  private char        detailAktie     = PersistenceConstants.RETRIEVE;
  private String      detailSubTitel  = "";
  private String      detailType;
  private String[]    exportTypes     = new String[] {"ONBEKEND"};
  private String      subTitel        = "";
  private String      type;

  /**
   * Schrijf een nieuwe rij in de database.
   */
  public void create() {
    LOGGER.error("create() niet toegestaan.");
  }

  /**
   * Schrijf een nieuwe rij in de database.
   */
  public void createDetail() {
    LOGGER.error("createDetail() niet toegestaan.");
  }

  /**
   * Verwijder een rij uit de database.
   */
  public void delete() {
    LOGGER.error("delete() niet toegestaan.");
  }

  /**
   * Verwijder een rij uit de database.
   */
  public void deleteDetail() {
    LOGGER.error("deleteDetail() niet toegestaan.");
  }

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
   * Is het exportType toegestaan?
   * 
   * @param exportType
   * @return
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
   * Is het toegestaan om data in te voeren?
   * 
   * @return invoer
   */
  public boolean isInvoer() {
    return invoer;
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
   * Reset de bean.
   */
  public void reset() {
    super.reset();

    setAktie(PersistenceConstants.RETRIEVE);
    setDetailAktie(PersistenceConstants.RETRIEVE);
    setDetailGefilterd(false);
    setGefilterd(false);
  }

  /**
   * Bewaar de rij in de database.
   */
  public void save() {
    LOGGER.error("save() niet toegestaan.");
  }

  /**
   * Bewaar de rij in de database.
   */
  public void saveDetail() {
    LOGGER.error("saveDetail() niet toegestaan.");
  }

  /**
   * Zoek in de database.
   */
  public void search() {
    LOGGER.error("search() niet toegestaan.");
  }

  /**
   * Zoek in de database.
   */
  public void searchDetail() {
    LOGGER.error("searchDetail() niet toegestaan.");
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
   * @param exportTypes
   */
  // TODO Test de exportTypes.
  public void setExportTypes(String... exportTypes) {
    this.exportTypes  = new String[exportTypes.length];
    for (int i = 0; i < exportTypes.length; i++) {
      this.exportTypes[i] = exportTypes[i];
    }
  }

  /**
   * @param gefilterd
   */
  public void setGefilterd(boolean gefilterd) {
    this.gefilterd  = gefilterd;
  }

  /**
   * @param invoer
   */
  public void setInvoer(boolean invoer) {
    this.invoer = invoer;
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
