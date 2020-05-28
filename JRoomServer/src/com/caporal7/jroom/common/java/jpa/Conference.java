/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caporal7.jroom.common.java.jpa;

import java.io.Serializable;
import java.util.Collection;
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

/**
 *
 * @author Ayoub Ismaili <ayoubismaili1@gmail.com>
 */
@Entity
@Table(name = "conference")
@NamedQueries({
    @NamedQuery(name = "Conference.findAll", query = "SELECT c FROM Conference c"),
    @NamedQuery(name = "Conference.findById", query = "SELECT c FROM Conference c WHERE c.id = :id"),
    @NamedQuery(name = "Conference.findByPassword", query = "SELECT c FROM Conference c WHERE c.password = :password"),
    @NamedQuery(name = "Conference.findByActive", query = "SELECT c FROM Conference c WHERE c.active = :active")})
public class Conference implements Serializable {

    @OneToMany(mappedBy = "personalConferenceId")
    private Collection<RegisteredAttendee> registeredAttendeeCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "password")
    private int password;
    @Basic(optional = false)
    @Column(name = "active")
    private boolean active;
    @JoinColumn(name = "discussion_id", referencedColumnName = "id")
    @ManyToOne
    private Discussion discussionId;
    @JoinColumn(name = "guest_animator_guid", referencedColumnName = "guid")
    @ManyToOne
    private GuestAttendee guestAnimatorGuid;
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @ManyToOne
    private RegisteredAttendee ownerId;
    @JoinColumn(name = "registered_animator_id", referencedColumnName = "id")
    @ManyToOne
    private RegisteredAttendee registeredAnimatorId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conference")
    private Collection<Attendance> attendanceCollection;

    public Conference() {
    }

    public Conference(Integer id) {
        this.id = id;
    }

    public Conference(Integer id, int password, boolean active) {
        this.id = id;
        this.password = password;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Discussion getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(Discussion discussionId) {
        this.discussionId = discussionId;
    }

    public GuestAttendee getGuestAnimatorGuid() {
        return guestAnimatorGuid;
    }

    public void setGuestAnimatorGuid(GuestAttendee guestAnimatorGuid) {
        this.guestAnimatorGuid = guestAnimatorGuid;
    }

    public RegisteredAttendee getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(RegisteredAttendee ownerId) {
        this.ownerId = ownerId;
    }

    public RegisteredAttendee getRegisteredAnimatorId() {
        return registeredAnimatorId;
    }

    public void setRegisteredAnimatorId(RegisteredAttendee registeredAnimatorId) {
        this.registeredAnimatorId = registeredAnimatorId;
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
        if (!(object instanceof Conference)) {
            return false;
        }
        Conference other = (Conference) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.caporal7.jroom.common.java.jpa.Conference[ id=" + id + " ]";
    }

    public Collection<RegisteredAttendee> getRegisteredAttendeeCollection() {
        return registeredAttendeeCollection;
    }

    public void setRegisteredAttendeeCollection(Collection<RegisteredAttendee> registeredAttendeeCollection) {
        this.registeredAttendeeCollection = registeredAttendeeCollection;
    }
    
}
