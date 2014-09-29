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
package eu.debooy.doosutils.errorhandling.handler.interceptor;

import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosExceptionHelper;
import eu.debooy.doosutils.errorhandling.handler.base.ExceptionHandlerFactory;
import eu.debooy.doosutils.errorhandling.handler.base.IExceptionHandler;

import java.io.Serializable;
import java.util.Collection;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;


/**
 * @author Marco de Booij
 */
public class PersistenceExceptionHandlerInterceptor implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  private IExceptionHandler handler;

  public PersistenceExceptionHandlerInterceptor() {
    this.handler  = ExceptionHandlerFactory.getPersistenceHandler();
  }

  public PersistenceExceptionHandlerInterceptor(IExceptionHandler handler) {
    this.handler  = handler;
  }

  /**
   * Verwijder de throws Exception niet omdat anders de applicatie niet
   * ge-deployed kan worden.
   */
  @AroundInvoke
  public Object handleException(InvocationContext invocation)
      throws Exception {
    Object  object  = null;

    try {
      object  = invocation.proceed();
    } catch (Throwable t) {
      handler.handle(t);
    }

    if (handler.isObjectNotFoundPattern()) {
      handleObjectNotFoundPattern(invocation, object);
    }

    return object;
  }

  /**
   * Behandel het object not found pattern.
   * Genereer een ObjectNotFoundException als er geen DAO gevonden is.
   * 
   * @param invocation De InvocationContext
   * @param object Het object dat niet gevonden werd.
   */
  private void handleObjectNotFoundPattern(InvocationContext invocation,
                                           Object object)
      throws ObjectNotFoundException {
    ObjectNotFoundException e = null;
    if (!"void".equals(invocation.getMethod().getReturnType().getName())) {
      if (null == object) {
        e = buildObjectNotFoundException(invocation);
      } else if (((object instanceof Collection))
          && (((Collection<?>) object).size() == 0)) {
        e = buildObjectNotFoundException(invocation);
      }
    }

    if (null != e) {
      handler.log(e);
      throw e;
    }
  }

  /**
   * Maak de ObjectNotFoundException exception.
   * 
   * @param invocation De InvocationContext
   * @return ObjectNotFoundException
   */
  private ObjectNotFoundException
      buildObjectNotFoundException(InvocationContext invocation) {
    return new ObjectNotFoundException(this.handler.getLayer(),
                                       invocation.getMethod().getName()
          + " kon het gevraagde object met parameters "
          + DoosExceptionHelper.convertParameters(invocation.getParameters())
          + " niet vinden.");
  }
}
