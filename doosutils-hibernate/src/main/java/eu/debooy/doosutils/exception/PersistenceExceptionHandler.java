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

import eu.debooy.doosutils.business.IProfiler;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;

import java.util.Collection;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;


/**
 * @author Marco de Booij
 */
public class PersistenceExceptionHandler {
  IProfiler profiledClass;

  public PersistenceExceptionHandler() {
    this.profiledClass  = null;
  }

  @AroundInvoke
  public Object handleException(InvocationContext invocation) throws Exception {
    if (!(invocation.getTarget() instanceof IProfiler)) {
      throw new IllegalArgumentException("When calling the "
          + PersistenceExceptionHandler.class.getName()
          + ", your class must implement the "
          + IProfiler.class.getCanonicalName() + " interface",
          this.profiledClass.getApplicationName());
    }

    this.profiledClass = ((IProfiler) invocation.getTarget());
    Logger  logger  = this.profiledClass.getLogger();
    Object  object  = null;
    try {
      object  = invocation.proceed();
    } catch (IllegalArgumentException iae) {  
      iae.setApplication(invocation.getMethod().getName() + ": "
                         + iae.getErrorMessage());
      throw iae;
    } catch (Exception exc) {
      if (logger.isTraceEnabled())
        logTraceMessage(logger, invocation.getMethod().getName()
                        + " Persistence error");
      throw new DAOException(exc, this.profiledClass.getApplicationName());
    }
    if ((object == null)
        && (!("void".equals(invocation.getMethod().getReturnType()
                                                  .getName())))) {
      throw new ObjectNotFoundException(this.profiledClass + "."
          + invocation.getMethod().getName()
          + " couldn't retrieve the required object with parameters "
          + convertParameters(invocation.getParameters()),
          this.profiledClass.getApplicationName());
    }

    if ((object instanceof Collection<?>)
        && (((Collection<?>) object).size() == 0)) {
      throw new ObjectNotFoundException(this.profiledClass + "."
          + invocation.getMethod().getName()
          + " couldn't retrieve the required object with parameters "
          + convertParameters(invocation.getParameters()),
          this.profiledClass.getApplicationName());
    }

    return object;
  }

  private void logTraceMessage(Logger logger, String message) {
    if ((logger != null) && (logger.isTraceEnabled()))
      logger.trace(message);
  }

  private String convertParameter(Object object) {
    if (object == null) {
      return "<NULL>";
    }
    if (object instanceof Long) {
      return ((Long) object).toString();
    }
    if (object instanceof String) {
      return ((String) object);
    }

    return object.getClass().getSimpleName();
  }

  private String convertParameters(Object[] objects) {
    StringBuffer params = new StringBuffer("");
    for (Object object : objects) {
      if (params.length() > 0) {
        params.append(", ");
      }
      params.append(convertParameter(object));
    }

    return params.toString();
  }
}
