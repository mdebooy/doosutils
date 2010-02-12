/**
 * Copyright 2009 Marco de Booij
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
package eu.debooy.doosutils.components;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.log4j.Logger;


/**
 * @author Marco de Booij
 */
public class DoosPhaseListener implements PhaseListener {
  private static final  long  serialVersionUID  = 1L;

  private static transient
    Logger  logger  = Logger.getLogger(DoosPhaseListener.class);

  @Override
  public void afterPhase(PhaseEvent phaseEvent) {
    logger.debug("After: " + phaseEvent.getPhaseId());
  }

  @Override
  public void beforePhase(PhaseEvent phaseEvent) {
    Map<String, ?>  reqParams;
    if (logger.isTraceEnabled()) {
      FacesContext  context = phaseEvent.getFacesContext();
      PhaseId       phase   = phaseEvent.getPhaseId();
      String        viewId  = "none";
      if (context.getViewRoot() != null) {
        viewId  = context.getViewRoot().getViewId();
      }
      logger.debug("Before: " + phase + " " + viewId);
      if (PhaseId.RESTORE_VIEW.equals(phase)) {
        reqParams = context.getExternalContext().getRequestParameterMap();
        for (String key : reqParams.keySet())
          logger.debug("   " + key + ": " + ((String)reqParams.get(key)));
      }
    }
  }

  @Override
  public PhaseId getPhaseId() {
    // TODO Auto-generated method stub
    return null;
  }

}
