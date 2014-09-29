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

import eu.debooy.doosutils.errorhandling.exception.base.DoosError;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;

import javax.ejb.ApplicationException;


/**
 * @author Marco de Booij
 */ 
@ApplicationException
public class DuplicateObjectException extends DoosRuntimeException {
  private static final long serialVersionUID = 1L;

  private Object  failedObject;

  public Object getFailedObject() {
      return failedObject;
  }

  public DuplicateObjectException(DoosLayer layer, String message) {
    super(DoosError.DUPLICATE_OBJECT, layer, message);
  }

  public DuplicateObjectException(DoosLayer layer, String message,
                                      Throwable cause) {
    super(DoosError.DUPLICATE_OBJECT, layer, message, cause);
  }

  public DuplicateObjectException(DoosLayer doosLayer, Object failedObject,
                                  String message) {
    super(DoosError.DUPLICATE_OBJECT, doosLayer, message);
    this.failedObject = failedObject;
  }

  public DuplicateObjectException(DoosLayer doosLayer, Object failedObject,
                                  String message, Throwable cause) {
    super(DoosError.DUPLICATE_OBJECT, doosLayer, message, cause);
    this.failedObject = failedObject;
  }
}
