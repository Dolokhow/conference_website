/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import beans.Conference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author djordjebozic
 */
@Stateless
public class ConferenceFacade extends AbstractFacade<Conference> {

    @PersistenceContext(unitName = "konferencija_scPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConferenceFacade() {
        super(Conference.class);
    }

    public List<Conference> getByName(String title) {
        Query nq = getEntityManager().createNamedQuery("Conference.findByPartialTitle");
        nq.setParameter("title", "%" + title + "%");
        return nq.getResultList();
    }

    public List<Conference> getByArea(String area) {
        Query nq = getEntityManager().createNamedQuery("Conference.findByPartialScientificArea");
        nq.setParameter("scientificArea", "%" + area + "%");
        return nq.getResultList();
    }

    public List<Conference> search(String title, String scientificArea, Date dateFrom, Date dateTo) {
        Map<String, Object> parameter_map = new HashMap();
        int call = 0;
        if (title != null && !title.isEmpty()) {
            parameter_map.put("title", title);
            call = 1;
        }
        if (scientificArea != null && !scientificArea.isEmpty()) {
            parameter_map.put("scientificArea", scientificArea);

            if (call == 0) {
                call = 2;
            } else {
                call = 3;
            }
        }
        if (dateFrom != null) {
            parameter_map.put("startDate", dateFrom);
        } else {
            try {
                parameter_map.put("startDate", new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01"));
            } catch (ParseException ex) {
                Logger.getLogger(ConferenceFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, +0);
            Date date = cal.getTime();
            parameter_map.put("startDate", date);
             */
        }
        if (dateTo != null) {
            parameter_map.put("endDate", dateTo);
        } else {
            try {
                parameter_map.put("endDate", new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-01"));
            } catch (ParseException ex) {
                Logger.getLogger(ConferenceFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        switch (call) {
            case 1:
                return super.findAllByCriteria("Conference.searchTitleDate", parameter_map);
            case 2:
                return super.findAllByCriteria("Conference.searchAreaDate", parameter_map);
            case 3:
                return super.findAllByCriteria("Conference.searchTitleAreaDate", parameter_map);
            default:
                return super.findAllByCriteria("Conference.searchDate", parameter_map);
        }

    }

}
