package controllers;

import beans.Message;
import beans.MessagePK;
import beans.User;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import sessionBeans.MessageFacade;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
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

@Named("messageController")
@SessionScoped
public class MessageController implements Serializable {

    @EJB
    private sessionBeans.MessageFacade ejbFacade;
    private List<Message> items = null;
    private Message selected;

    public MessageController() {
    }

    public Message getSelected() {
        return selected;
    }

    public void setSelected(Message selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getMessagePK().setIdRecipient(selected.getUser1().getId());
        selected.getMessagePK().setIdSender(selected.getUser().getId());
    }

    protected void initializeEmbeddableKey() {
        selected.setMessagePK(new beans.MessagePK());
    }

    private MessageFacade getFacade() {
        return ejbFacade;
    }

    public Message prepareCreate() {
        selected = new Message();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MessageCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MessageUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MessageDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Message> getItems() {
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

    public Message getMessage(beans.MessagePK id) {
        return getFacade().find(id);
    }

    public List<Message> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Message> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Message.class)
    public static class MessageControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MessageController controller = (MessageController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "messageController");
            return controller.getMessage(getKey(value));
        }

        beans.MessagePK getKey(String value) {
            beans.MessagePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new beans.MessagePK();
            key.setId(Integer.parseInt(values[0]));
            key.setIdSender(Integer.parseInt(values[1]));
            key.setIdRecipient(Integer.parseInt(values[2]));
            return key;
        }

        String getStringKey(beans.MessagePK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getId());
            sb.append(SEPARATOR);
            sb.append(value.getIdSender());
            sb.append(SEPARATOR);
            sb.append(value.getIdRecipient());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Message) {
                Message o = (Message) object;
                return getStringKey(o.getMessagePK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Message.class.getName()});
                return null;
            }
        }

    }

    private String newMessage;
    private String message;
    
    public String getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }
    
    public String chatHistory(User sender, User recipient) {
        String history = "";
        StringBuilder builder = new StringBuilder();
        List<Message> messages = getFacade().getMessages(sender.getId(), recipient.getId());
        
        for (Message m: messages) {
            builder.append(m.getUser().getName());
            builder.append(": ");
            builder.append(m.getBody());
            builder.append("\n");
            builder.append("\n");
        }
        
        history = builder.toString();
        
        
        
        
        return history;
    }
    
    public void sendMessage(User sender, User recipient) {
        if (newMessage == null)
            newMessage = "";
        prepareCreate();
        selected.setBody(newMessage);
        selected.setUser(sender);
        selected.setUser1(recipient);
        MessagePK pk = new MessagePK();
        pk.setIdRecipient(recipient.getId());
        pk.setIdSender(sender.getId());
        pk.setId(getFacade().nextId());
        selected.setMessagePK(pk);
        
        Calendar cal = Calendar.getInstance();
        
        Date now = new Date(cal.getTimeInMillis());
        selected.setDateTime(now);
        persist(PersistAction.CREATE, message);
        newMessage = null;
        //return "/homepage?faces-redirect=true";
    }
}
