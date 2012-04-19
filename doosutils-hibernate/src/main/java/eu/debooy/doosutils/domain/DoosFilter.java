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
package eu.debooy.doosutils.domain;

import eu.debooy.doosutils.Filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;


/**
 * @author Marco de Booij
 */
public class DoosFilter implements CriteriaCommand {
  private List<Filter>  filters = new ArrayList<Filter>();

  /**
   * Voeg een conditie toe aan de filter.
   * 
   * @param element
   * @param waarde
   */
  public void addFilter(String element, Object waarde) {
    filters.add(new Filter(element, waarde));
  }

  /**
   * Build the criteria.
   * 
   * @param criteria
   * @param property
   * @param waarde
   */
  private void buildCriteria(Criteria criteria, String element,
                             Object waarde) {
    if (null == waarde) {
      criteria.add(Restrictions.isNull(element));
    } else {
      if (waarde instanceof String) {
        if (((String) waarde).contains("%")) {
          criteria.add(Restrictions.like(element, waarde));
        } else {
          criteria.add(Restrictions.eq(element, waarde));
        }
      } else if (waarde instanceof Date) {
        criteria.add(Restrictions.eq(element, waarde));
      } else if (waarde instanceof Number) {
        criteria.add(Restrictions.eq(element, waarde));
      } else {
        if (waarde.toString().contains("%")) {
          criteria.add(Restrictions.like(element, waarde.toString()));
        } else {
          criteria.add(Restrictions.eq(element, waarde.toString()));
        }
      }
    }
  }

  /**
   * Return the Criteria.
   */
  @Override
  public Criteria execute(Criteria criteria) {
    for (Filter filter : filters) {
      buildCriteria(criteria, filter.getElement(), filter.getWaarde());
    }

    return criteria;
  }

  /**
   * Geef alle filter elementen.
   * 
   * @return
   */
  public final List<Filter> getAll() {
    return filters;
  }

  /**
   * Maak een String van alle elementen van de filter.
   * 
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuffer  sb  = new StringBuffer();

    for (Filter filter : filters) {
      Object  waarde  = filter.getWaarde();
      if (waarde instanceof String) {
        if (((String) waarde).contains("%")) {
          sb.append(", " + filter.getElement() + " like " + waarde);
        } else {
          sb.append(", " + filter.getElement() + " = " + waarde);
        }
      } else if (waarde instanceof Date) {
        sb.append(", " + filter.getElement() + " = " + waarde);
      } else if (waarde instanceof Number) {
        sb.append(", " + filter.getElement() + " = " + waarde);
      } else {
        if (waarde.toString().contains("%")) {
          sb.append(", " + filter.getElement() + " like " + waarde);
        } else {
          sb.append(", " + filter.getElement() + " = " + waarde);
        }
      }
    }

    return sb.toString().replaceFirst(", ", "");
  }
}
