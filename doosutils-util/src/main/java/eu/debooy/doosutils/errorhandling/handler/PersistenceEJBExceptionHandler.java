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

import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.MultipleObjectFoundException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.SerializableException;
import eu.debooy.doosutils.errorhandling.exception.TechnicalException;
import eu.debooy.doosutils.errorhandling.exception.WrappedException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosError;
import eu.debooy.doosutils.errorhandling.exception.base.DoosException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;
import eu.debooy.doosutils.errorhandling.handler.base.ExceptionHandlerBase;

import java.sql.SQLException;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;


/**
 * @author Marco de Booij
 */
public class PersistenceEJBExceptionHandler extends ExceptionHandlerBase {
  private static final  long  serialVersionUID  = 1L;

  private static  PersistenceEJBExceptionUtil util  = null;

  public PersistenceEJBExceptionHandler(String name, DoosLayer layer,
                                        boolean objectNotFoundPattern) {
    super(name, layer, objectNotFoundPattern);

    if (null == util) {
      util = new PersistenceEJBExceptionUtil();
    }
  }

  public void handle(Throwable t) {
    try {
      throw t;
    } catch (DoosException ie) {
      log(ie);
      throw new WrappedException(getLayer(), ie);
    } catch (DoosRuntimeException ire) {
      log(ire);
      if ((ire instanceof WrappedException)) {
        handle(unwrapException((WrappedException) ire));
      } else {
        throw ire;
      }
    } catch (NoResultException e) {
      DoosRuntimeException  de  =
          new ObjectNotFoundException(DoosLayer.PERSISTENCE,
                                      e.getMessage(), e);
      log(de);
      throw de;
    } catch (NonUniqueResultException e) {
      DoosRuntimeException  de  =
          new MultipleObjectFoundException(DoosLayer.PERSISTENCE,
                                           e.getMessage(), e);
      log(de);
      throw de;
    } catch (EntityExistsException eee){
      DoosRuntimeException  de  =
          new DuplicateObjectException(DoosLayer.PERSISTENCE,
                                       eee.getMessage(), eee);
      log(de);
      throw de;
    } catch (PersistenceException pe) {
      Throwable thr = findRootCause(pe, 5);

      DoosRuntimeException ir = null;

      ir = new TechnicalException(DoosError.RUNTIME_EXCEPTION, getLayer(),
          thr.getMessage(), thr);

      log(ir);
      throw ir;
    } catch (RuntimeException rt) {
      DoosRuntimeException ir = null;
      if (shouldBeSerialized(rt)) {
        ir = new SerializableException(rt);
      } else {
        ir = new TechnicalException(DoosError.RUNTIME_EXCEPTION, getLayer(),
            rt.getMessage(), rt);
      }
      log(ir);
      throw ir;
    } catch (Throwable th) {
      TechnicalException te = new TechnicalException(
          DoosError.RUNTIME_EXCEPTION, getLayer(), th.getMessage(), th);
      log(te);
      throw te;
    }
  }

  private boolean shouldBeSerialized(Throwable t) {
    Package pack  = t.getClass().getPackage();
    if (pack == null) {
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

  public static Throwable findRootCause(Throwable t, int nbTimes) {
    Throwable targetException = t;
    if (null != targetException) {
//      try {
        // TODO Vind de PropertyUtils
//        String exceptionProperty = "targetException";
//        if (PropertyUtils.isReadable(t, exceptionProperty)) {
//          targetException = (Throwable) PropertyUtils.getProperty(t,
//              exceptionProperty);
//        } else {
//          exceptionProperty = "causedByException";
//          if (PropertyUtils.isReadable(th, exceptionProperty)) {
//            targetException = (Throwable) PropertyUtils.getProperty(t,
//                exceptionProperty);
//          }
//        }
//        if (null != targetException) {
//          t = targetException;
//        }
//      } catch (Exception ex) {
//        ex.printStackTrace();
//      }

      if ((null != targetException.getCause()) && (nbTimes != 0)) {
        targetException = t.getCause();
        if ((targetException instanceof SQLException)) {
          return util.transform((SQLException) targetException);
        }
        targetException = findRootCause(targetException, nbTimes--);
      }
    }

    return targetException;
  }
}
