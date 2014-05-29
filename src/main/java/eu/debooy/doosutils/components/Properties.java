/**
 * Copyright 2011 Marco de Booij
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
package eu.debooy.doosutils.components;

import eu.debooy.doosutils.components.business.IProperty;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


/**
 * @author Marco de Booij
 */
@Named
@SessionScoped
public class Properties implements Serializable {
  private static final  long    serialVersionUID  = 1L;

  public static final   String  BEAN_NAME = "properties";

  private List<Applicatieparameter> properties;
  private Applicatieparameter       property;

  @EJB
  private IProperty propertyBean;

  /**
   * Stop de laatste aktie.
   */
  public void cancel() {
    property  = null;
  }

  /**
   * Geef de parameters/properties van een applicatie.
   * 
   * @param applicatie
   * @return
   */
  public List<Applicatieparameter> properties(String applicatie) {
    if (null == properties) {
      properties  = propertyBean.getProperties(applicatie);
    }

    return properties;
  }

  /**
   * Geef de geselecteerde parameter/property.
   * 
   * @return
   */
  public Applicatieparameter getProperty() {
    return property;
  }

  /**
   * Geef de waarde van een parameter/property.
   * 
   * @param property
   * @return
   */
  public String value(String property) {
    return propertyBean.getProperty(property);
  }

  /**
   * Bewaar de Applicatieparameter in de database.
   */
  public void save() {
    propertyBean.update(property);
    property  = null;
  }

  /**
   * @param property
   */
  public void wijzig(Applicatieparameter property) {
    this.property = property;
  }
}
