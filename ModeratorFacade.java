/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import beans.Administrator;
import beans.Moderator;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author djordjebozic
 */
@Stateless
public class ModeratorFacade extends AbstractFacade<Moderator> {

    @PersistenceContext(unitName = "konferencija_scPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ModeratorFacade() {
        super(Moderator.class);
    }
    
    public Moderator getModerator(int id) {
        Map<String, Object> parameter_map = new HashMap();
        parameter_map.put("id", id);
        return super.findSingleByCriteria("Moderator.findById", parameter_map);
    }
    
}
