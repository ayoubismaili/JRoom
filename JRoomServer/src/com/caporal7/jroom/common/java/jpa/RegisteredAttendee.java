/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caporal7.jroom.common.java.jpa;

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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Ayoub Ismaili <ayoubismaili1@gmail.com>
 */
@Entity
@Table(name = "registered_attendee")
@NamedQueries({
    @NamedQuery(name = "RegisteredAttendee.findAll", query = "SELECT r FROM RegisteredAttendee r"),
    @NamedQuery(name = "RegisteredAttendee.findById", query = "SELECT r FROM RegisteredAttendee r WHERE r.id = :id"),
    @NamedQuery(name = "RegisteredAttendee.findByUsername", query = "SELECT r FROM RegisteredAttendee r WHERE r.username = :username"),
    @NamedQuery(name = "RegisteredAttendee.findByPassword", query = "SELECT r FROM RegisteredAttendee r WHERE r.password = :password"),
    @NamedQuery(name = "RegisteredAttendee.findByEmail", query = "SELECT r FROM RegisteredAttendee r WHERE r.email = :email"),
    @NamedQuery(name = "RegisteredAttendee.findByName", query = "SELECT r FROM RegisteredAttendee r WHERE r.name = :name"),
    @NamedQuery(name = "RegisteredAttendee.findByLastKnownIp", query = "SELECT r FROM RegisteredAttendee r WHERE r.lastKnownIp = :lastKnownIp"),
    @NamedQuery(name = "RegisteredAttendee.findByLastKnownClient", query = "SELECT r FROM RegisteredAttendee r WHERE r.lastKnownClient = :lastKnownClient"),
    @NamedQuery(name = "RegisteredAttendee.findByLastActivityDate", query = "SELECT r FROM RegisteredAttendee r WHERE r.lastActivityDate = :lastActivityDate")})
public class RegisteredAttendee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "last_known_ip")
    private String lastKnownIp;
    @Column(name = "last_known_client")
    private String lastKnownClient;
    @Basic(optional = false)
    @Column(name = "last_activity_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActivityDate;
    @OneToMany(mappedBy = "ownerId")
    private Collection<Conference> conferenceCollection;
    @OneToMany(mappedBy = "registeredAnimatorId")
    private Collection<Conference> conferenceCollection1;
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Avatar avatarId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registeredAttendeeId")
    private Collection<Session> sessionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registeredAttendee")
    private Collection<Attendance> attendanceCollection;

    public RegisteredAttendee() {
    }

    public RegisteredAttendee(Integer id) {
        this.id = id;
    }

    public RegisteredAttendee(Integer id, String username, String password, String email, String name, Date lastActivityDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.lastActivityDate = lastActivityDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastKnownIp() {
        return lastKnownIp;
    }

    public void setLastKnownIp(String lastKnownIp) {
        this.lastKnownIp = lastKnownIp;
    }

    public String getLastKnownClient() {
        return lastKnownClient;
    }

    public void setLastKnownClient(String lastKnownClient) {
        this.lastKnownClient = lastKnownClient;
    }

    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public Collection<Conference> getConferenceCollection() {
        return conferenceCollection;
    }

    public void setConferenceCollection(Collection<Conference> conferenceCollection) {
        this.conferenceCollection = conferenceCollection;
    }

    public Collection<Conference> getConferenceCollection1() {
        return conferenceCollection1;
    }

    public void setConferenceCollection1(Collection<Conference> conferenceCollection1) {
        this.conferenceCollection1 = conferenceCollection1;
    }

    public Avatar getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Avatar avatarId) {
        this.avatarId = avatarId;
    }

    public Collection<Session> getSessionCollection() {
        return sessionCollection;
    }

    public void setSessionCollection(Collection<Session> sessionCollection) {
        this.sessionCollection = sessionCollection;
    }

    public Collection<Attendance> getAttendanceCollection() {
        return attendanceCollection;
    }

    public void setAttendanceCollection(Collection<Attendance> attendanceCollection) {
        this.attendanceCollection = attendanceCollection;
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
        if (!(object instanceof RegisteredAttendee)) {
            return false;
        }
        RegisteredAttendee other = (RegisteredAttendee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.caporal7.jroom.common.java.jpa.RegisteredAttendee[ id=" + id + " ]";
    }
    
}
