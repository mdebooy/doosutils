/**
 * Copyright 2012 Marco de Booij
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
package eu.debooy.doosutils.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Before;


/**
 * @author Marco de Booij
 */
public class DoosJpaTest extends DoosTest {
  private EntityManager     em;
  private EntityTransaction et;

  public DoosJpaTest(String testName) {
    super(testName);
  }

  @Before
  public void setUp() {
    super.setUp();
    em  = Persistence.createEntityManagerFactory("test")
                     .createEntityManager();
    et  = em.getTransaction();
  }
}
