/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author djordjebozic
 */
@Embeddable
public class SessionPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idC")
    private int idC;

    public SessionPK() {
    }

    public SessionPK(int id, int idC) {
        this.id = id;
        this.idC = idC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdC() {
        return idC;
    }

    public void setIdC(int idC) {
        this.idC = idC;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) idC;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SessionPK)) {
            return false;
        }
        SessionPK other = (SessionPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.idC != other.idC) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.SessionPK[ id=" + id + ", idC=" + idC + " ]";
    }
    
}
