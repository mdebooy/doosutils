/**
 * Copyright 2009 Marco de Booij
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
package eu.debooy.doosutils.errorhandling.exception.base;


/**
 * @author Marco de Booij
 */
public class DoosException extends Exception
    implements IDoosException {
  private static final long serialVersionUID = 1L;

  private DoosError error;
  private DoosLayer layer;
  private boolean   loggable;
  private boolean   logged;

  public DoosException() {
    super();
  }

  /**
   * @param doosError Type of error
   * @param doosLayer Layer where the error was thrown
   * @param loggable  Exption loggable or not?
   * @param message   Explanation of the runtimeException
   * @param cause     Cause of the runtimeException
   */
  public DoosException(DoosError error, DoosLayer layer, boolean loggable,
                       String message, Throwable cause) {
    super(message, cause);
    this.error    = error;
    this.layer    = layer;
    this.loggable = loggable;
    this.logged   = false;
  }

  /**
   * @param doosError Type of error
   * @param doosLayer Layer where the error was thrown
   * @param loggable  Exption loggable or not?
   * @param message   Explanation of the runtimeException
   * @param cause     Cause of the runtimeException
   */
  public DoosException(DoosError error, DoosLayer layer, String message,
                       Throwable cause) {
    this(error, layer, true, message, cause);
  }

  /**
   * @param doosError Type of error
   * @param doosLayer Layer where the error was thrown
   * @param loggable  Exption loggable or not?
   * @param message   Explanation of the runtimeException
   */
  public DoosException(DoosError error, DoosLayer layer, boolean loggable,
                       String message) {
    this(error, layer, loggable, message, null);
  }

  /**
   * @param doosError Type of error
   * @param doosLayer Layer where the error was thrown
   * @param message   Explanation of the runtimeException
   */
  public DoosException(DoosError error, DoosLayer layer, String message) {
    this(error, layer, true, message, null);
  }

  public DoosError getDoosError() {
    return this.error;
  }

  public DoosLayer getDoosLayer() {
    return this.layer;
  }

  public boolean isLogged() {
    return this.logged;
  }

  public void setLoggedTrue() {
    this.logged = true;
  }

  public boolean isLoggable() {
    return this.loggable;
  }

  public void setLoggable(boolean loggable) {
    this.loggable = loggable;
  }

  public String getStackTraceAsString() {
    return DoosExceptionHelper.getStackTrace(this);
  }

  public String toString() {
    return "DoosException ( " + super.toString() + "    " + "    "
           + "layer = " + this.layer + "    " + "error = " + this.error + " )";
  }
}