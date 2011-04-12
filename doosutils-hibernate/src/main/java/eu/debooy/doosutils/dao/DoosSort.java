/**
 * Copyright 2009 Marco de Booij
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
package eu.debooy.doosutils.dao;

import eu.debooy.doosutils.Sort;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;


/**
 * @author Marco de Booij
 */
public class DoosSort {
  private List<Sort>  sorts = new ArrayList<Sort>();

  public void addSort(String property, String order) {
    sorts.add(new Sort(property, order));
  }

  public Criteria execute(Criteria criteria) {
    for (Sort sort : sorts)
      buildCriteria(criteria, sort.getProperty(), sort.getOrder());

    return criteria;
  }

  private void buildCriteria(Criteria criteria, String property, String order) {
    if (order.equals(Sort.ASC)) {
      criteria.addOrder(Order.asc(property));
    } else if (order.equals(Sort.DESC)) {
      criteria.addOrder(Order.desc(property));
    }
  }
}
