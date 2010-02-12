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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;


/**
 * @author Marco de Booij
 */
public class ExceptionLogUtil {
  private static final int _MAX_DEPTH = 5;

  public static Throwable getRootCause(Throwable cause) {
    if (cause != null) {
      if (cause.getCause() != null) {
        getRootCause(cause, 0);
      } else {
        return cause;
      }
    }

    return null;
  }

  private static Throwable getRootCause(Throwable cause, int level) {
    if ((level++ < _MAX_DEPTH) 
        && (cause != null)) {
      if (cause.getCause() != null) {
        getRootCause(cause, level);
      } else {
        return cause;
      }
    }

    return cause;
  }

  public static Throwable getMainException(Exception e) {
    if (e instanceof RemoteException) {
      RemoteException re    = (RemoteException) e;
      Throwable       cause = re.detail;
      if (cause instanceof Exception) {
        return cause;
      }
    }
    return e;
  }

  public static String getStackTrace(Throwable cause) {
    StringWriter  sw      = new StringWriter();
    cause.printStackTrace(new PrintWriter(sw));
    StringBuffer  sb      = sw.getBuffer();
    String        result  = null;
    if (sb.length() > 4000)
      result  = sb.substring(0, 3999);
    else {
      result  = sb.toString();
    }

    return result;
  }
}
