/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author djordjebozic
 */
@Entity
@Table(name = "agenda")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Agenda.findAll", query = "SELECT a FROM Agenda a")
    , @NamedQuery(name = "Agenda.findByIdS", query = "SELECT a FROM Agenda a WHERE a.agendaPK.idS = :idS")
    , @NamedQuery(name = "Agenda.findByIdU", query = "SELECT a FROM Agenda a WHERE a.agendaPK.idU = :idU")
    , @NamedQuery(name = "Agenda.findByIdC", query = "SELECT a FROM Agenda a WHERE a.agendaPK.idC = :idC")
    , @NamedQuery(name = "Agenda.findByIdSAndIdU", query = "SELECT a FROM Agenda a WHERE a.agendaPK.idS = :idS AND a.agendaPK.idU = :idU")
})
public class Agenda implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AgendaPK agendaPK;
    @JoinColumns({
        @JoinColumn(name = "idS", referencedColumnName = "id", insertable = false, updatable = false)
        , @JoinColumn(name = "idC", referencedColumnName = "idC", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Session session;
    @JoinColumn(name = "idU", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "agenda")
    private Impression impression;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agenda")
    private Collection<Mark> markCollection;

    public Agenda() {
    }

    public Agenda(AgendaPK agendaPK) {
        this.agendaPK = agendaPK;
    }

    public Agenda(int idS, int idU, int idC) {
        this.agendaPK = new AgendaPK(idS, idU, idC);
    }

    public AgendaPK getAgendaPK() {
        return agendaPK;
    }

    public void setAgendaPK(AgendaPK agendaPK) {
        this.agendaPK = agendaPK;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Impression getImpression() {
        return impression;
    }

    public void setImpression(Impression impression) {
        this.impression = impression;
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
        hash += (agendaPK != null ? agendaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Agenda)) {
            return false;
        }
        Agenda other = (Agenda) object;
        if ((this.agendaPK == null && other.agendaPK != null) || (this.agendaPK != null && !this.agendaPK.equals(other.agendaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.Agenda[ agendaPK=" + agendaPK + " ]";
    }
    
}
