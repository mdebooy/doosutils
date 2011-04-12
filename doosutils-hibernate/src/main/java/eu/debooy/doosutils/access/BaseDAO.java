/**
 * Copyright 2010 Marco de Booij
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
package eu.debooy.doosutils.access;

import eu.debooy.doosutils.business.IProfiler;
import eu.debooy.doosutils.errorhandling.exception.IllegalArgumentException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.hibernate.Session;
import org.hibernate.ejb.EntityManagerImpl;
import org.hibernate.ejb.HibernateEntityManager;


/**
 * @author Marco de Booij
 */
public class BaseDAO implements IProfiler {
  @Override
  public String getApplicationName() {
    throw new IllegalArgumentException(DoosLayer.PERSISTENCE,
          "ApplicationName moet gezet worden in de implementerende DAO bean.");
  }

  @Override
  public Log getLog() {
    throw new IllegalArgumentException(DoosLayer.PERSISTENCE,
          "Een logger moet gezet worden in de implementerende DAO bean.");
  }

  public Object create(EntityManager em, Object dto) {
    em.persist(dto);
    em.flush();
    Object  inserted  = em.merge(dto);
    em.refresh(inserted);
    return inserted;
  }

  public Object update(EntityManager em, Object dto) {
    Object  updated = em.merge(dto);
    em.flush();
    em.refresh(updated);
    return updated;
  }

  public void delete(EntityManager em, Object dto) {
    em.remove(em.merge(dto));
    em.flush();
  }

  public Session getSession(EntityManager em) {
    if (em.getDelegate() instanceof HibernateEntityManager) {
      return ((HibernateEntityManager) em.getDelegate()).getSession();
    }
    if (em.getDelegate() instanceof EntityManagerImpl) {
      return ((EntityManagerImpl) em.getDelegate()).getSession();
    }
    return ((Session)em.getDelegate());
  }
}
