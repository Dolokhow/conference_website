/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import beans.Impression;
import beans.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author djordjebozic
 */
@Stateless
public class ImpressionFacade extends AbstractFacade<Impression> {

    @PersistenceContext(unitName = "konferencija_scPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ImpressionFacade() {
        super(Impression.class);
    }
    
    public List<Impression> getLikes(int idS, int idU) {
        Map<String, Object> parameter_map = new HashMap();
        parameter_map.put("idS", idS);
        parameter_map.put("idU", idU);
        return super.findAllByCriteria("Impression.countLikes", parameter_map);
    }
    
}
