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

import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.components.business.II18nTekst;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


/**
 * @author Marco de Booij
 */
@Named
@SessionScoped
public class I18nTeksten implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  @EJB
  private II18nTekst  i18nTekstBean;

  public String tekst(String code) {
    if (DoosUtils.isBlankOrNull(code)) {
      return "<null>";
    }

//    Gebruiker gebruiker = (Gebruiker) CDI.getBean("gebruiker");

    return i18nTekstBean.getI18nTekst(code, "nl");
//                                      gebruiker.getLocale().getLanguage());
  }
}
