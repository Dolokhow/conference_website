package controllers;

import beans.Administrator;
import beans.Conference;
import beans.User;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import sessionBeans.UserFacade;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import util.ControllerUtils;

@Named("userController")
@SessionScoped
public class UserController implements Serializable {

    @EJB
    private sessionBeans.UserFacade ejbFacade;
    private List<User> items = null;
    private User selected;
    private User profile_selected;
    private User chatProfile;

    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String institution;
    private String gender;
    private String profile_image;
    private String t_shirt_size;
    private String linkedIn_profile;
    private UploadedFile image;
    private StreamedContent preview;

    private String message;

    public UserController() {
    }

    public User getSelected() {
        return selected;
    }

    public void setSelected(User selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UserFacade getFacade() {
        return ejbFacade;
    }

    public User prepareCreate() {
        selected = new User();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UserCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UserUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UserDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<User> getItems() {
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
                    if (profile_selected != null) {
                        getFacade().edit(profile_selected);
                    }
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

    public User getUser(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<User> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<User> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = User.class)
    public static class UserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserController controller = (UserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userController");
            return controller.getUser(getKey(value));
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
            if (object instanceof User) {
                User o = (User) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), User.class.getName()});
                return null;
            }
        }

    }

    // ADDED PROPERTIES! --------------------------------------------------------------------------------------------------
    private boolean show_chat = false;
    private boolean registered = true;

    private String old_password;
    private String new_password;

    private boolean password_match = false;

    private Collection<String> moderators;
    private List<Conference> forbiden = new ArrayList<>();

    @Inject
    AdministratorController ac;
    @Inject
    ModeratorController mc;

    public boolean isShow_chat() {
        return show_chat;
    }

    public void setShow_chat(boolean show_chat) {
        this.show_chat = show_chat;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getT_shirt_size() {
        return t_shirt_size;
    }

    public void setT_shirt_size(String t_shirt_size) {
        this.t_shirt_size = t_shirt_size;
    }

    public String getLinkedIn_profile() {
        return linkedIn_profile;
    }

    public void setLinkedIn_profile(String linkedIn_profile) {
        this.linkedIn_profile = linkedIn_profile;
    }

    public UploadedFile getImage() {
        return image;
    }

    public void setImage(UploadedFile image) {
        this.image = image;
    }

    public StreamedContent getPreview() {
        return preview;
    }

    public void setPreview(StreamedContent preview) {
        this.preview = preview;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getProfile_selected() {
        return profile_selected;
    }

    public void setProfile_selected(User profile_selected) {
        this.profile_selected = profile_selected;
    }

    public User getChatProfile() {
        return chatProfile;
    }

    public void setChatProfile(User chatProfile) {
        this.chatProfile = chatProfile;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public Collection<String> getModerators() {
        return moderators;
    }

    public void setModerators(Collection<String> moderators) {
        this.moderators = moderators;
    }

    public List<Conference> getForbiden() {
        return forbiden;
    }

    public void setForbiden(List<Conference> forbiden) {
        this.forbiden = forbiden;
    }
    

    public String login() {
        if (selected != null) {
            registered = true;
            if (ac.isAdmin(selected.getId())) {
                return "/admin.xhtml?faces-redirect=true";
            } else {
                return "/homepage.xhtml?faces-redirect=true";
            }
        } else {
            return "index.xhtml";
        }

    }

    public void loginListener(ActionEvent e) {
        FacesMessage msg = null;
        selected = null;
        selected = ejbFacade.getLoginInfo(username, password);

        if (selected == null) {
            this.message = "Invalid credentials!";
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", this.message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public String logout() {
        selected = null;
        items = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }

    public String register() {

        ControllerUtils cu = ControllerUtils.instance();
        if (!ejbFacade.checkUsernameAvailability(username)) {
            FacesMessage msg = null;
            this.message = "Invalid credentials!";
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Chose another username!", this.message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            username = null;
            logout();
            return "/registerUser.xhtml";

        }

        if (password != null) {

            if (password.length() < 8 || password.length() > 12 || !Character.isLetterOrDigit(password.charAt(0))
                    || cu.sequenceLength(password) || !cu.countNumerics(password)
                    || !cu.countCapital(password) || !cu.countSpecialChars(password)) {

                FacesMessage msg = null;
                this.message = "Invalid credentials!";
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "InvalidPassword!", this.message);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                password = null;
                logout();
                return "/registerUser";

            }
        }

        upload();

        selected = prepareCreate();
        selected.setEmail(email);
        selected.setGender(gender.charAt(0));
        selected.setInstitution(institution);
        selected.setLinkedInprofile(linkedIn_profile);
        selected.setName(name);
        selected.setPassword(password);
        selected.setSurname(surname);
        selected.setUsername(username);
        selected.setTShirtSize(t_shirt_size);
        if (profile_image != null && !profile_image.isEmpty()) {
            selected.setProfileImage(profile_image);
        } else {
            selected.setProfileImage("profile_images/user_default.jpg");
        }

        persist(PersistAction.CREATE, message);
        logout();
        return "/index.xhtml?faces-redirect=true";
    }

    public void upload() {
        if (image.getFileName() != null && !image.getFileName().isEmpty()) {
            try {
                InputStream input = image.getInputstream();
                Path folder = Paths.get("/Users/djordjebozic/konferencija_server_data/profile_images");

                String filename = FilenameUtils.getBaseName(image.getFileName());
                String extension = FilenameUtils.getExtension(image.getFileName());

                Path file = Files.createTempFile(folder, filename + "-", "." + extension);
                Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
                profile_image = "profile_images/" + file.getFileName().toString();
            } catch (IOException ex) {
                FacesMessage msg = new FacesMessage("Error");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public String addFriend() {
        if (profile_selected != null) {
            selected.getUserCollection().add(profile_selected);
            profile_selected.getUserCollection1().add(selected);
            message = "User successfully added to contacts!";
            persist(PersistAction.CREATE, message);
        }
        return "/conferenceDetails?faces-redirect=true";
    }

    public void addAdmin() {
        Administrator a = ac.getAdmin();
        profile_selected = getFacade().find(a.getId());
        if (isNotFriend()) {
            selected.getUserCollection().add(profile_selected);
            profile_selected.getUserCollection1().add(selected);
            message = "User successfully added to contacts!";
            persist(PersistAction.CREATE, message);
        }
    }

    public boolean isNotFriend() {
        if (profile_selected != null) {
            if (profile_selected.equals(selected) || selected.getUserCollection().contains(profile_selected)) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public String profilePage() {
        return "/userProfile.xhtml?faces-redirect=true";
    }

    public String chat() {
        show_chat = true;
        return "homepage";
    }

    public String closeChat() {
        show_chat = false;
        return "homepage";
    }

    public String continueAsUnregistered() {
        registered = false;
        return "/homepage.xhtml?faces-redirect=true";
    }

    public String changePassword() {
        if (selected != null) {
            registered = true;
            return "/changePassword.xhtml?faces-redirect=true";
        } else {
            return "index.xhtml";
        }
    }

    public void modifyListener(ActionEvent e) {
        FacesMessage msg = null;
        if (old_password == null || old_password.isEmpty() || !old_password.equals(selected.getPassword())) {
            this.message = "Invalid credentials!";
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Password missmatch!", this.message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            password_match = false;

        } else {

            ControllerUtils cu = ControllerUtils.instance();
            if (new_password == null || new_password.isEmpty() || new_password.length() < 8
                    || new_password.length() > 12 || !Character.isLetterOrDigit(new_password.charAt(0))
                    || cu.sequenceLength(new_password) || !cu.countNumerics(new_password)
                    || !cu.countCapital(new_password) || !cu.countSpecialChars(new_password)) {

                this.message = "Invalid new password!";
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Password!", this.message);
                FacesContext.getCurrentInstance().addMessage(null, msg);
                new_password = null;

                password_match = false;
            } else {
                password_match = true;
            }
        }
    }

    public String modify() {
        if (password_match) {
            selected.setPassword(new_password);
            message = "Password change successful!";
            persist(PersistAction.CREATE, message);
            return "/index.xhtml?faces-redirect=true";
        } else {
            return "changePassword";
        }
    }

    public String addModerators(Conference c) {
        if (!moderators.isEmpty()) {
            for (String i : moderators) {
                User u = getUser(Integer.parseInt(i));
                mc.addModerator(u, c);
            }
        }

        return "admin.xhtml?faces-redirect=true";
    }
    
    public boolean isAdmin() {
        boolean notAdmin = true;
        if (chatProfile != null) {
            if (ac.isAdmin(chatProfile.getId()))
                notAdmin = false;
        }
        return notAdmin;
    }

}
