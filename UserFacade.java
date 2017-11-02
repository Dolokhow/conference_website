/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

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
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "konferencija_scPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
    public User getLoginInfo(String username, String password) {
        Map<String, Object> parameter_map = new HashMap();
        parameter_map.put("username", username);
        parameter_map.put("password", password);
        return super.findSingleByCriteria("User.findByUsernameAndPassword", parameter_map);
    }

    public boolean checkUsernameAvailability(String username) {
        Map<String, Object> parameter_map = new HashMap();
        parameter_map.put("username", username);
        List<User> users = super.findAllByCriteria("User.findByUsername", parameter_map);
        return users.isEmpty();
    }
}
