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


/**
 * @author Marco de Booij
 */
public class ConfirmationBean extends JsfBean {
  private static final long serialVersionUID  = 1L;

  public  static final String BEAN_ALIAS  = "confirmation";

  private boolean show;
  private String  header;
  private String  body;
  private String  confirmReturnAction;
  private String  rejectReturnAction;

  public ConfirmationBean() {
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public boolean isShow() {
    return show;
  }

  public void setShow(boolean show) {
    this.show = show;
  }

  public void setConfirmReturnAction(String confirmReturnAction) {
    this.confirmReturnAction  = confirmReturnAction;
  }

  public void setRejectReturnAction(String rejectReturnAction) {
    this.rejectReturnAction   = rejectReturnAction;
  }

  public void showPopup() {
    show  = true;
  }

  public void closePopup() {
    show  = false;
  }

  public void confirm() {
    invokeAction(confirmReturnAction);
    show  = false;
  }

  public void reject() {
    invokeAction(rejectReturnAction);
    show  = false;
  }
}
