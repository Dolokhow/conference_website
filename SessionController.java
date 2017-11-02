package controllers;

import beans.Conference;
import beans.Session;
import beans.SessionPK;
import beans.User;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import sessionBeans.SessionFacade;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named("sessionController")
@SessionScoped
public class SessionController implements Serializable {

    @EJB
    private sessionBeans.SessionFacade ejbFacade;
    private List<Session> items = null;
    private Session selected;

    public SessionController() {
    }

    public Session getSelected() {
        return selected;
    }

    public void setSelected(Session selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getSessionPK().setIdC(selected.getConference().getId());
    }

    protected void initializeEmbeddableKey() {
        selected.setSessionPK(new beans.SessionPK());
    }

    private SessionFacade getFacade() {
        return ejbFacade;
    }

    public Session prepareCreate() {
        selected = new Session();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SessionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SessionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SessionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Session> getItems() {
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

    public Session getSession(beans.SessionPK id) {
        return getFacade().find(id);
    }

    public List<Session> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Session> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Session.class)
    public static class SessionControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SessionController controller = (SessionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "sessionController");
            return controller.getSession(getKey(value));
        }

        beans.SessionPK getKey(String value) {
            beans.SessionPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new beans.SessionPK();
            key.setId(Integer.parseInt(values[0]));
            key.setIdC(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(beans.SessionPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getId());
            sb.append(SEPARATOR);
            sb.append(value.getIdC());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Session) {
                Session o = (Session) object;
                return getStringKey(o.getSessionPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Session.class.getName()});
                return null;
            }
        }

    }
    
    private String name;
    private Date date;
    private Date startTime;
    private Date endTime;
    private String auditorium;
    private String type;
    
    private String message;
    
    @Inject
    AuditoriumController ac;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(String auditorium) {
        this.auditorium = auditorium;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
    
    
    
    
    
    
    
    public String rate() {
        return "/rating.xhtml?faces-redirect=true";
    }
    
    public String details() {
        return "/sessionDetails.xhtml?faces-redirect=true";   
    }
    
    public Session getById(int id) {
        return getFacade().getSession(id);
    }
    
    public int getConferenceId(int idS) {
        return getFacade().getConferenceId(idS);
    }
    
    
    public String insertSession(Conference c) {
        if (checkName() && checkTimes() && checkDate(c.getStartDate(), c.getEndDate())) {
            prepareCreate();
            selected.setName(name);
            selected.setConference(c);
            selected.setDate(date);
            selected.setEndTime(endTime);
            selected.setStartTime(startTime);
            selected.setIdA(ac.getAuditorium(Integer.parseInt(auditorium)));
            selected.setSessionType(type);
            selected.setSessionPK(new SessionPK(getFacade().nextId(), c.getId()));
            persist(PersistAction.CREATE, message);
            c.getSessionCollection().add(selected);
            return "/addSession?faces-redirect=true";
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid field!", this.message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "addSession";
        }
        
        
    }
    
    private boolean checkName() {
        boolean checked = false;
        if (name != null && !name.isEmpty() && name.length() <= 50) {
            checked = true;
        } else {
            message = "Invalid title!";
        }
        return checked;
    }
    
    private boolean checkTimes() {
        boolean checked = false;
        if (startTime != null && endTime != null) {
            Long d = endTime.getTime() - startTime.getTime();
            if (d >= 0) {
                checked = true;
            } else {
                message = "Invalid times!";
            }
        }
        return checked;
    }
    
    private boolean checkDate(Date startDate, Date endDate) {
        boolean checked = false;
        if (date!=null) {
            long after_start = (date.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
            long before_end = (endDate.getTime() - date.getTime()) / (24 * 60 * 60 * 1000);
            if (after_start >= 0 && before_end >= 0)
                checked = true;
            else {
                message = "Date is not within conference dates span!";
            }
            
        } else {
            message = "Insert date!";
        }
        return checked;
    }
    
    
    public String addLectures() {
        
        return "/addLecture.xhtml?faces-redirect=true";
    }
    
    
    

}
