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
package eu.debooy.doosutils.components.bean;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;


/**
 * @author Marco de Booij
 */
public class ListFromCollection implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  private Map<Collection<?>, List<?>> map;
  private int                         size;

  private static  int defaultSize = 50;

  public ListFromCollection() {
    map   = new MakeList();
    size  = defaultSize;
  }

  public Map<Collection<?>, List<?>> getList() {
    return map;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  private static class ListImpl extends AbstractList<Object> {
    private final Collection<?> collection;
    private final int           bufferSize;
    private final int           cSize;
    private int                 offset;
    @SuppressWarnings("rawtypes")
    private List                buffer;

    @SuppressWarnings("rawtypes")
	  public ListImpl(Collection<?> collection, int size) {
      this.collection = collection;
      cSize           = collection.size();
      if (size == 0) {
        bufferSize =  cSize;
      }  else {
        bufferSize = Math.min(size, cSize);
      }

      buffer = new ArrayList(bufferSize);
      offset = -1;
    }

    @Override
    public int size() {
      return cSize;
    }

    @Override
    public Object get(int index) {
      if ((index < 0) || (index >= cSize)) {
        throw new IndexOutOfBoundsException();
      }
      int gOffset = index / bufferSize * bufferSize;
      if (gOffset != this.offset) {
        loadBuffer(gOffset);
        offset = gOffset;
      }

      return buffer.get(index - offset);
    }

    @SuppressWarnings("unchecked")
    private void loadBuffer(int offset) {
      Iterator<?> iter = collection.iterator();
      int i = 0;

      while (i < offset) {
        assert (iter.hasNext());
        iter.next();
        ++i;
      }

      buffer.clear();

      int count = 0;
      while ((count < bufferSize) && (i < cSize)) {
        assert (iter.hasNext());
        buffer.add(iter.next());
        i++;
        count++;
      }
    }
  }

  private class MakeList extends AbstractMap<Collection<?>, List<?>> {
    public List<?> get(Object obj) {
      if (!(obj instanceof Collection)) {
        return null;
      }

      if ((obj instanceof List) && (obj instanceof RandomAccess)) {
        return ((List<?>) obj);
      }

      Collection<?> collection = (Collection<?>) obj;

      if (collection.isEmpty()) {
        return Collections.EMPTY_LIST;
      }

      return new ListFromCollection.ListImpl(collection,
                                             ListFromCollection.this.getSize());
    }

    public Set<Map.Entry<Collection<?>, List<?>>> entrySet() {
      return Collections.emptySet();
    }
  }
}
