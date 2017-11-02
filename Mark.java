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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author djordjebozic
 */
@Entity
@Table(name = "mark")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mark.findAll", query = "SELECT m FROM Mark m")
    , @NamedQuery(name = "Mark.findByValue", query = "SELECT m FROM Mark m WHERE m.value = :value")
    , @NamedQuery(name = "Mark.findByIdL", query = "SELECT m FROM Mark m WHERE m.markPK.idL = :idL")
    , @NamedQuery(name = "Mark.findByIdS", query = "SELECT m FROM Mark m WHERE m.markPK.idS = :idS")
    , @NamedQuery(name = "Mark.findByIdC", query = "SELECT m FROM Mark m WHERE m.markPK.idC = :idC")
    , @NamedQuery(name = "Mark.findByIdU", query = "SELECT m FROM Mark m WHERE m.markPK.idU = :idU")})
public class Mark implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MarkPK markPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "value")
    private int value;
    @JoinColumns({
        @JoinColumn(name = "idL", referencedColumnName = "id", insertable = false, updatable = false)
        , @JoinColumn(name = "idS", referencedColumnName = "idS", insertable = false, updatable = false)
        , @JoinColumn(name = "idC", referencedColumnName = "idC", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Lecture lecture;
    @JoinColumns({
        @JoinColumn(name = "idS", referencedColumnName = "idS", insertable = false, updatable = false)
        , @JoinColumn(name = "idC", referencedColumnName = "idC", insertable = false, updatable = false)
        , @JoinColumn(name = "idU", referencedColumnName = "idU", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Agenda agenda;

    public Mark() {
    }

    public Mark(MarkPK markPK) {
        this.markPK = markPK;
    }

    public Mark(MarkPK markPK, int value) {
        this.markPK = markPK;
        this.value = value;
    }

    public Mark(int idL, int idS, int idC, int idU) {
        this.markPK = new MarkPK(idL, idS, idC, idU);
    }

    public MarkPK getMarkPK() {
        return markPK;
    }

    public void setMarkPK(MarkPK markPK) {
        this.markPK = markPK;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
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
        hash += (markPK != null ? markPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mark)) {
            return false;
        }
        Mark other = (Mark) object;
        if ((this.markPK == null && other.markPK != null) || (this.markPK != null && !this.markPK.equals(other.markPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.Mark[ markPK=" + markPK + " ]";
    }
    
}
