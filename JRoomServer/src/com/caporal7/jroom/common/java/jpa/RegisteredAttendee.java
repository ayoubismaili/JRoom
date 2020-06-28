/*
 * The MIT License
 *
 * Copyright 2020 JavaDev1.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/**
 *
 * @author JavaDev1
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
@PrimaryKey(validation = IdValidation.NULL)
public class RegisteredAttendee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Column(name = "name")
    private String name;
    @Column(name = "last_known_ip")
    private String lastKnownIp;
    @Column(name = "last_known_client")
    private String lastKnownClient;
    @Column(name = "last_activity_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActivityDate;
    @OneToMany(mappedBy = "ownerId")
    private Collection<Conference> conferenceCollection;
    @OneToMany(mappedBy = "registeredAnimatorId")
    private Collection<Conference> conferenceCollection1;
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    @ManyToOne
    private Avatar avatarId;
    @JoinColumn(name = "personal_conference_id", referencedColumnName = "id")
    @ManyToOne
    private Conference personalConferenceId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registeredAttendeeId")
    private Collection<Session> sessionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registeredAttendee")
    private Collection<Attendance> attendanceCollection;

    public RegisteredAttendee() {
    }

    public RegisteredAttendee(Integer id) {
        this.id = id;
    }

    public RegisteredAttendee(Integer id, String password, String email) {
        this.id = id;
        this.password = password;
        this.email = email;
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

    public Conference getPersonalConferenceId() {
        return personalConferenceId;
    }

    public void setPersonalConferenceId(Conference personalConferenceId) {
        this.personalConferenceId = personalConferenceId;
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
