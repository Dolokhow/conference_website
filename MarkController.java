package controllers;

import beans.Agenda;
import beans.Mark;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import sessionBeans.MarkFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import beans.Lecture;
import beans.MarkPK;
import beans.User;
import java.util.Collection;

@Named("markController")
@SessionScoped
public class MarkController implements Serializable {

    @EJB
    private sessionBeans.MarkFacade ejbFacade;
    private List<Mark> items = null;
    private Mark selected;

    public MarkController() {
    }

    public Mark getSelected() {
        return selected;
    }

    public void setSelected(Mark selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getMarkPK().setIdS(selected.getAgenda().getAgendaPK().getIdS());
        selected.getMarkPK().setIdC(selected.getAgenda().getAgendaPK().getIdC());
        selected.getMarkPK().setIdU(selected.getAgenda().getAgendaPK().getIdU());
        selected.getMarkPK().setIdL(selected.getLecture().getLecturePK().getId());
    }

    protected void initializeEmbeddableKey() {
        selected.setMarkPK(new beans.MarkPK());
    }

    private MarkFacade getFacade() {
        return ejbFacade;
    }

    public Mark prepareCreate() {
        selected = new Mark();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MarkCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MarkUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MarkDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Mark> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Mark getMark(beans.MarkPK id) {
        return getFacade().find(id);
    }

    public List<Mark> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Mark> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Mark.class)
    public static class MarkControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MarkController controller = (MarkController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "markController");
            return controller.getMark(getKey(value));
        }

        beans.MarkPK getKey(String value) {
            beans.MarkPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new beans.MarkPK();
            key.setIdL(Integer.parseInt(values[0]));
            key.setIdS(Integer.parseInt(values[1]));
            key.setIdC(Integer.parseInt(values[2]));
            key.setIdU(Integer.parseInt(values[3]));
            return key;
        }

        String getStringKey(beans.MarkPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdL());
            sb.append(SEPARATOR);
            sb.append(value.getIdS());
            sb.append(SEPARATOR);
            sb.append(value.getIdC());
            sb.append(SEPARATOR);
            sb.append(value.getIdU());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Mark) {
                Mark o = (Mark) object;
                return getStringKey(o.getMarkPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Mark.class.getName()});
                return null;
            }
        }

    }

    private int value;
    private String message;

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String rate(Lecture l, User u, AgendaController ac) {
        prepareCreate();
        selected.setValue(value);
        selected.setLecture(l);

        Collection<Agenda> agendas = (ac.getNewItems());
        for (Agenda a : agendas) {
            if (a.getUser().equals(u) && a.getSession().equals(l.getSession())) {
                selected.setAgenda(a);
                break;
            }
            
        }
        selected.setMarkPK(new MarkPK(l.getLecturePK().getId(), 
                l.getLecturePK().getIdS(), l.getLecturePK().getIdC(), u.getId()));
        

        message = "You have rated " + l.getName();
        persist(PersistAction.CREATE, message);
        l.getMarkCollection().add(selected);
        
        return "/rating.xhtml?faces-redirect=true";
    }

    public boolean check(User u, Lecture l) {
        boolean notRated = true;
        getItems();
        for (Mark m: items) {
            if (m.getMarkPK().getIdU() == u.getId() && m.getMarkPK().getIdL() == l.getLecturePK().getId()
                    && m.getMarkPK().getIdS() == l.getLecturePK().getIdS())
                notRated = false;
        }
        return notRated;
    }
}
