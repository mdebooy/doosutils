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

import eu.debooy.doosutils.domain.DoosFilter;
import eu.debooy.doosutils.domain.DoosSort;
import eu.debooy.doosutils.domain.Dto;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.IllegalArgumentException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;
import eu.debooy.doosutils.errorhandling.handler.interceptor.PersistenceExceptionHandlerInterceptor;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.ejb.EntityManagerImpl;
import org.slf4j.Logger;


/**
 * Data Access Object pattern.
 * 
 * @author Marco de Booij
 */
@Interceptors({PersistenceExceptionHandlerInterceptor.class})
public abstract class Dao<T extends Dto> {
  protected abstract  EntityManager getEntityManager();

  protected Class<T>  persistentClass;

  public Dao(Class<T> persistentClass) {
    this.persistentClass  = persistentClass;
  }

  /**
   * Converteer een List in een Set.
   * 
   * De Set is een TreeSet als de entities Comparable is. Anders een HashSet.
   * 
   * 
   * @param entities
   * @return een TreeSet of HashSet
   */
  public Collection<T> convertToCollection(List<T> entities) {
    if (entities == null || entities.size() == 0) {
      return null;
    }

    if (entities.get(0) instanceof Comparable<?>) {
      return new TreeSet<T>(entities);
    } else {
      return entities;
    }
  }

  public T create(T dto) throws DoosRuntimeException, DuplicateObjectException {
    Dto bestaat = getUniqueResult(dto);
    if (bestaat != null) {
        throw new DuplicateObjectException(DoosLayer.PERSISTENCE, dto,
                                           "bestaat reeds");
    }
    getHibernateSession().save(dto);
    getHibernateSession().flush();
    getHibernateSession().refresh(dto);

    return dto;
  }

  @SuppressWarnings("unchecked")
  public void delete(T dto) throws DoosRuntimeException {
    T merged = (T) getHibernateSession().merge(dto);
    getHibernateSession().delete(merged);
    getHibernateSession().flush();
  }

  @SuppressWarnings("unchecked")
  public Collection<T> getAll()
      throws DoosRuntimeException, ObjectNotFoundException {
    Criteria  criteria  = initCriteria();

    return convertToCollection(criteria.list());
  }

  @SuppressWarnings("unchecked")
  public Collection<T> getAll(DoosFilter filter)
      throws DoosRuntimeException, IllegalArgumentException,
             ObjectNotFoundException {
    Criteria  criteria  = filter.execute(initCriteria());

    return convertToCollection(criteria.list());
  }

  @SuppressWarnings("unchecked")
  public Collection<T> getAll(DoosFilter filter, DoosSort sort)
      throws DoosRuntimeException, IllegalArgumentException,
             ObjectNotFoundException {
    Criteria  criteria  = filter.execute(initCriteria());
    sort.execute(criteria);

    return convertToCollection(criteria.list());
  }

  @SuppressWarnings("unchecked")
  public Collection<T> getAll(DoosSort sort)
      throws DoosRuntimeException, ObjectNotFoundException {
    Criteria  criteria  = initCriteria();
    sort.execute(criteria);

    return convertToCollection(criteria.list());
  }

  public abstract String  getApplicationName();

  /**
   * 'Hulp' methode om de onderliggende Hibernate {@link Session} te krijgen.
   * 
   * @return de Hibernate {@link Session}
   */
  protected Session getHibernateSession() {
    if (getEntityManager().getDelegate() instanceof EntityManagerImpl) {
      return ((EntityManagerImpl) getEntityManager().getDelegate())
                                                    .getSession();
    }

    return (Session) getEntityManager().getDelegate();
  }

  public abstract Logger  getLogger();

  @SuppressWarnings("unchecked")
  public T getUniqueResult(DoosFilter filter)
      throws DoosRuntimeException, IllegalArgumentException,
             ObjectNotFoundException {
    Criteria  criteria  = filter.execute(initCriteria());

    return (T) criteria.uniqueResult();
  }

  public abstract T getUniqueResult(T dto);

  /**
   * Initialisering van een criteria gebaseerd op de generieke superclass.
   * 
   * @return een Hibernate {@link Criteria}
   */
  protected Criteria initCriteria() {
    return getHibernateSession().createCriteria(persistentClass);
  }

  @SuppressWarnings("unchecked")
  public T merge(T dto) throws DoosRuntimeException {
    T merged = (T) getHibernateSession().merge(dto);
    getHibernateSession().flush();
    getHibernateSession().refresh(merged);

    return merged;
  }

  @SuppressWarnings("unchecked")
  public T update(T dto) throws DoosRuntimeException, ObjectNotFoundException {
    T updated = (T) getHibernateSession().merge(dto);
    getHibernateSession().update(updated);
    getHibernateSession().flush();
    getHibernateSession().refresh(updated);

    return updated;
  }
}
