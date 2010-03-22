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
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;



/**
 * @author Marco de Booij
 */
public class ServiceLocator {
  static Logger
    logger    = Logger.getLogger(ServiceLocator.class);

  private static List<Context>
    contexts  = new ArrayList<Context>();
  private static ServiceLocator
    _instance = new ServiceLocator();

  private ServiceLocator() {
    try {
      contexts.add(new InitialContext());
      loadBackupContexts();
    } catch (NamingException ne) {
      logger.error("Error in CTX lookup", ne);
    }
  }

  public static ServiceLocator getInstance() {
    return _instance;
  }

  public static ServiceLocator forceInstance(Properties env) {
    logger.warn("Default InitialContext wordt overschreven.");
    if ((env == null) || (!(env.containsKey("java.naming.provider.url"))))
      throw new IllegalArgumentException(
          "forceInstance: Context environment mag niet null zijn en moet "
          + "minstens 1 'provider url' hebben.");
    try {
      Context initialContext = new InitialContext(env);
      if (contexts.size() > 0)
        contexts.set(0, initialContext);
      else
        contexts.add(initialContext);
    } catch (NamingException ne) {
      logger.error("Error in CTX lookup", ne);
    }
    return _instance;
  }

  public DataSource getDataSource(String jndiName)
      throws ServiceLocatorException {
    if (jndiName == null) {
      throw new IllegalArgumentException(
          "getDataSource: jndiName mag niet null zijn.");
    }
    DataSource datasource = (DataSource) lookup(jndiName);
    if (datasource == null) {
      logger.error("getDataSource: Kan geen datasource vinden met jndiName="
          + jndiName + " in geen enkele context.");
      throw new ServiceLocatorException(DoosError.OBJECT_NOT_FOUND,
                  DoosLayer.BUSINESS, "Kan geen datasource vinden met jndiName="
                  + jndiName + " in geen enkele context.");
    }

    return datasource;
  }

  public Object getEJB(String mappedName, Class<?> interfaceClassName,
                       String[] params) throws ServiceLocatorException {
    String canonicalName = interfaceClassName.getCanonicalName();

    return getEJB(mappedName, canonicalName, params);
  }

  public Object getEJB(String mappedName, String interfaceClassName,
                       String[] params) throws ServiceLocatorException {
    if (mappedName == null) {
      throw
        new IllegalArgumentException("getEJB: mappedName mag niet null zijn.");
    }

    if (interfaceClassName == null) {
      throw new IllegalArgumentException(
          "getEJB: interfaceClassName mag niet null zijn.");
    }

    String  jndi      = mappedName;
    logger.debug("getEJB: trying OpenEJB jndi:" + jndi);
    Object  ejbObject = lookup(jndi);
    if (ejbObject != null) {
      logger.debug("getEJB: ejb gevonden.");
    } else {
      logger.error("getEJB: Kan geen ejb vinden met mappedName=" + mappedName
          + " in any of the provided contexts.");
      throw new ServiceLocatorException(DoosError.OBJECT_NOT_FOUND,
                  DoosLayer.BUSINESS, "Kan geen ejb vinden met  mappedName="
                  + mappedName + " in geen enkele context.");
    }

    return ejbObject;
  }

  public Object getEJB(String jndiName) throws ServiceLocatorException {
    if (jndiName == null) {
      throw new IllegalArgumentException("getEJB: jndiName mag niet null "
                                         + "zijn.");
    }
    Object ejbObject = lookup(jndiName);
    if (ejbObject == null) {
      logger.error("getEJB: Kan geen ejb vinden met jndiName=" + jndiName
                   + " in geen enkele context.");
      throw new ServiceLocatorException(DoosError.OBJECT_NOT_FOUND,
                  DoosLayer.BUSINESS, "Kan geen ejb vinden met jndiName="
                  + jndiName + " in geen enkele context.");
    }

    return ejbObject;
  }

  private Object lookup(String jndiName) {
    Context context = null;
    for (int i = 0; i < contexts.size(); ++i) {
      context = (Context) contexts.get(i);
      try {
        Object object = context.lookup(jndiName);
        return object;
      } catch (NamingException e) {
      }
    }
    return null;
  }

  private void loadBackupContexts() {
    InputStream xmlStream = super.getClass().getClassLoader()
                                 .getResourceAsStream("locator-properties.xml");
    if (xmlStream != null) {
      logger.info("Toevoegennvan  backup context providers...");
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
            logger.warn(
                "loadBackupContexts: Kan geen context maken voor server " + i
                    + ", server wordt genegeerd.", e);
          }
          ++i;
        }
      } catch (InvalidPropertiesFormatException e) {
        logger.error("loadBackupContexts: Unable to parse to "
                     + "locator-properties.xml.", e);
      } catch (IOException e) {
        logger.error(
            "loadBackupContexts: Kan locator-properties.xml niet lezen.", e);
      }
    }
  }

  public static ServiceLocator addContext(Properties env) {
    if ((env == null) || (!(env.containsKey("java.naming.provider.url"))))
      throw new IllegalArgumentException(
          "addContext: Context environment mag niet null zijn en moet "
          + "minstens 1 'provider url' hebben.");
    try {
      Context context = new InitialContext(env);
      contexts.add(context);
    } catch (NamingException ne) {
      logger.error("Error in CTX lookup", ne);
    }
    return _instance;
  }
}
