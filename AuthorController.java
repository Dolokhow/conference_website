package controllers;

import beans.Author;
import beans.Lecture;
import beans.User;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import sessionBeans.AuthorFacade;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@Named("authorController")
@SessionScoped
public class AuthorController implements Serializable {

    @EJB
    private sessionBeans.AuthorFacade ejbFacade;
    private List<Author> items = null;
    private Author selected;

    public AuthorController() {
    }

    public Author getSelected() {
        return selected;
    }

    public void setSelected(Author selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AuthorFacade getFacade() {
        return ejbFacade;
    }

    public Author prepareCreate() {
        selected = new Author();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AuthorCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AuthorUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AuthorDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Author> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    protected void persist(PersistAction persistAction, String successMessage) {
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

    public Author getAuthor(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Author> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Author> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Author.class)
    public static class AuthorControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AuthorController controller = (AuthorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "authorController");
            return controller.getAuthor(getKey(value));
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
            if (object instanceof Author) {
                Author o = (Author) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Author.class.getName()});
                return null;
            }
        }

    }

    // ADDED PROPERTIES! --------------------------------------------------------------------------------------------------
   
    private String surname;
    private String id;

    private String message;

    

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAuthor(User u, Lecture l) {
        boolean check = false;
        Author a = ejbFacade.getAuthor(u.getId());
        if (a != null) {
            Collection<Lecture> lectures = a.getLectureCollection();

            for (Lecture cur : lectures) {
                if (cur.getLecturePK().getId() == l.getLecturePK().getId()) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }
    
   

    
    /*
    public String addAuthor(Lecture l) {
        if (name.length() < 40 && surname.length() < 40) {
            prepareCreate();
            if (id != null && !id.isEmpty()) {
                User u = uc.getUser(Integer.parseInt(id));
                if (u == null) {
                    message = "User with specified id does not exist!";
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid field!", this.message);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    return "lectureModify";
                }
                if (!name.equals(u.getName()) || !surname.equals(u.getSurname())) {
                    message = "User with specified id does not have specified name!";
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid field!", this.message);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    return "lectureModify";
                    
                }
                selected.setIdU(u);
            }
            selected.setName(name);
            selected.setSurname(surname);
            persist(PersistAction.CREATE, message);
            selected.getLectureCollection().add(l);
            l.getAuthorCollection().add(selected);
            persist(PersistAction.CREATE, message);

            return "/lectureModify.xhtml?faces-redirect=true";

        } else {
            message = "Name too long!";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid field!", this.message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "lectureModify";
        }

    }

*/

}
