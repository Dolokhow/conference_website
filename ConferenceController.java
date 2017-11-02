package controllers;

import beans.Agenda;
import beans.City;
import beans.Conference;
import beans.User;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import sessionBeans.ConferenceFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
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

@Named("conferenceController")
@SessionScoped
public class ConferenceController implements Serializable {

    @EJB
    private sessionBeans.ConferenceFacade ejbFacade;
    private List<Conference> items = null;
    private Conference selected;

    public ConferenceController() {
    }

    public Conference getSelected() {
        return selected;
    }

    public void setSelected(Conference selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ConferenceFacade getFacade() {
        return ejbFacade;
    }

    public Conference prepareCreate() {
        selected = new Conference();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ConferenceCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ConferenceUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ConferenceDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Conference> getItems() {
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

    public Conference getConference(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Conference> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Conference> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Conference.class)
    public static class ConferenceControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ConferenceController controller = (ConferenceController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "conferenceController");
            return controller.getConference(getKey(value));
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
            if (object instanceof Conference) {
                Conference o = (Conference) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Conference.class.getName()});
                return null;
            }
        }

    }

    // ADDED PROPERTIES! --------------------------------------------------------------------------------------------------
    private String keywordTitle = null;
    private String keywordArea = null;

    private Date dateFrom = null;
    private Date dateTo = null;

    private String title;
    private String passcode;
    private Date startDate;
    private Date endDate;
    private Date openingTime;
    private int city;
    private int location;
    private String sciArea;

    private String message;
    
    @Inject
    CityController cc;
    @Inject 
    MessageController mc;
    @Inject
    AgendaController ac;
    @Inject
    LocationController lc;

    public String getKeywordTitle() {
        return keywordTitle;
    }

    public void setKeywordTitle(String keywordTitle) {
        this.keywordTitle = keywordTitle;
    }

    public String getKeywordArea() {
        return keywordArea;
    }

    public void setKeywordArea(String keywordArea) {
        this.keywordArea = keywordArea;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Date openingTime) {
        this.openingTime = openingTime;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getSciArea() {
        return sciArea;
    }

    public void setSciArea(String sciArea) {
        this.sciArea = sciArea;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }
    
    

    public List<String> byNames(String key) {
        List<String> candidates = new ArrayList<>();
        List<Conference> conf = getFacade().getByName(key);
        for (Conference c : conf) {
            candidates.add(c.getTitle());
        }
        return candidates;
    }

    public List<String> byArea(String key) {
        List<String> candidates = new ArrayList<>();
        List<Conference> conf = getFacade().getByArea(key);
        for (Conference c : conf) {
            candidates.add(c.getScientificArea());
        }
        return candidates;
    }

    public List<Conference> search() {
        List<Conference> conferences = getFacade().search(keywordTitle, keywordArea, dateFrom, dateTo);
        keywordArea = null;
        keywordTitle = null;
        dateFrom = null;
        dateTo = null;
        return conferences;
    }

    public String details(boolean option) {
        if (option) {
            return "/conferenceDetails.xhtml?faces-redirect=true";
        } else {
            return "/registerUser.xhtml?faces-redirect=true";
        }
    }

    public String reload() {
        return "/homepage.xhtml?faces-redirect=true";
    }

    public Collection<Agenda> getAgendas(AgendaController ac) {

        Set<Agenda> removalSet = new HashSet<>();
        Collection<Agenda> agendas = (ac.getNewItems());
        for (Agenda a : agendas) {
            if (a.getAgendaPK().getIdC() != selected.getId()) {
                removalSet.add(a);
            }
        }
        agendas.removeAll(removalSet);
        removalSet.clear();
        int i = 0;
        int j;
        for (Agenda a : agendas) {
            User idU = a.getUser();
            j = 0;
            for (Agenda a_others : agendas) {
                if (a_others.getUser().equals(idU) && j > i) {
                    removalSet.add(a_others);
                }
                j++;
            }
            i++;
        }
        agendas.removeAll(removalSet);
        return agendas;
    }

    public String veiwGallery() {
        return "/gallery.xhtml?faces-redirect=true";
    }

    public String insertConference() {
        if (checkTitle() && checkSciArea() && checkPasscode() && checkDates()) {
            prepareCreate();
            selected.setTitle(title);
            selected.setScientificArea(sciArea);
            selected.setCanceled(0);
            selected.setEntryCode(passcode);
            selected.setStartDate(startDate);
            selected.setEndDate(endDate);
            selected.setIdC(lc.getLocation(location).getIdC());
            selected.setIdL(lc.getLocation(location));
            selected.setOpeningTime(openingTime);

            message = "Success!";
            persist(PersistAction.CREATE, message);
            items = null;
            return "/admin.xhtml?faces-redirect=true";
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid field!", this.message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "admin";
        }
    }


    private boolean checkTitle() {
        boolean checked = false;
        if (title != null && !title.isEmpty() && title.length() <= 50) {
            checked = true;
        } else {
            message = "Invalid title!";
        }
        return checked;
    }

    private boolean checkSciArea() {
        boolean checked = false;
        if (sciArea != null && !sciArea.isEmpty() && sciArea.length() <= 50) {
            checked = true;
        } else {
            message = "Invalid scientific area!";
        }
        return checked;
    }

    private boolean checkPasscode() {
        boolean checked = false;
        if (passcode != null && !passcode.isEmpty() && passcode.length() <= 10) {
            checked = true;
        } else {
            message = "Invalid passcode!";
        }
        return checked;
    }

    private boolean checkDates() {
        boolean checked = false;
        if (startDate != null && endDate != null) {
            Long d = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
            if (d >= 0) {
                checked = true;
            } else {
                message = "Invalid dates!";
            }
        }
        return checked;
    }
    
    public String addMods() {
        return "/modifyConference.xhtml?faces-redirect=true";
    }
    
    public String cancel(User u) {
        selected.setCanceled(1);
        persist(PersistAction.CREATE, message);
        Collection<Agenda> agendas = getAgendas(ac);
        for (Agenda a: agendas) {
            mc.setNewMessage("Conference " + selected.getTitle() + " on the field of " + selected.getScientificArea() +
                    " planned for the date " + selected.getStartDate() + " has been canceled!");
            mc.sendMessage(u, a.getUser());
        }
        
        return "/admin.xhtml?faces-redirect=true";
    }
    
    public String modify() {
        return "/addSession.xhtml?faces-redirect=true";
    }
    
    public boolean conferenceExpired() {
        boolean notExpired = true;
        Date today = new Date();
        long diff = (selected.getStartDate().getTime() - today.getTime())/(24*60*60*1000);
        if (diff < 3)
            notExpired = false;
        return notExpired;
    }

}
