package controllers;

import beans.Conference;
import beans.Gallery;
import beans.GalleryPK;
import beans.Session;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import java.io.IOException;
import java.io.InputStream;
import sessionBeans.GalleryFacade;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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
import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.UploadedFile;

@Named("galleryController")
@SessionScoped
public class GalleryController implements Serializable {

    @EJB
    private sessionBeans.GalleryFacade ejbFacade;
    private List<Gallery> items = null;
    private Gallery selected;

    public GalleryController() {
    }

    public Gallery getSelected() {
        return selected;
    }

    public void setSelected(Gallery selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
        selected.setGalleryPK(new beans.GalleryPK());
    }

    private GalleryFacade getFacade() {
        return ejbFacade;
    }

    public Gallery prepareCreate() {
        selected = new Gallery();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("GalleryCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("GalleryUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("GalleryDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Gallery> getItems() {
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

    public Gallery getGallery(beans.GalleryPK id) {
        return getFacade().find(id);
    }

    public List<Gallery> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Gallery> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Gallery.class)
    public static class GalleryControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GalleryController controller = (GalleryController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "galleryController");
            return controller.getGallery(getKey(value));
        }

        beans.GalleryPK getKey(String value) {
            beans.GalleryPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new beans.GalleryPK();
            key.setId(Integer.parseInt(values[0]));
            key.setIdS(Integer.parseInt(values[1]));
            key.setIdC(Integer.parseInt(values[2]));
            return key;
        }

        String getStringKey(beans.GalleryPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getId());
            sb.append(SEPARATOR);
            sb.append(value.getIdS());
            sb.append(SEPARATOR);
            sb.append(value.getIdC());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Gallery) {
                Gallery o = (Gallery) object;
                return getStringKey(o.getGalleryPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Gallery.class.getName()});
                return null;
            }
        }

    }

    // ADDED PROPERTIES! --------------------------------------------------------------------------------------------------
    private int idS;
    private boolean found = false;

    private UploadedFile image;
    private String image_path;
    private String message;

    private boolean upload_successful = false;

    public int getIdS() {
        return idS;
    }

    public void setIdS(int idS) {
        this.idS = idS;
    }

    public UploadedFile getImage() {
        return image;
    }

    public void setImage(UploadedFile image) {
        this.image = image;
    }

    public String setCriteria(int s) {
        setIdS(s);
        found = true;
        return "/gallery.xhtml?faces-redirect=true";
    }

    public List<Gallery> getImages() {
        if (found == false) {
            return getItems();
        } else {
            List<Gallery> images = getFacade().getImages(idS);
            return images;
        }
    }

    public void upload() {
        if (image.getFileName() != null && !image.getFileName().isEmpty()) {
            try {
                InputStream input = image.getInputstream();
                Path folder = Paths.get("/Users/djordjebozic/konferencija_server_data/gallery");

                String filename = FilenameUtils.getBaseName(image.getFileName());
                String extension = FilenameUtils.getExtension(image.getFileName());

                Path file = Files.createTempFile(folder, filename + "-", "." + extension);
                Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
                image_path = "gallery/" + file.getFileName().toString();
                upload_successful = true;
            } catch (IOException ex) {
                upload_successful = false;
                FacesMessage msg = new FacesMessage("Error");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public String importImage(SessionController sc) {
        upload();
        if (upload_successful) {
            upload_successful = false;
            selected = prepareCreate();

            selected.setImagePath(image_path);
            GalleryPK pk = new GalleryPK(getFacade().nextId(), idS, sc.getConferenceId(idS));
            selected.setGalleryPK(pk);
            message = "Successful file upload!";
            persist(PersistAction.CREATE, message);
        }

        return "gallery";
    }
    
    public String importImageAsModerator(Session s) {
        upload();
        if (upload_successful) {
            upload_successful = false;
            selected = prepareCreate();

            selected.setImagePath(image_path);
            GalleryPK pk = new GalleryPK(getFacade().nextId(), s.getSessionPK().getId(), s.getSessionPK().getIdC());
            selected.setGalleryPK(pk);
            message = "Successful file upload!";
            persist(PersistAction.CREATE, message);
        }

        return "addLecture";
    }
}
