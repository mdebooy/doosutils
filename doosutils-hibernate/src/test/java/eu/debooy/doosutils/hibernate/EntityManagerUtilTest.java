/**
 * Copyright 2010 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.0 or - as soon they will be approved by
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

import eu.debooy.doosutils.test.DoosTest;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class EntityManagerUtilTest extends DoosTest {
  private EntityManagerFactory  factory;

  public EntityManagerUtilTest(String testName) {
    super(testName);
  }

  /* (non-Javadoc)
   * @see eu.debooy.doosutils.test.DoosTest#setUp()
   */
  @Override
  public void setUp() {
    super.setUp();
    factory = EntityManagerUtil.getEntityManagerFactory("doos-utils", "doosutils.hibernate.properties");
  }

  /* (non-Javadoc)
   * @see eu.debooy.doosutils.test.DoosTest#tearDown()
   */
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    factory.close();
  }

  @Test
  public void testInitialisePlus() {
  }
}
