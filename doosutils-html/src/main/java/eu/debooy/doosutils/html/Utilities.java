/**
 * Copyright 2008 Marco de Booy
 *
 * Licensed under the EUPL, Version 1.0 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/7330l5
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doosutils.html;


/**
 * @author Marco de Booy
 * @version $Revision$
 */
public class Utilities {
  public static String kwart(Double value) {
    String[]  displayString = {"", "&frac14;", "&frac12;", "&frac34;"};

    return displayString[(int)((value - value.intValue()) * 4)];
  }
}
