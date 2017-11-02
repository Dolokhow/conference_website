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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "conference")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conference.findAll", query = "SELECT c FROM Conference c")
    , @NamedQuery(name = "Conference.findById", query = "SELECT c FROM Conference c WHERE c.id = :id")
    , @NamedQuery(name = "Conference.findByTitle", query = "SELECT c FROM Conference c WHERE c.title = :title")
    , @NamedQuery(name = "Conference.findByPartialTitle", query = "SELECT c FROM Conference c WHERE c.title like :title")
    , @NamedQuery(name = "Conference.findByStartDate", query = "SELECT c FROM Conference c WHERE c.startDate = :startDate")
    , @NamedQuery(name = "Conference.findByEndDate", query = "SELECT c FROM Conference c WHERE c.endDate = :endDate")
    , @NamedQuery(name = "Conference.findByOpeningTime", query = "SELECT c FROM Conference c WHERE c.openingTime = :openingTime")
    , @NamedQuery(name = "Conference.findByCanceled", query = "SELECT c FROM Conference c WHERE c.canceled = :canceled")
    , @NamedQuery(name = "Conference.findByScientificArea", query = "SELECT c FROM Conference c WHERE c.scientificArea = :scientificArea")
    , @NamedQuery(name = "Conference.findByPartialScientificArea", query = "SELECT c FROM Conference c WHERE c.scientificArea like :scientificArea")
    , @NamedQuery(name = "Conference.searchTitleDate", query = "SELECT c FROM Conference c WHERE c.title = :title AND c.startDate >= :startDate AND c.endDate <= :endDate")
    , @NamedQuery(name = "Conference.searchAreaDate", query = "SELECT c FROM Conference c WHERE c.scientificArea = :scientificArea AND c.startDate >= :startDate AND c.endDate <= :endDate")
    , @NamedQuery(name = "Conference.searchTitleAreaDate", query = "SELECT c FROM Conference c WHERE c.title = :title AND c.scientificArea = :scientificArea AND c.startDate >= :startDate AND c.endDate <= :endDate")
    , @NamedQuery(name = "Conference.searchDate", query = "SELECT c FROM Conference c "
            + "WHERE ( c.startDate <= :startDate AND c.startDate <= :endDate ) OR ( c.endDate >= :startDate AND c.endDate <= :endDate )")
        
        
})
public class Conference implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "opening_time")
    @Temporal(TemporalType.TIME)
    private Date openingTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canceled")
    private int canceled;
    @Size(max = 50)
    @Column(name = "scientific_area")
    private String scientificArea;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "entryCode")
    private String entryCode;
    @ManyToMany(mappedBy = "conferenceCollection")
    private Collection<Moderator> moderatorCollection;
    @JoinColumn(name = "idC", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private City idC;
    @JoinColumn(name = "idL", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Location idL;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conference")
    private Collection<Session> sessionCollection;

    public Conference() {
    }

    public Conference(Integer id) {
        this.id = id;
    }

    public Conference(Integer id, String title, Date startDate, Date endDate, Date openingTime, int canceled) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.openingTime = openingTime;
        this.canceled = canceled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getCanceled() {
        return canceled;
    }

    public void setCanceled(int canceled) {
        this.canceled = canceled;
    }

    public String getScientificArea() {
        return scientificArea;
    }

    public void setScientificArea(String scientificArea) {
        this.scientificArea = scientificArea;
    }

    public String getEntryCode() {
        return entryCode;
    }

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }
    
    

    @XmlTransient
    public Collection<Moderator> getModeratorCollection() {
        return moderatorCollection;
    }

    public void setModeratorCollection(Collection<Moderator> moderatorCollection) {
        this.moderatorCollection = moderatorCollection;
    }

    public City getIdC() {
        return idC;
    }

    public void setIdC(City idC) {
        this.idC = idC;
    }
    
    public Location getIdL() {
        return idL;
    }

    public void setIdL(Location idL) {
        this.idL = idL;
    }

    @XmlTransient
    public Collection<Session> getSessionCollection() {
        return sessionCollection;
    }

    public void setSessionCollection(Collection<Session> sessionCollection) {
        this.sessionCollection = sessionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Conference)) {
            return false;
        }
        Conference other = (Conference) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.Conference[ id=" + id + " ]";
    }
    
}
