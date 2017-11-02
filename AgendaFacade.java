/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import beans.Agenda;
import beans.Gallery;
import beans.Session;
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
public class AgendaFacade extends AbstractFacade<Agenda> {

    @PersistenceContext(unitName = "konferencija_scPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgendaFacade() {
        super(Agenda.class);
    }
    
    
    public Agenda getAgenda(int idS, int idU) {
        Map<String, Object> parameter_map = new HashMap();
        parameter_map.put("idS", idS);
        parameter_map.put("idU", idU);
        return super.findSingleByCriteria("Agenda.findByIdSAndIdU", parameter_map);
    }

    
}
