package controllers;

import beans.Agenda;
import beans.AgendaPK;
import beans.Conference;
import beans.Session;
import beans.User;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import sessionBeans.AgendaFacade;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@Named("agendaController")
@SessionScoped
public class AgendaController implements Serializable {

    @EJB
    private sessionBeans.AgendaFacade ejbFacade;
    private List<Agenda> items = null;
    private Agenda selected;
    
    private String message;

    public AgendaController() {
    }

    public Agenda getSelected() {
        return selected;
    }

    public void setSelected(Agenda selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getAgendaPK().setIdU(selected.getUser().getId());
        selected.getAgendaPK().setIdC(selected.getSession().getSessionPK().getIdC());
        selected.getAgendaPK().setIdS(selected.getSession().getSessionPK().getId());
    }

    protected void initializeEmbeddableKey() {
        selected.setAgendaPK(new beans.AgendaPK());
    }

    private AgendaFacade getFacade() {
        return ejbFacade;
    }

    public Agenda prepareCreate() {
        selected = new Agenda();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AgendaCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AgendaUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AgendaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Agenda> getItems() {
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

    public Agenda getAgenda(beans.AgendaPK id) {
        return getFacade().find(id);
    }

    public List<Agenda> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Agenda> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Agenda.class)
    public static class AgendaControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AgendaController controller = (AgendaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "agendaController");
            return controller.getAgenda(getKey(value));
        }

        beans.AgendaPK getKey(String value) {
            beans.AgendaPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new beans.AgendaPK();
            key.setIdS(Integer.parseInt(values[0]));
            key.setIdU(Integer.parseInt(values[1]));
            key.setIdC(Integer.parseInt(values[2]));
            return key;
        }

        String getStringKey(beans.AgendaPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdS());
            sb.append(SEPARATOR);
            sb.append(value.getIdU());
            sb.append(SEPARATOR);
            sb.append(value.getIdC());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Agenda) {
                Agenda o = (Agenda) object;
                return getStringKey(o.getAgendaPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Agenda.class.getName()});
                return null;
            }
        }

    }
    
    // ADDED PROPERTIES! --------------------------------------------------------------------------------------------------
    
    private String entryCode;
    private boolean checked;
    Map<Integer, Integer> parameter_map = new HashMap();
    
    @Inject
    UserController uc;

    public String getEntryCode() {
        return entryCode;
    }
    
    

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }
    
    public String checkEntryCode(Conference c) {
        if (entryCode!= null && !entryCode.isEmpty()) {
            if (entryCode.equals(c.getEntryCode()))
                checked = true;
            else {
                checked = false;
                if (parameter_map.get(c.getId()) == null) {
                    parameter_map.put(c.getId(), 1);
                }
                else if (parameter_map.get(c.getId()) < 2) {
                    parameter_map.put(c.getId(), 2);
                }
                
                message = "Incorrect Entry code!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
                return "sessionDetails";
            }
        }
        else {
            checked = false;
            message = "Enter Entry code!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
            return "sessionDetails";
        }
        return "/sessionDetails.xhtml?faces-redirect=true";
    }
    
    
    public String addAgendaForUser(User u, Session s) {
        if(checked == true && parameter_map.get(s.getSessionPK().getId()) == null) {
            
            completeCreate(u, s);
            uc.addAdmin();
            return "/conferenceDetails.xhtml?faces-redirect=true";
        }
        else if (checked == true && parameter_map.get(s.getSessionPK().getId()) != 2) {
            completeCreate(u, s);
            uc.addAdmin();
            return "/conferenceDetails.xhtml?faces-redirect=true";
        }
        else {
            message = "Session is blocked for you!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
            return "sessionDetails";
        }
    }
    
    public void addAgendaListener(ActionEvent e) {
        if (checked == false) {
            message = "Incorrect entry code or session blocked!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
        }
    }
    
    public Agenda completeCreate(User u, Session s){
        
        prepareCreate();
        AgendaPK pk = new AgendaPK(s.getSessionPK().getId(), u.getId(), s.getSessionPK().getIdC());
        selected.setAgendaPK(pk);
        selected.setUser(u);
        selected.setSession(s);
        message = "Session successfully added to agenda!";
        persist(PersistAction.CREATE, message);
        s.getAgendaCollection().add(selected);
        u.getAgendaCollection().add(selected);
        return selected;
    }
    
    public boolean alreadyAdded(User u, Session s) {
        boolean notAdded = true;
        items = null;
        getItems();
        for (Agenda a: items) {
            if (a.getAgendaPK().getIdS() == s.getSessionPK().getId() && a.getAgendaPK().getIdU() == u.getId())
                notAdded = false;
        }
        return notAdded;
    }
    
    public boolean alreadyAddedC(Session s) {
        boolean notAdded = true;
        if (s.getSessionType().equals("Break"))
            notAdded = false;
        return notAdded;
    }
    
    public Collection<Agenda> getNewItems() {
        this.items = null;
        this.items = getFacade().findAll();
        return items;
    }
    
    
    
    public boolean checkPermission(int idS, int idU) {
        boolean permission = false;
        Agenda a = getFacade().getAgenda(idS, idU);
        if (a != null)
            permission = true;
        return permission;
    }

    
    

}
