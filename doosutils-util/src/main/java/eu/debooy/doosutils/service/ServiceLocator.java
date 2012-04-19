/**
 * Copyright 2009 Marco de Booij
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
package eu.debooy.doosutils.service;

import eu.debooy.doosutils.errorhandling.exception.base.DoosError;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Marco de Booij
 */
public final class ServiceLocator {
  private static Log            log       =
    LogFactory.getLog(ServiceLocator.class);

  private static List<Context>  contexts  = new ArrayList<Context>();
  private static ServiceLocator locator   = new ServiceLocator();

  private ServiceLocator() {
    try {
      contexts.add(new InitialContext());
      loadBackupContexts();
    } catch (NamingException ne) {
      log.error("Error in CTX lookup", ne);
    }
  }

  public static ServiceLocator getInstance() {
    return locator;
  }

  public static ServiceLocator forceInstance(Properties env) {
    log.warn("Default InitialContext wordt overschreven.");
    if ((env == null) || (!(env.containsKey("java.naming.provider.url")))) {
      throw new IllegalArgumentException(
          "forceInstance: Context environment mag niet null zijn en moet "
          + "minstens 1 'provider URL' hebben.");
    }
    try {
      Context initialContext = new InitialContext(env);
      if (contexts.size() > 0) {
        contexts.set(0, initialContext);
      } else {
        contexts.add(initialContext);
      }
    } catch (NamingException ne) {
      log.error("Error in CTX lookup", ne);
    }
    return locator;
  }

  /**
   * Get de DataSource
   * 
   * @param jndi
   * @return
   * @throws ServiceLocatorException
   */
  public DataSource getDataSource(String jndi)
      throws ServiceLocatorException {
    if (jndi == null) {
      throw new IllegalArgumentException(
          "getDataSource: JNDI mag niet null zijn.");
    }
    log.debug("getDataSource: Zoek JNDI " + jndi);
    DataSource datasource = (DataSource) lookup(jndi);
    if (datasource == null) {
      log.error("getDataSource: Kan geen datasource vinden met JNDI="
                + jndi + " in geen enkele context.");
      throw new ServiceLocatorException(DoosError.OBJECT_NOT_FOUND,
                  DoosLayer.BUSINESS, "Kan geen datasource vinden met JNDI="
                  + jndi + " in geen enkele context.");
    }
    log.debug("getDataSource: Gevonden.");

    return datasource;
  }

  /**
   * Get de EJB
   * 
   * @param jndi
   * @return
   * @throws ServiceLocatorException
   */
  public Object getEJB(String resource) throws ServiceLocatorException {
    if (null == resource) {
      throw new IllegalArgumentException("getEJB: JNDI mag niet null zijn.");
    }
    String  appName = (String) lookup("java:app/AppName");
    String  jndi    = "java:global/" + appName + "/" + resource;
    log.debug("getEJB: Zoek JNDI " + jndi);
    Object ejbObject = lookup(jndi);
    if (null == ejbObject) {
      jndi      = resource;
      ejbObject = lookup(jndi);
      if (null == ejbObject) {
        log.error("getEJB: Kan geen EJB vinden met JNDI=" + jndi
                  + " in geen enkele context.");
        throw new ServiceLocatorException(DoosError.OBJECT_NOT_FOUND,
                    DoosLayer.BUSINESS, "Kan geen EJB vinden met JNDI="
                    + jndi + " in geen enkele context.");
      }
    }
    log.debug("getEJB: Gevonden.");

    return ejbObject;
  }

  /**
   * Get de EJB
   * 
   * @param mappedName
   * @param interfaceClassName
   * @param params
   * @return
   * @throws ServiceLocatorException
   */
  public Object getEJB(String mappedName, Class<?> interfaceClassName,
                       String... params) throws ServiceLocatorException {
    String canonicalName = interfaceClassName.getCanonicalName();

    return getEJB(mappedName, canonicalName, params);
  }

  /**
   * Get de EJB
   * 
   * @param mappedName
   * @param interfaceClassName
   * @param params
   * @return
   * @throws ServiceLocatorException
   */
  public Object getEJB(String mappedName, String className,
                       String... params) throws ServiceLocatorException {
    if (mappedName == null) {
      throw
        new IllegalArgumentException("getEJB: mappedName mag niet null zijn.");
    }

    if (className == null) {
      throw new IllegalArgumentException(
          "getEJB: ClassName mag niet null zijn.");
    }

    // Glassfish local
    String  appName   = (String) lookup("java:app/AppName");
    String  jndi      = "java:global/" + appName + "/" + mappedName + "!"
                        + className;
    Object  ejbObject = lookup(jndi);
    if (null == ejbObject) {
      // Glassfish remote
      jndi      = mappedName + "!" + className;
      ejbObject = lookup(jndi);
      if (null == ejbObject) {
        log.error("getEJB: Kan geen EJB vinden met JNDI=" + jndi
                  + " in geen enkele context.");
        throw new ServiceLocatorException(DoosError.OBJECT_NOT_FOUND,
                    DoosLayer.BUSINESS, "Kan geen EJB vinden met JNDI="
                    + jndi + " in geen enkele context.");
      }
    }
    log.debug("getEJB: Gevonden.");

    return ejbObject;
  }

  private void listContext(String s, Context context) throws NamingException {
    NamingEnumeration<NameClassPair>  pairs = context.list("");
    for (; pairs.hasMoreElements();) {
      NameClassPair pair  = pairs.next();
      log.debug(s + "/" + pair.getName() + " " + pair.getClassName());
      Object obj  = context.lookup(pair.getName());
      if (obj instanceof Context) {
        Context child = (Context) obj;
        listContext(s + "/" + pair.getName(), child);
      }
    }
  }

  private Object lookup(String jndi) {
    Context context = null;
    for (int i = 0; i < contexts.size(); ++i) {
      context = (Context) contexts.get(i);
      try {
        listContext("", context);
      } catch (NamingException e) {
        log.debug(e.getMessage());
      }
      try {
        Object object = context.lookup(jndi);
        return object;
      } catch (NamingException e) {
        log.debug("JNDI: " + jndi + " [" + e.getMessage() + "]");
      }
    }
    return null;
  }

  private void loadBackupContexts() {
    InputStream xmlStream = super.getClass().getClassLoader()
                                 .getResourceAsStream("locator-properties.xml");
    if (xmlStream != null) {
      log.info("Toevoegen van backup context providers...");
      Properties props = new Properties();
      try {
        props.loadFromXML(xmlStream);
        int i = 1;
        Properties env = null;
        while (props.containsKey("server" + i + ".url")) {
          env = new Properties();
          env.put("java.naming.provider.url", props.get("server" + i + ".url"));
          if (props.containsKey("server" + i + ".contextFactory")) {
            env.put("java.naming.factory.initial", props.get("server" + i
                    + ".contextFactory"));
          }
          if (props.containsKey("server" + i + ".principal")) {
            env.put("java.naming.security.principal", props.get("server" + i
                + ".principal"));
          }
          if (props.containsKey("server" + i + ".credentials")) {
            env.put("java.naming.security.credentials", props.get("server" + i
                + ".credentials"));
          }
          try {
            Context ctx = new InitialContext(env);
            contexts.add(ctx);
          } catch (NamingException e) {
            log.warn(
                "loadBackupContexts: Kan geen context maken voor server " + i
                 + ", server wordt genegeerd.", e);
          }
          ++i;
        }
      } catch (InvalidPropertiesFormatException e) {
        log.error("loadBackupContexts: Kan locator-properties.xml niet parsen.",
                  e);
      } catch (IOException e) {
        log.error(
            "loadBackupContexts: Kan locator-properties.xml niet lezen.", e);
      }
    }
  }

  public static ServiceLocator addContext(Properties env) {
    if ((env == null) || (!(env.containsKey("java.naming.provider.url"))))
      throw new IllegalArgumentException(
          "addContext: Context environment mag niet null zijn en moet "
          + "minstens 1 'provider URL' hebben.");
    try {
      Context context = new InitialContext(env);
      contexts.add(context);
    } catch (NamingException ne) {
      log.error("Error in CTX lookup", ne);
    }
    return locator;
  }
}
