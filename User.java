/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import controllers.UserController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author djordjebozic
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
    , @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id")
    , @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name")
    , @NamedQuery(name = "User.findBySurname", query = "SELECT u FROM User u WHERE u.surname = :surname")
    , @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
    , @NamedQuery(name = "User.findByInstitution", query = "SELECT u FROM User u WHERE u.institution = :institution")
    , @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
    , @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password")
    , @NamedQuery(name = "User.findByGender", query = "SELECT u FROM User u WHERE u.gender = :gender")
    , @NamedQuery(name = "User.findByProfileImage", query = "SELECT u FROM User u WHERE u.profileImage = :profileImage")
    , @NamedQuery(name = "User.findByTShirtSize", query = "SELECT u FROM User u WHERE u.tShirtSize = :tShirtSize")
    , @NamedQuery(name = "User.findByLinkedInprofile", query = "SELECT u FROM User u WHERE u.linkedInprofile = :linkedInprofile")
    , @NamedQuery(name = "User.findByUsernameAndPassword", query = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
})
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "surname")
    private String surname;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "institution")
    private String institution;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Column(name = "gender")
    private Character gender;
    @Size(max = 300)
    @Column(name = "profile_image")
    private String profileImage;
    @Size(max = 3)
    @Column(name = "t_shirt_size")
    private String tShirtSize;
    @Size(max = 300)
    @Column(name = "linkedIn_profile")
    private String linkedInprofile;
    @JoinTable(name = "contacts", joinColumns = {
        @JoinColumn(name = "idSender", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "idRecipient", referencedColumnName = "id")})
    @ManyToMany
    private Collection<User> userCollection;
    @ManyToMany(mappedBy = "userCollection")
    private Collection<User> userCollection1;
    @OneToMany(mappedBy = "idU")
    private Collection<Author> authorCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Moderator moderator;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<Message> messageCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user1")
    private Collection<Message> messageCollection1;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<Agenda> agendaCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Administrator administrator;
    
    
    public User() {
        profileImage = "";
    }
    
    public User(Integer id) {
        this.id = id;
    }
    
    public User(Integer id, String name, String surname, String email, String institution, String username, String password, Character gender) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.institution = institution;
        this.username = username;
        this.password = password;
        this.gender = gender;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSurname() {
        return surname;
    }
    
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getInstitution() {
        return institution;
    }
    
    public void setInstitution(String institution) {
        this.institution = institution;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Character getGender() {
        return gender;
    }
    
    public void setGender(Character gender) {
        this.gender = gender;
    }
    
    public String getProfileImage() {
        return profileImage;
    }
    
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    
    public String getTShirtSize() {
        return tShirtSize;
    }
    
    public void setTShirtSize(String tShirtSize) {
        this.tShirtSize = tShirtSize;
    }
    
    public String getLinkedInprofile() {
        return linkedInprofile;
    }
    
    public void setLinkedInprofile(String linkedInprofile) {
        this.linkedInprofile = linkedInprofile;
    }
    
    @XmlTransient
    public Collection<User> getUserCollection() {
        return userCollection;
    }
    
    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }
    
    @XmlTransient
    public Collection<User> getUserCollection1() {
        return userCollection1;
    }
    
    public void setUserCollection1(Collection<User> userCollection1) {
        this.userCollection1 = userCollection1;
    }
    
    @XmlTransient
    public Collection<Author> getAuthorCollection() {
        return authorCollection;
    }
    
    public void setAuthorCollection(Collection<Author> authorCollection) {
        this.authorCollection = authorCollection;
    }
    
    public Moderator getModerator() {
        return moderator;
    }
    
    public void setModerator(Moderator moderator) {
        this.moderator = moderator;
    }
    
    @XmlTransient
    public Collection<Message> getMessageCollection() {
        return messageCollection;
    }
    
    public void setMessageCollection(Collection<Message> messageCollection) {
        this.messageCollection = messageCollection;
    }
    
    @XmlTransient
    public Collection<Message> getMessageCollection1() {
        return messageCollection1;
    }
    
    public void setMessageCollection1(Collection<Message> messageCollection1) {
        this.messageCollection1 = messageCollection1;
    }
    
    @XmlTransient
    public Collection<Agenda> getAgendaCollection() {
        return agendaCollection;
    }
    
    public void setAgendaCollection(Collection<Agenda> agendaCollection) {
        this.agendaCollection = agendaCollection;
    }
    
    public Administrator getAdministrator() {
        return administrator;
    }
    
    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "beans.User[ id=" + id + " ]";
    }
    
   
    
   
    
}
