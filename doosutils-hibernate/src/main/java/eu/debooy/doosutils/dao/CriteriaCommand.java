package eu.debooy.doosutils.dao;

import org.hibernate.Criteria;


/**
 * @author Marco de Booy
 * @version $Revision$
 */
public interface CriteriaCommand {
  public Criteria execute(Criteria criteria);
}
