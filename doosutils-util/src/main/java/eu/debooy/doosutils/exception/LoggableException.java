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
package eu.debooy.doosutils.exception;



/**
 * @author Marco de Booij
 */
public class LoggableException extends Exception {
  private static final  long  serialVersionUID  = 1L;

  protected String  application;
  protected String  errorMessage;

  public LoggableException(LoggableException le) {
    super(le);

    this.application  = le.getApplication();
    this.errorMessage = le.getErrorMessage();
  }

  public LoggableException(Exception e, String application) {
    super(e);

    this.application = application;
    Throwable cause = ExceptionLogUtil.getMainException(e);
    this.errorMessage = cause.getMessage();
  }

  public LoggableException(String message, String application) {
    super(message);

    this.application  = application;
    this.errorMessage = message;
  }

  /**
   * @return the application
   */
  public String getApplication() {
    return application;
  }

  /**
   * @param application the application to set
   */
  public void setApplication(String application) {
    this.application = application;
  }

  /**
   * @return the errorMessage
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * @param errorMessage the errorMessage to set
   */
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
