package controllers;

import beans.Author;
import beans.Lecture;
import beans.LecturePK;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import sessionBeans.LectureFacade;

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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import beans.Mark;
import beans.Session;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.activation.MimetypesFileTypeMap;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@Named("lectureController")
@SessionScoped
public class LectureController implements Serializable {

    @EJB
    private sessionBeans.LectureFacade ejbFacade;
    private List<Lecture> items = null;
    private Lecture selected;

    public LectureController() {
    }

    public Lecture getSelected() {
        return selected;
    }

    public void setSelected(Lecture selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getLecturePK().setIdC(selected.getSession().getSessionPK().getIdC());
        selected.getLecturePK().setIdS(selected.getSession().getSessionPK().getId());
    }

    protected void initializeEmbeddableKey() {
        selected.setLecturePK(new beans.LecturePK());
    }

    private LectureFacade getFacade() {
        return ejbFacade;
    }

    public Lecture prepareCreate() {
        selected = new Lecture();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("LectureCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("LectureUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("LectureDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Lecture> getItems() {
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

    public Lecture getLecture(beans.LecturePK id) {
        return getFacade().find(id);
    }

    public List<Lecture> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Lecture> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Lecture.class)
    public static class LectureControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LectureController controller = (LectureController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "lectureController");
            return controller.getLecture(getKey(value));
        }

        beans.LecturePK getKey(String value) {
            beans.LecturePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new beans.LecturePK();
            key.setId(Integer.parseInt(values[0]));
            key.setIdS(Integer.parseInt(values[1]));
            key.setIdC(Integer.parseInt(values[2]));
            return key;
        }

        String getStringKey(beans.LecturePK value) {
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
            if (object instanceof Lecture) {
                Lecture o = (Lecture) object;
                return getStringKey(o.getLecturePK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Lecture.class.getName()});
                return null;
            }
        }

    }

    // ADDED PROPERTIES! --------------------------------------------------------------------------------------------------
    private UploadedFile ppt;
    private UploadedFile pdf;

    private String ppt_path;
    private String pdf_path;

    private String message;

    private StreamedContent ppt_file;
    private StreamedContent pdf_file;

    private String name;
    private Date time;

    private List<String> names;

    @Inject
    AuthorController ac;

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public UploadedFile getPpt() {
        return ppt;
    }

    public void setPpt(UploadedFile ppt) {
        this.ppt = ppt;
    }

    public UploadedFile getPdf() {
        return pdf;
    }

    public void setPdf(UploadedFile pdf) {
        this.pdf = pdf;
    }

    public String getPpt_path() {
        return ppt_path;
    }

    public void setPpt_path(String ppt_path) {
        this.ppt_path = ppt_path;
    }

    public String getPdf_path() {
        return pdf_path;
    }

    public void setPdf_path(String pdf_path) {
        this.pdf_path = pdf_path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StreamedContent getPpt_file() {
        return ppt_file;
    }

    public void setPpt_file(StreamedContent ppt_file) {
        this.ppt_file = ppt_file;
    }

    public StreamedContent getPdf_file() {
        return pdf_file;
    }

    public void setPdf_file(StreamedContent pdf_file) {
        this.pdf_file = pdf_file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int average(Lecture l) {
        Collection<Mark> marks = l.getMarkCollection();
        Double avg = 0.0;
        double count = 0;
        for (Mark m : marks) {
            avg += m.getValue();
            count++;
        }
        avg = avg / count;
        return avg.intValue();
    }

    public String markPage() {
        return "/markPage.xhtml?faces-redirect=true";
    }

    public void upload() {

        if (ppt.getFileName() != null && !ppt.getFileName().isEmpty()) {
            try {
                InputStream input = ppt.getInputstream();
                Path folder = Paths.get("/Users/djordjebozic/konferencija_server_data/lectures");

                String filename = FilenameUtils.getBaseName(ppt.getFileName());
                String extension = FilenameUtils.getExtension(ppt.getFileName());

                Path file = Files.createTempFile(folder, filename + "-", "." + extension);
                Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
                ppt_path = "lectures/" + file.getFileName().toString();
            } catch (IOException ex) {
                FacesMessage msg = new FacesMessage("Error");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (pdf.getFileName() != null && !pdf.getFileName().isEmpty()) {
            try {
                InputStream input = pdf.getInputstream();
                Path folder = Paths.get("/Users/djordjebozic/konferencija_server_data/lectures");

                String filename = FilenameUtils.getBaseName(pdf.getFileName());
                String extension = FilenameUtils.getExtension(pdf.getFileName());

                Path file = Files.createTempFile(folder, filename + "-", "." + extension);
                Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
                pdf_path = "lectures/" + file.getFileName().toString();
            } catch (IOException ex) {
                FacesMessage msg = new FacesMessage("Error");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    public void uploadListener(FileUploadEvent e) {

        if (ppt.getFileName() != null && !ppt.getFileName().isEmpty()) {
            try {
                InputStream input = ppt.getInputstream();
                Path folder = Paths.get("/Users/djordjebozic/konferencija_server_data/lectures");

                String filename = FilenameUtils.getBaseName(ppt.getFileName());
                String extension = FilenameUtils.getExtension(ppt.getFileName());

                Path file = Files.createTempFile(folder, filename + "-", "." + extension);
                Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
                ppt_path = "lectures/" + file.getFileName().toString();
            } catch (IOException ex) {
                FacesMessage msg = new FacesMessage("Error");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (pdf.getFileName() != null && !pdf.getFileName().isEmpty()) {
            try {
                InputStream input = pdf.getInputstream();
                Path folder = Paths.get("/Users/djordjebozic/konferencija_server_data/lectures");

                String filename = FilenameUtils.getBaseName(pdf.getFileName());
                String extension = FilenameUtils.getExtension(pdf.getFileName());

                Path file = Files.createTempFile(folder, filename + "-", "." + extension);
                Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
                pdf_path = "lectures/" + file.getFileName().toString();
            } catch (IOException ex) {
                FacesMessage msg = new FacesMessage("Error");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public String addMaterials() {
        if (selected != null) {
            upload();

            if (ppt_path != null && !ppt_path.isEmpty()) {
                selected.setPptPath(ppt_path);
            }

            if (pdf_path != null && !pdf_path.isEmpty()) {
                selected.setPdfPath(pdf_path);
            }

            message = "File upload successful!";
            persist(PersistAction.CREATE, message);

        }
        return "markPage";
    }
    
    public String addModification() {
        if (selected != null) {

            if (ppt_path != null && !ppt_path.isEmpty()) {
                selected.setPptPath(ppt_path);
            }

            if (pdf_path != null && !pdf_path.isEmpty()) {
                selected.setPdfPath(pdf_path);
            }

            message = "File upload successful!";
            persist(PersistAction.CREATE, message);

        }
        return "markPage";
    }
    

    public String addMaterialsM() {
        if (selected != null) {
            upload();

            if (ppt_path != null && !ppt_path.isEmpty()) {
                selected.setPptPath(ppt_path);
            }

            if (pdf_path != null && !pdf_path.isEmpty()) {
                selected.setPdfPath(pdf_path);
            }

            message = "File upload successful!";
            persist(PersistAction.CREATE, message);

        }
        return "lectureModify";
    }

    public StreamedContent downloadPPT() {
        if (selected.getPptPath() != null && !selected.getPptPath().isEmpty()) {
            File f = new File("/Users/djordjebozic/konferencija_server_data/" + selected.getPptPath());
            String name = modifyName(f.getName());
            try {
                ppt_file = new DefaultStreamedContent(new FileInputStream(f), new MimetypesFileTypeMap().getContentType(f), name);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(LectureController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ppt_file;
    }

    public StreamedContent downloadPDF() {
        if (selected.getPdfPath() != null && !selected.getPdfPath().isEmpty()) {
            File f = new File("/Users/djordjebozic/konferencija_server_data/" + selected.getPdfPath());
            String name = modifyName(f.getName());

            try {
                pdf_file = new DefaultStreamedContent(new FileInputStream(f), new MimetypesFileTypeMap().getContentType(f), name);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(LectureController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pdf_file;
    }

    public boolean exists(int option) {
        if (option == 0) {
            return (selected.getPptPath() != null && !selected.getPptPath().isEmpty());
        } else {
            return (selected.getPdfPath() != null && !selected.getPdfPath().isEmpty());
        }
    }

    private String modifyName(String name) {
        boolean dot_found = false;
        boolean dash_found = false;
        int dot_index = 0;
        int dash_index = 0;
        for (int i = name.length() - 1; i >= 0; i--) {
            if (!dot_found) {
                if (name.charAt(i) == '.') {
                    dot_index = i;
                    dot_found = true;
                }
            }
            if (!dash_found) {
                if (name.charAt(i) == '-') {
                    dash_index = i;
                    dash_found = true;
                }
            }
            if (dot_found && dash_found) {
                break;
            }
        }
        if (dash_found && dot_found) {
            name = name.replace(name.substring(dash_index, dot_index), "");
        }
        return name;
    }

    public String insertLecture(Session s) {
        if (checkTime(s.getStartTime(), s.getEndTime()) && name.length() < 100) {
            prepareCreate();
            selected.setName(name);
            selected.setSession(s);
            selected.setStartTime(time);
            selected.setLecturePK(new LecturePK(getFacade().nextId(), s.getSessionPK().getId(), s.getSessionPK().getIdC()));
            persist(PersistAction.CREATE, message);
            s.getLectureCollection().add(selected);
            return "/addLecture.xhtml?faces-redirect=true";
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid field!", this.message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "addLecture";
        }

    }

    private boolean checkTime(Date startTime, Date endTime) {
        boolean checked = false;
        if (time != null) {
            long after_start = (time.getTime() - startTime.getTime());
            long before_end = (endTime.getTime() - time.getTime());
            if (after_start >= 0 && before_end >= 0) {
                checked = true;
            } else {
                message = "Time is not within session times span!";
            }

        } else {
            message = "Insert time!";
        }
        return checked;
    }

    public String modify() {
        return "/lectureModify.xhtml?faces-redirect=true";
    }

    public String addAuthors() {
        if (!names.isEmpty()) {
            for (String i : names) {
                Author a = ac.getAuthor(Integer.parseInt(i));
                selected.getAuthorCollection().add(a);
                a.getLectureCollection().add(selected);
                ac.setSelected(a);
                ac.persist(PersistAction.CREATE, message);

            }
        }
        persist(PersistAction.CREATE, null);
        return "/lectureModify.xhtml?faces-redirect=true";
    }

}
