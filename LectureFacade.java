/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import beans.Lecture;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author djordjebozic
 */
@Stateless
public class LectureFacade extends AbstractFacade<Lecture> {

    @PersistenceContext(unitName = "konferencija_scPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LectureFacade() {
        super(Lecture.class);
    }
    
    public int nextId () {
        int next_id = 0;
        Query nq = em.createNativeQuery("SELECT MAX(id) FROM lecture");
        next_id = (Integer) nq.getSingleResult();
        
        return next_id + 1;
    }
    
}
