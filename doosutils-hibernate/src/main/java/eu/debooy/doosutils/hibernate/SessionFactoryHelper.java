/**
 * Copyright 2009 Marco de Booy
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
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
package eu.debooy.doosutils.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * @author Marco de Booy
 * @version $Revision$
 */
public class SessionFactoryHelper {
  private static final  SessionFactory  sessionFactory;
     
  static {
    try {           
      /*
       * Build a SessionFactory object from session-factory configuration
       * defined in the hibernate.cfg.xml file. In this file we register
       * the JDBC connection information, connection pool, the hibernate
       * dialect that we used and the mapping to our hbm.xml file for each
       * POJO (Plain Old Java Object).
       */
       sessionFactory = new Configuration().configure().buildSessionFactory();
    } catch (Throwable e) {
      System.err.println("Error in creating SessionFactory object: "
                         + e.getMessage());
      throw new ExceptionInInitializerError(e);
    }
  }
       
  /*
   * A static method for other application to get SessionFactory object
   * initialized in this helper class.
   */
  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }
}
