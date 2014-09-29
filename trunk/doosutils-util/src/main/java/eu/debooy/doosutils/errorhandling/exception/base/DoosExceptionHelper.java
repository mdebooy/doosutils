/**
 * Copyright 2009 Marco de Booy
 *
 * Licensed under the EUPL, Version 1.0 or - as soon they will be approved by
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
package eu.debooy.doosutils.errorhandling.exception.base;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * @author Marco de Booij
 */
public final class DoosExceptionHelper {
  private DoosExceptionHelper() {}

  public static String getStackTrace(Throwable t) {
    StringWriter  sw  = new StringWriter();
    PrintWriter   pw  = new PrintWriter(sw);
    t.printStackTrace(pw);

    return sw.toString();
  }

  public static String convertParameter(Object object) {
    if (null == object) {
      return "<NULL>";
    }
    if (object instanceof Long) {
      return ((Long) object).toString();
    }
    if (object instanceof String) {
      return ((String)object);
    }
    return "";
  }

  public static String convertParameters(Object[] objects) {
    if (null == objects) {
      return "<NULL>";
    }

    StringBuilder params  = new StringBuilder("");
    for (Object object : objects) {
      if (params.length() > 0) {
        params.append(", ");
      }
      params.append(convertParameter(object));
    }
    return params.toString();
  }
}
