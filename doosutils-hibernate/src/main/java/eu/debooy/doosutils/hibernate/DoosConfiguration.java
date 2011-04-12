/**
 * Copyright 2006 Marco de Booy
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;


/**
 * @author Marco de Booy
 * @version $Revision: 1.1 $
 */
public class  DoosConfiguration extends Configuration {
  private static final long serialVersionUID  = 1L;

  private static  Log   log  = LogFactory.getLog(DoosConfiguration.class);

  protected InputStream getConfigurationInputStream(String resource)
      throws HibernateException {
    
    FileInputStream fileIn  = null ;
    
    try {
     fileIn = new FileInputStream(resource);
    } catch (FileNotFoundException e) {
      log.error("no such file found " + resource);    
    }
    return fileIn;    
  }
}
