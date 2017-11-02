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
import javax.persistence.JoinColumn;
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
@Table(name = "session")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Session.findAll", query = "SELECT s FROM Session s")
    , @NamedQuery(name = "Session.findById", query = "SELECT s FROM Session s WHERE s.sessionPK.id = :id")
    , @NamedQuery(name = "Session.findByName", query = "SELECT s FROM Session s WHERE s.name = :name")
    , @NamedQuery(name = "Session.findByDate", query = "SELECT s FROM Session s WHERE s.date = :date")
    , @NamedQuery(name = "Session.findByStartTime", query = "SELECT s FROM Session s WHERE s.startTime = :startTime")
    , @NamedQuery(name = "Session.findByEndTime", query = "SELECT s FROM Session s WHERE s.endTime = :endTime")
    , @NamedQuery(name = "Session.findByIdC", query = "SELECT s FROM Session s WHERE s.sessionPK.idC = :idC")
    , @NamedQuery(name = "Session.findBySessionType", query = "SELECT s FROM Session s WHERE s.sessionType = :sessionType")})
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SessionPK sessionPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_time")
    @Temporal(TemporalType.TIME)
    private Date startTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_time")
    @Temporal(TemporalType.TIME)
    private Date endTime;
    @Size(max = 40)
    @Column(name = "session_type")
    private String sessionType;
    @JoinColumn(name = "idC", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Conference conference;
    @JoinColumn(name = "idA", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Auditorium idA;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "session")
    private Collection<Agenda> agendaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "session")
    private Collection<Lecture> lectureCollection;

    public Session() {
    }

    public Session(SessionPK sessionPK) {
        this.sessionPK = sessionPK;
    }

    public Session(SessionPK sessionPK, String name, Date date, Date startTime, Date endTime) {
        this.sessionPK = sessionPK;
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Session(int id, int idC) {
        this.sessionPK = new SessionPK(id, idC);
    }

    public SessionPK getSessionPK() {
        return sessionPK;
    }

    public void setSessionPK(SessionPK sessionPK) {
        this.sessionPK = sessionPK;
    }

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

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public Auditorium getIdA() {
        return idA;
    }

    public void setIdA(Auditorium idA) {
        this.idA = idA;
    }

    @XmlTransient
    public Collection<Agenda> getAgendaCollection() {
        return agendaCollection;
    }

    public void setAgendaCollection(Collection<Agenda> agendaCollection) {
        this.agendaCollection = agendaCollection;
    }

    @XmlTransient
    public Collection<Lecture> getLectureCollection() {
        return lectureCollection;
    }

    public void setLectureCollection(Collection<Lecture> lectureCollection) {
        this.lectureCollection = lectureCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sessionPK != null ? sessionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Session)) {
            return false;
        }
        Session other = (Session) object;
        if ((this.sessionPK == null && other.sessionPK != null) || (this.sessionPK != null && !this.sessionPK.equals(other.sessionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.Session[ sessionPK=" + sessionPK + " ]";
    }
    
}
