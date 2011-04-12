/**
 * Copyright 2009 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.0 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/7330l5
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doosutils.errorhandling.exception.base;

import java.io.Serializable;


/**
 * @author Marco de Booij
 */
public class DoosError implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String code;
  private final String description;

  public static final DoosError DUPLICATE_OBJECT      =
    new DoosError("DUPLICATE_OBJECT");
  public static final DoosError FILE_NOT_FOUND        =
    new DoosError("FILE_NOT_FOUND");
  public static final DoosError ILLEGAL_ARGUMENT      =
    new DoosError("ILLEGAL_ARGUMENT");
  public static final DoosError MULTIPLE_OBJECT_FOUND =
    new DoosError("MULTIPLE_OBJECT_FOUND");
  public static final DoosError OBJECT_NOT_FOUND      =
    new DoosError("OBJECT_NOT_FOUND");
  public static final DoosError RUNTIME_EXCEPTION     =
    new DoosError("RUNTIME_EXCEPTION");

  protected DoosError() {
    code        = "DEFAULT";
    description = "DEFAULT_CONSTRUCTOR";
  }

  protected DoosError(String code, String description) {
    this.code         = code;
    this.description  = description;
  }

  protected DoosError(String code) {
    this.code   = code;
    description = code;
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public final String toString() {
    return (new StringBuilder()).append("DoosError ( ")
                                .append(super.toString())
                                .append("    ").append("code = ")
                                .append(code).append("    ")
                                .append("description = ").append(description)
                                .append(" )").toString();
  }
}
