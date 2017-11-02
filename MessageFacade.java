/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import beans.Message;
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
public class MessageFacade extends AbstractFacade<Message> {

    @PersistenceContext(unitName = "konferencija_scPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MessageFacade() {
        super(Message.class);
    }

    public List<Message> getMessages(int sender_id, int recipient_id) {
        Map<String, Object> parameter_map = new HashMap();
        List<Message> ret;

        parameter_map.put("idSender", sender_id);
        parameter_map.put("idRecipient", recipient_id);
        ret = super.findAllByCriteria("Message.findByIdSenderAndIdRecipient", parameter_map);

        return ret;
    }
    
    public int nextId () {
        int next_id = 0;
        Query nq = em.createNativeQuery("SELECT MAX(id) FROM message");
        next_id = (Integer) nq.getSingleResult();
        
        return next_id + 1;
    }

}
