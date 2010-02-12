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
public class Filter implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String  property;
  private final Object  value;

  public Filter(String property, Object value) {
    this.property = property;
    this.value    = value;
  }

  public String getProperty() {
    return property;
  }

  public Object getValue() {
    return value;
  }

  public int compareTo(Filter obj) {
    return (property+"|"+value).compareTo(obj.property+"|"+obj.value);
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

    final Filter  other = (Filter) obj;
    if (value != other.value)
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

    int       property  =
      null == this.getProperty() ? 0 : this.getProperty().hashCode();
    int       value     =
      null == this.getValue() ? 0 : this.getValue().hashCode();
    result  = PRIME * result + property;
    result  = PRIME * result + value;

    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuffer  result  = new StringBuffer();

    result.append("property: " + property);
    result.append(" - value: " + value);

    return result.toString();
  }
}

