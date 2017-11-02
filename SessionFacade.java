/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import beans.Session;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author djordjebozic
 */
@Stateless
public class SessionFacade extends AbstractFacade<Session> {

    @PersistenceContext(unitName = "konferencija_scPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SessionFacade() {
        super(Session.class);
    }
    
    public Session getSession(int idS) {
        Map<String, Object> parameter_map = new HashMap();
        parameter_map.put("id", idS);
        return super.findSingleByCriteria("Session.findById", parameter_map);
    }
    
    public int getConferenceId (int idS) {
        Map<String, Object> parameter_map = new HashMap();
        parameter_map.put("id", idS);
        return super.findSingleByCriteria("Session.findById", parameter_map).getSessionPK().getIdC();
    }
    
    public int nextId () {
        int next_id = 0;
        Query nq = em.createNativeQuery("SELECT MAX(id) FROM session");
        next_id = (Integer) nq.getSingleResult();
        
        return next_id + 1;
    }
    
}
