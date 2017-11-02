/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author djordjebozic
 */
@Entity
@Table(name = "lecture")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lecture.findAll", query = "SELECT l FROM Lecture l")
    , @NamedQuery(name = "Lecture.findById", query = "SELECT l FROM Lecture l WHERE l.lecturePK.id = :id")
    , @NamedQuery(name = "Lecture.findByName", query = "SELECT l FROM Lecture l WHERE l.name = :name")
    , @NamedQuery(name = "Lecture.findByStartTime", query = "SELECT l FROM Lecture l WHERE l.startTime = :startTime")
    , @NamedQuery(name = "Lecture.findByIdS", query = "SELECT l FROM Lecture l WHERE l.lecturePK.idS = :idS")
    , @NamedQuery(name = "Lecture.findByPdfPath", query = "SELECT l FROM Lecture l WHERE l.pdfPath = :pdfPath")
    , @NamedQuery(name = "Lecture.findByPptPath", query = "SELECT l FROM Lecture l WHERE l.pptPath = :pptPath")
    , @NamedQuery(name = "Lecture.findByIdC", query = "SELECT l FROM Lecture l WHERE l.lecturePK.idC = :idC")})
public class Lecture implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LecturePK lecturePK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Size(max = 300)
    @Column(name = "pdf_path")
    private String pdfPath;
    @Size(max = 300)
    @Column(name = "ppt_path")
    private String pptPath;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "lectureCollection")
    private Collection<Author> authorCollection;
    @JoinColumns({
        @JoinColumn(name = "idS", referencedColumnName = "id", insertable = false, updatable = false)
        , @JoinColumn(name = "idC", referencedColumnName = "idC", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Session session;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lecture")
    private Collection<Mark> markCollection;

    public Lecture() {
    }

    public Lecture(LecturePK lecturePK) {
        this.lecturePK = lecturePK;
    }

    public Lecture(LecturePK lecturePK, String name, Date startTime) {
        this.lecturePK = lecturePK;
        this.name = name;
        this.startTime = startTime;
    }

    public Lecture(int id, int idS, int idC) {
        this.lecturePK = new LecturePK(id, idS, idC);
    }

    public LecturePK getLecturePK() {
        return lecturePK;
    }

    public void setLecturePK(LecturePK lecturePK) {
        this.lecturePK = lecturePK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getPptPath() {
        return pptPath;
    }

    public void setPptPath(String pptPath) {
        this.pptPath = pptPath;
    }

    @XmlTransient
    public Collection<Author> getAuthorCollection() {
        return authorCollection;
    }

    public void setAuthorCollection(Collection<Author> authorCollection) {
        this.authorCollection = authorCollection;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @XmlTransient
    public Collection<Mark> getMarkCollection() {
        return markCollection;
    }

    public void setMarkCollection(Collection<Mark> markCollection) {
        this.markCollection = markCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lecturePK != null ? lecturePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lecture)) {
            return false;
        }
        Lecture other = (Lecture) object;
        if ((this.lecturePK == null && other.lecturePK != null) || (this.lecturePK != null && !this.lecturePK.equals(other.lecturePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.Lecture[ lecturePK=" + lecturePK + " ]";
    }
    
}
