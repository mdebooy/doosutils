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
package eu.debooy.doosutils.conversie;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * @author Marco de Booij
 */
public final class ByteArray {
  private ByteArray() {}

  public static Object byteArrayToObject(byte[] byteArray) throws IOException {
    ByteArrayInputStream  bais    = new ByteArrayInputStream(byteArray);
    ObjectInputStream     ois     = null;
    Object                object  = null;
    try {
      ois     = new ObjectInputStream(bais);
      object  = ois.readObject();
    } catch (ClassNotFoundException e) {
      // Hoogst onwaarschijnlijk. Maak er dus maar een IO Exception van.
      throw new IOException(e);
    }

    return object;
  }

  /**
   * Zet een object om in een byte array.
   * 
   * @param object
   * @return
   */
  public static byte[] toByteArray(Object object) throws IOException {
    ByteArrayOutputStream baos      = new ByteArrayOutputStream();
    ObjectOutputStream    oos       = new ObjectOutputStream(baos);
    oos.writeObject(object);
    byte[]                byteArray = baos.toByteArray();

    return byteArray;
  }
}
