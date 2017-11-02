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
public class AgendaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "idS")
    private int idS;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idU")
    private int idU;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idC")
    private int idC;

    public AgendaPK() {
    }

    public AgendaPK(int idS, int idU, int idC) {
        this.idS = idS;
        this.idU = idU;
        this.idC = idC;
    }

    public int getIdS() {
        return idS;
    }

    public void setIdS(int idS) {
        this.idS = idS;
    }

    public int getIdU() {
        return idU;
    }

    public void setIdU(int idU) {
        this.idU = idU;
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
        hash += (int) idS;
        hash += (int) idU;
        hash += (int) idC;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AgendaPK)) {
            return false;
        }
        AgendaPK other = (AgendaPK) object;
        if (this.idS != other.idS) {
            return false;
        }
        if (this.idU != other.idU) {
            return false;
        }
        if (this.idC != other.idC) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.AgendaPK[ idS=" + idS + ", idU=" + idU + ", idC=" + idC + " ]";
    }
    
}
