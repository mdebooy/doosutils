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
import eu.debooy.doosutils.HibernateConstants;
import eu.debooy.doosutils.components.Property;
import eu.debooy.doosutils.components.bean.JsfBean;

import java.util.HashMap;
import java.util.Map;

import javax.faces.event.ActionEvent;


/**
 * @author Marco de Booij
 */
public class DataController extends JsfBean {
  private static final  long  serialVersionUID  = 1L;

  protected boolean     gefilterd = false;
  protected char        aktie     = HibernateConstants.RETRIEVE;
  protected String      subTitel  = "";
  protected String      type;

  protected Map<String, String> getLijstKleuren(String applicatie) {
    Map<String, String> kleuren   = new HashMap<String, String>();
    Property            property  =
        (Property) getExternalContext().getSessionMap().get("properties");

    String  kleur = property.value(applicatie
                                   + ".lijst.columnheader.background");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("columnheader.background", kleur);
    }
    kleur = property.value(applicatie + ".lijst.columnheader.foreground");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("columnheader.foreground", kleur);
    }
    kleur = property.value(applicatie + ".lijst.footer.background");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("footer.background", kleur);
    }
    kleur = property.value(applicatie + ".lijst.footer.foreground");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("footer.foreground", kleur);
    }
    kleur = property.value(applicatie + ".lijst.row.background");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("row.background", kleur);
    }
    kleur = property.value(applicatie + ".lijst.row.foreground");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("row.foreground", kleur);
    }
    kleur = property.value(applicatie + ".lijst.row.conditional.background");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("row.conditional.background", kleur);
    }
    kleur = property.value(applicatie + ".lijst.row.conditional.foreground");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("row.conditional.foreground", kleur);
    }
    kleur = property.value(applicatie + ".lijst.titel.background");
    if (DoosUtils.isNotBlankOrNull(kleur)) {
      kleuren.put("titel.background", kleur);
    }
    kleur = property.value(applicatie + ".lijst.titel.foreground");
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
    return (aktie == HibernateConstants.CREATE);
  }

  /**
   * In 'Verwijder' mode?
   * 
   * @return
   */
  public boolean isVerwijder() {
    return (aktie == HibernateConstants.DELETE);
  }

  /**
   * In 'Wijzig' mode?
   * 
   * @return the wijzig
   */
  public boolean isWijzig() {
    return (aktie == HibernateConstants.UPDATE);
  }

  /**
   * In 'Zoek' mode?
   * 
   * @return the zoek
   */
  public boolean isZoek() {
    return (aktie == HibernateConstants.RETRIEVE);
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
   * @param event
   */
  public void valueChangeForm(ActionEvent event) {
    setPageDirty(Boolean.valueOf(true));
  }
}
