package eu.debooy.doosutils;
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


import java.io.Serializable;


/**
 * @author Marco de Booy
 */
public class Sort implements Serializable, Comparable<Sort> {
  private static final  long serialVersionUID = 1L;

  public final static String  ASC   = "asc";
  public final static String  DESC  = "desc";

  private final String  property;
  private final String  order;

  public Sort(String property, String order) {
    this.property = property;
    this.order    = order;
  }

  public String getProperty() {
    return property;
  }

  public String getOrder() {
    return order;
  }

  public int compareTo(Sort obj) {
    return (property+"|"+order).compareTo(obj.property+"|"+obj.order);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;

    final Sort  other = (Sort) obj;
    if (order != other.order)
      return false;
    if (property != other.property)
      return false;

    return true;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int PRIME     = 31;
    int       result    = super.hashCode();

    int       order     =
      null == this.getOrder() ? 0 : this.getOrder().hashCode();
    int       property  =
      null == this.getProperty() ? 0 : this.getProperty().hashCode();
    result  = PRIME * result + order;
    result  = PRIME * result + property;

    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuffer  result  = new StringBuffer();

    result.append("property: " + property);
    result.append(" - order: " + order);

    return result.toString();
  }
}
