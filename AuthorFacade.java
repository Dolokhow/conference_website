/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import beans.Author;
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
public class AuthorFacade extends AbstractFacade<Author> {

    @PersistenceContext(unitName = "konferencija_scPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AuthorFacade() {
        super(Author.class);
    }
    
    public Author getAuthor(int idU) {
        Map<String, Object> parameter_map = new HashMap();
        parameter_map.put("idU", idU);
        return super.findSingleByCriteria("Author.findByIdU", parameter_map);
    }
    
}
