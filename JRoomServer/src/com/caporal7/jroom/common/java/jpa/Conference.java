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
 * @author JavaDev1
 */
@Entity
@Table(name = "conference")
@NamedQueries({
    @NamedQuery(name = "Conference.findAll", query = "SELECT c FROM Conference c"),
    @NamedQuery(name = "Conference.findById", query = "SELECT c FROM Conference c WHERE c.id = :id"),
    @NamedQuery(name = "Conference.findByPassword", query = "SELECT c FROM Conference c WHERE c.password = :password"),
    @NamedQuery(name = "Conference.findByActive", query = "SELECT c FROM Conference c WHERE c.active = :active")})
public class Conference implements Serializable {

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
    @OneToMany(mappedBy = "personalConferenceId")
    private Collection<RegisteredAttendee> registeredAttendeeCollection;
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

    public Collection<RegisteredAttendee> getRegisteredAttendeeCollection() {
        return registeredAttendeeCollection;
    }

    public void setRegisteredAttendeeCollection(Collection<RegisteredAttendee> registeredAttendeeCollection) {
        this.registeredAttendeeCollection = registeredAttendeeCollection;
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
    
}
