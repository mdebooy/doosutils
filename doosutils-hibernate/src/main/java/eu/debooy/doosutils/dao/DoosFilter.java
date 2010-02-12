/**
 * Copyright 2009 Marco de Booy
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
package eu.debooy.doosutils.dao;

import eu.debooy.doosutils.Filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;


/**
 * @author Marco de Booy
 */
public class DoosFilter implements CriteriaCommand {
  List<Filter>  filters = new ArrayList<Filter>();

  public void addFilter(String property, Object value) {
    filters.add(new Filter(property, (String) value));
  }

  public Criteria execute(Criteria criteria) {
    for (Filter filter : filters)
      buildCriteria(criteria, filter.getProperty(), filter.getValue());

    return criteria;
  }

  private void buildCriteria(Criteria criteria, String property, Object value) {
    if (value != null) {
      if (value instanceof String) {
        criteria.add(Restrictions.like(property, "%" + value + "%")
                                 .ignoreCase());
      } else if (value instanceof Date) {
        criteria.add(Restrictions.eq(property, value));
      } else if (value instanceof Number) {
        criteria.add(Restrictions.eq(property, value));
      } else {
        criteria.add(Restrictions.like(property, "%" + value.toString() + "%")
                                 .ignoreCase());
      }
    }
  }
}
