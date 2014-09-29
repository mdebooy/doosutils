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
package eu.debooy.doosutils.errorhandling.exception;

import eu.debooy.doosutils.errorhandling.exception.base.DoosError;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;

import javax.ejb.ApplicationException;


/**
 * @author Marco de Booij
 */
@ApplicationException
public class TechnicalException extends DoosRuntimeException {
  private static final  long  serialVersionUID  = 1L;

  private boolean recoverableInTime = false;

  public TechnicalException(DoosError error, DoosLayer layer,
                            boolean loggable, String message, Throwable cause) {
    super(error, layer, loggable, message, cause);
  }

  public TechnicalException(DoosError error, DoosLayer layer,
                            boolean loggable, String message) {
    super(error, layer, loggable, message, null);
  }

  public TechnicalException(DoosError error, DoosLayer layer, String message,
                            Throwable cause) {
    super(error, layer, true, message, cause);
  }

  public TechnicalException(DoosError error, DoosLayer layer, String message) {
    super(error, layer, true, message, null);
  }

  public boolean isRecoverableInTime() {
    return this.recoverableInTime;
  }

  public void setRecoverableInTime(boolean recoverableInTime) {
    this.recoverableInTime = recoverableInTime;
  }
}
