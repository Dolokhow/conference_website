/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import beans.Gallery;
import beans.User;
import java.util.HashMap;
import java.util.List;
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
public class GalleryFacade extends AbstractFacade<Gallery> {

    @PersistenceContext(unitName = "konferencija_scPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GalleryFacade() {
        super(Gallery.class);
    }
    
    public List<Gallery> getImages(int idS) {
        Map<String, Object> parameter_map = new HashMap();
        parameter_map.put("idS", idS);
        return super.findAllByCriteria("Gallery.findByIdS", parameter_map);
    }
    
    public int nextId () {
        int next_id = 0;
        Query nq = em.createNativeQuery("SELECT MAX(id) FROM gallery");
        next_id = (Integer) nq.getSingleResult();
        
        return next_id + 1;
    }
}
