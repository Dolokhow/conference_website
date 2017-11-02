/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import beans.Administrator;
import beans.User;
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
public class AdministratorFacade extends AbstractFacade<Administrator> {

    @PersistenceContext(unitName = "konferencija_scPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdministratorFacade() {
        super(Administrator.class);
    }
    
    public Administrator getAdmin(int id) {
        Map<String, Object> parameter_map = new HashMap();
        parameter_map.put("id", id);
        
        return super.findSingleByCriteria("Administrator.findById", parameter_map);
    }
    
    public Administrator getAdmin() {
        return (super.findAll()).get(0);
    }
    
}
