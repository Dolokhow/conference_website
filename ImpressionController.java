package controllers;

import beans.Impression;
import beans.ImpressionPK;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import sessionBeans.ImpressionFacade;

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
import javax.faces.event.ActionEvent;
import beans.User;
import beans.Session;
import beans.Agenda;
import beans.Mark;
import java.util.Collection;

@Named("impressionController")
@SessionScoped
public class ImpressionController implements Serializable {

    @EJB
    private sessionBeans.ImpressionFacade ejbFacade;
    private List<Impression> items = null;
    private Impression selected;

    public ImpressionController() {
    }

    public Impression getSelected() {
        return selected;
    }

    public void setSelected(Impression selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getImpressionPK().setIdC(selected.getAgenda().getAgendaPK().getIdC());
        selected.getImpressionPK().setIdU(selected.getAgenda().getAgendaPK().getIdU());
        selected.getImpressionPK().setIdS(selected.getAgenda().getAgendaPK().getIdS());
    }

    protected void initializeEmbeddableKey() {
        selected.setImpressionPK(new beans.ImpressionPK());
    }

    private ImpressionFacade getFacade() {
        return ejbFacade;
    }

    public Impression prepareCreate() {
        selected = new Impression();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ImpressionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ImpressionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ImpressionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Impression> getItems() {
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

    public Impression getImpression(beans.ImpressionPK id) {
        return getFacade().find(id);
    }

    public List<Impression> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Impression> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Impression.class)
    public static class ImpressionControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ImpressionController controller = (ImpressionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "impressionController");
            return controller.getImpression(getKey(value));
        }

        beans.ImpressionPK getKey(String value) {
            beans.ImpressionPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new beans.ImpressionPK();
            key.setIdS(Integer.parseInt(values[0]));
            key.setIdC(Integer.parseInt(values[1]));
            key.setIdU(Integer.parseInt(values[2]));
            return key;
        }

        String getStringKey(beans.ImpressionPK value) {
            StringBuilder sb = new StringBuilder();
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
            if (object instanceof Impression) {
                Impression o = (Impression) object;
                return getStringKey(o.getImpressionPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Impression.class.getName()});
                return null;
            }
        }

    }

    // ADDED PROPERTIES! --------------------------------------------------------------------------------------------------
    private String impression;
    private boolean like;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImpression() {
        return impression;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public String review(User u, Session s, AgendaController ac) {
        prepareCreate();
        if (like == true) {
            selected.setLke(1);
        } else {
            selected.setLke(0);
        }

        selected.setCmment(impression);
        selected.setImpressionPK(new ImpressionPK(s.getSessionPK().getId(), s.getSessionPK().getIdC(), u.getId()));
        Collection<Agenda> agendas = ac.getNewItems();
        for (Agenda a : agendas) {
            if (a.getAgendaPK().getIdU() == u.getId() && a.getAgendaPK().getIdS() == s.getSessionPK().getId()
                    && a.getAgendaPK().getIdC() == s.getSessionPK().getIdC()) {
                selected.setAgenda(a);
                break;
            }
        }

        message = "Impression successfully submitted!";
        persist(PersistAction.CREATE, message);

        return "myAgenda.xhtml?faces-redirect=true";
    }

    public void like(ActionEvent e) {
        like = true;
    }

    public boolean check(User u, Session s) {
        boolean notRated = true;
        items = null;
        getItems();
        for (Impression i : items) {
            if (i.getImpressionPK().getIdU() == u.getId() && i.getImpressionPK().getIdS() == s.getSessionPK().getId()
                    && i.getImpressionPK().getIdC() == s.getSessionPK().getIdC()) {
                notRated = false;
                break;
            }
        }
        return notRated;
    }
    
    public int countLikes(Session s, User u) {
        items = getFacade().getLikes(s.getSessionPK().getId(), u.getId());
        return items.size();
    }

}
