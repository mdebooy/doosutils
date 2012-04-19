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
package eu.debooy.doosutils.errorhandling.handler.base;

import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;
import eu.debooy.doosutils.errorhandling.exception.base.IDoosException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
public abstract class ExceptionHandlerBase implements IExceptionHandler {
  private static final  long  serialVersionUID  = 1L;

  private static final Logger logger                =
      LoggerFactory.getLogger(ExceptionHandlerBase.class);
  private String              name;
  private DoosLayer           layer;
  private boolean             objectNotFoundPattern = true;

  public ExceptionHandlerBase(String name, DoosLayer layer,
                              boolean objectNotFoundPattern) {
    this.name                   = name;
    this.layer                  = layer;
    this.objectNotFoundPattern  = objectNotFoundPattern;
  }

  public void log(IDoosException e) {
    if ((Boolean.TRUE.booleanValue() != e.isLoggable())
        && (Boolean.TRUE.booleanValue() != e.isLogged())) {
      logger.error("IDoosException logged by " + getName() + " handler",
                   (Throwable) e);

      e.setLoggedTrue();
    }
  }

  public String getName() {
    return name;
  }

  public DoosLayer getLayer() {
    return layer;
  }

  public boolean isObjectNotFoundPattern() {
    return objectNotFoundPattern;
  }
}
