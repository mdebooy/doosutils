/**
 * Copyright 2010 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doosutils.components;

import java.net.URL;

import com.sun.facelets.impl.DefaultResourceResolver;
import com.sun.facelets.impl.ResourceResolver;


/**
 * @author Marco de Booij
 */
public class CustomResourceResolver extends DefaultResourceResolver
    implements ResourceResolver {
  public URL resolveUrl(String path) {
    URL resourceUrl = super.resolveUrl(path);
    if (resourceUrl == null) {
      if (path.startsWith("/")) {
        path  = path.substring(1);
      }
      resourceUrl = Thread.currentThread().getContextClassLoader()
                          .getResource(path);
    }
    return resourceUrl;
  }
}
