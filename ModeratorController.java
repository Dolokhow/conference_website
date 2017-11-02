package controllers;

import beans.Conference;
import beans.Moderator;
import beans.User;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import sessionBeans.ModeratorFacade;

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

@Named("moderatorController")
@SessionScoped
public class ModeratorController implements Serializable {

    @EJB
    private sessionBeans.ModeratorFacade ejbFacade;
    private List<Moderator> items = null;
    private Moderator selected;

    public ModeratorController() {
    }

    public Moderator getSelected() {
        return selected;
    }

    public void setSelected(Moderator selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ModeratorFacade getFacade() {
        return ejbFacade;
    }

    public Moderator prepareCreate() {
        selected = new Moderator();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ModeratorCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ModeratorUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ModeratorDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Moderator> getItems() {
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

    public Moderator getModerator(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Moderator> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Moderator> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Moderator.class)
    public static class ModeratorControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ModeratorController controller = (ModeratorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "moderatorController");
            return controller.getModerator(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Moderator) {
                Moderator o = (Moderator) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Moderator.class.getName()});
                return null;
            }
        }

    }
    
    public void addModerator(User u, Conference c) {
        prepareCreate();
        selected.setUser(u);
        selected.setId(u.getId());
        String message = "Success!";
        persist(PersistAction.CREATE, message);
        selected.getConferenceCollection().add(c);
        persist(PersistAction.CREATE, message);
    }
    
    public boolean isModerator(User u){
        Moderator m = getFacade().getModerator(u.getId());
        if (m == null)
            return false;
        else return true;
    }
    
    public Moderator getModerator(int u) {
        return getFacade().getModerator(u);
    }

}
