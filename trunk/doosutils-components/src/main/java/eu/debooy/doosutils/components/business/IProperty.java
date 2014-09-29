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
package eu.debooy.doosutils.components.business;

import eu.debooy.doosutils.components.Applicatieparameter;

import java.util.List;

import javax.ejb.Remote;


/**
 * @author Marco de Booij
 */
// TODO Toegang beperken via rollen.
@Remote
public interface IProperty {
  void                      clear();
  void                      delete(String property);
  List<Applicatieparameter> getProperties(String applicatie);
  // Voor iedereen
  String                    getProperty(String property);
  void                      update(Applicatieparameter property);
  void                      update(String property, String waarde);
  int                       size();
}
