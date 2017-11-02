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
public class MessagePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idSender")
    private int idSender;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idRecipient")
    private int idRecipient;

    public MessagePK() {
    }

    public MessagePK(int id, int idSender, int idRecipient) {
        this.id = id;
        this.idSender = idSender;
        this.idRecipient = idRecipient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }

    public int getIdRecipient() {
        return idRecipient;
    }

    public void setIdRecipient(int idRecipient) {
        this.idRecipient = idRecipient;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) idSender;
        hash += (int) idRecipient;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MessagePK)) {
            return false;
        }
        MessagePK other = (MessagePK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.idSender != other.idSender) {
            return false;
        }
        if (this.idRecipient != other.idRecipient) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.MessagePK[ id=" + id + ", idSender=" + idSender + ", idRecipient=" + idRecipient + " ]";
    }
    
}
