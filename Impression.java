/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author djordjebozic
 */
@Entity
@Table(name = "impression")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Impression.findAll", query = "SELECT i FROM Impression i")
    , @NamedQuery(name = "Impression.findByLke", query = "SELECT i FROM Impression i WHERE i.lke = :lke")
    , @NamedQuery(name = "Impression.findByCmment", query = "SELECT i FROM Impression i WHERE i.cmment = :cmment")
    , @NamedQuery(name = "Impression.findByIdS", query = "SELECT i FROM Impression i WHERE i.impressionPK.idS = :idS")
    , @NamedQuery(name = "Impression.findByIdC", query = "SELECT i FROM Impression i WHERE i.impressionPK.idC = :idC")
    , @NamedQuery(name = "Impression.findByIdU", query = "SELECT i FROM Impression i WHERE i.impressionPK.idU = :idU")
    , @NamedQuery(name = "Impression.countLikes", query = "SELECT i FROM Impression i WHERE i.impressionPK.idU = :idU AND i.impressionPK.idS = :idS"
            + " AND i.lke = 1")
})
public class Impression implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ImpressionPK impressionPK;
    @Column(name = "lke")
    private Integer lke;
    @Size(max = 1000)
    @Column(name = "cmment")
    private String cmment;
    @JoinColumns({
        @JoinColumn(name = "idS", referencedColumnName = "idS", insertable = false, updatable = false)
        , @JoinColumn(name = "idC", referencedColumnName = "idC", insertable = false, updatable = false)
        , @JoinColumn(name = "idU", referencedColumnName = "idU", insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private Agenda agenda;

    public Impression() {
    }

    public Impression(ImpressionPK impressionPK) {
        this.impressionPK = impressionPK;
    }

    public Impression(int idS, int idC, int idU) {
        this.impressionPK = new ImpressionPK(idS, idC, idU);
    }

    public ImpressionPK getImpressionPK() {
        return impressionPK;
    }

    public void setImpressionPK(ImpressionPK impressionPK) {
        this.impressionPK = impressionPK;
    }

    public Integer getLke() {
        return lke;
    }

    public void setLke(Integer lke) {
        this.lke = lke;
    }

    public String getCmment() {
        return cmment;
    }

    public void setCmment(String cmment) {
        this.cmment = cmment;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (impressionPK != null ? impressionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Impression)) {
            return false;
        }
        Impression other = (Impression) object;
        if ((this.impressionPK == null && other.impressionPK != null) || (this.impressionPK != null && !this.impressionPK.equals(other.impressionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.Impression[ impressionPK=" + impressionPK + " ]";
    }
    
}
