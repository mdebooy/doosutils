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
package eu.debooy.doosutils.errorhandling.handler;

import eu.debooy.doosutils.errorhandling.exception.SerializableException;
import eu.debooy.doosutils.errorhandling.exception.TechnicalException;
import eu.debooy.doosutils.errorhandling.exception.WrappedException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosError;
import eu.debooy.doosutils.errorhandling.exception.base.DoosException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;
import eu.debooy.doosutils.errorhandling.handler.base.ExceptionHandlerBase;


/**
 * @author Marco de Booij
 */
public class DefaultEJBExceptionHandler extends ExceptionHandlerBase {
  private static final  long  serialVersionUID  = 1L;

  public DefaultEJBExceptionHandler(String name, DoosLayer layer,
                                    boolean objectNotFoundPattern) {
    super(name, layer, objectNotFoundPattern);
  }

  public void handle(Throwable t) {
    try {
      throw t;
    } catch (DoosException e) {
      log(e);
      throw new WrappedException(getLayer(), e);
    } catch (DoosRuntimeException e) {
      log(e);
      if ((e instanceof WrappedException)) {
        handle(unwrapException((WrappedException) e));
      } else {
        throw e;
      }
    } catch (RuntimeException e) {
      DoosRuntimeException  de  = null;
      if (shouldBeSerialized(e)) {
        de  = new SerializableException(e);
      } else {
        de  = new TechnicalException(DoosError.RUNTIME_EXCEPTION, getLayer(),
                                     e.getMessage(), e);
      }
      log(de);
      throw de;
    } catch (Throwable e) {
      TechnicalException  te  =
          new TechnicalException(DoosError.RUNTIME_EXCEPTION, getLayer(),
                                 e.getMessage(), e);
      log(te);
      throw te;
    }
  }

  private boolean shouldBeSerialized(Throwable t) {
    Package pack  = t.getClass().getPackage();
    if (null == pack) {
      return false;
    }
    if (pack.getName().startsWith("java.")) {
      return false;
    }
    if (pack.getName().startsWith("javax.")) {
      return false;
    }

    return !(t instanceof RuntimeException);
  }

  private Throwable unwrapException(WrappedException e) {
    Throwable t = e;
    while ((t instanceof WrappedException)) {
      t = t.getCause();
    }

    return t;
  }
}
