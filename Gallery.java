/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author djordjebozic
 */
@Entity
@Table(name = "gallery")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Gallery.findAll", query = "SELECT g FROM Gallery g")
    , @NamedQuery(name = "Gallery.findById", query = "SELECT g FROM Gallery g WHERE g.galleryPK.id = :id")
    , @NamedQuery(name = "Gallery.findByImagePath", query = "SELECT g FROM Gallery g WHERE g.imagePath = :imagePath")
    , @NamedQuery(name = "Gallery.findByIdS", query = "SELECT g FROM Gallery g WHERE g.galleryPK.idS = :idS")
    , @NamedQuery(name = "Gallery.findByIdC", query = "SELECT g FROM Gallery g WHERE g.galleryPK.idC = :idC")})
public class Gallery implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GalleryPK galleryPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "image_path")
    private String imagePath;
    @JoinColumns({
        @JoinColumn(name = "idS", referencedColumnName = "id", insertable = false, updatable = false)
        , @JoinColumn(name = "idC", referencedColumnName = "idC", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Session session;

    public Gallery() {
    }

    public Gallery(GalleryPK galleryPK) {
        this.galleryPK = galleryPK;
    }

    public Gallery(GalleryPK galleryPK, String imagePath) {
        this.galleryPK = galleryPK;
        this.imagePath = imagePath;
    }

    public Gallery(int id, int idS, int idC) {
        this.galleryPK = new GalleryPK(id, idS, idC);
    }

    public GalleryPK getGalleryPK() {
        return galleryPK;
    }

    public void setGalleryPK(GalleryPK galleryPK) {
        this.galleryPK = galleryPK;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (galleryPK != null ? galleryPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gallery)) {
            return false;
        }
        Gallery other = (Gallery) object;
        if ((this.galleryPK == null && other.galleryPK != null) || (this.galleryPK != null && !this.galleryPK.equals(other.galleryPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.Gallery[ galleryPK=" + galleryPK + " ]";
    }
    
}
