/**
 * Copyright 2009 Marco de Booij
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
package eu.debooy.doosutils.errorhandling.exception;

import eu.debooy.doosutils.exception.LoggableException;

//import javax.ejb.ApplicationException;


/**
 * @author Marco de Booij
 * @TODO EJB of niet
 */
//@ApplicationException
public class ObjectNotFoundException extends LoggableException {
  private static final long serialVersionUID = 1L;

  public ObjectNotFoundException(Exception e, String application) {
    super(e, application);
  }

  public ObjectNotFoundException(String message, String application) {
    super(message, application);
  }
}
