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
public class LecturePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idS")
    private int idS;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idC")
    private int idC;

    public LecturePK() {
    }

    public LecturePK(int id, int idS, int idC) {
        this.id = id;
        this.idS = idS;
        this.idC = idC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdS() {
        return idS;
    }

    public void setIdS(int idS) {
        this.idS = idS;
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
        hash += (int) idS;
        hash += (int) idC;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LecturePK)) {
            return false;
        }
        LecturePK other = (LecturePK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.idS != other.idS) {
            return false;
        }
        if (this.idC != other.idC) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.LecturePK[ id=" + id + ", idS=" + idS + ", idC=" + idC + " ]";
    }
    
}
