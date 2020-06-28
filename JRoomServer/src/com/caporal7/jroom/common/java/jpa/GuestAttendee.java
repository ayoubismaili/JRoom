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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author JavaDev1
 */
@Entity
@Table(name = "guest_attendee")
@NamedQueries({
    @NamedQuery(name = "GuestAttendee.findAll", query = "SELECT g FROM GuestAttendee g"),
    @NamedQuery(name = "GuestAttendee.findByGuid", query = "SELECT g FROM GuestAttendee g WHERE g.guid = :guid"),
    @NamedQuery(name = "GuestAttendee.findByName", query = "SELECT g FROM GuestAttendee g WHERE g.name = :name"),
    @NamedQuery(name = "GuestAttendee.findByLastKnownIp", query = "SELECT g FROM GuestAttendee g WHERE g.lastKnownIp = :lastKnownIp"),
    @NamedQuery(name = "GuestAttendee.findByLastKnownClient", query = "SELECT g FROM GuestAttendee g WHERE g.lastKnownClient = :lastKnownClient"),
    @NamedQuery(name = "GuestAttendee.findByLastActivityDate", query = "SELECT g FROM GuestAttendee g WHERE g.lastActivityDate = :lastActivityDate")})
public class GuestAttendee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "guid")
    private String guid;
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
    @OneToMany(mappedBy = "guestAnimatorGuid")
    private Collection<Conference> conferenceCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "guestAttendee")
    private Collection<Attendance> attendanceCollection;

    public GuestAttendee() {
    }

    public GuestAttendee(String guid) {
        this.guid = guid;
    }

    public GuestAttendee(String guid, String name, Date lastActivityDate) {
        this.guid = guid;
        this.name = name;
        this.lastActivityDate = lastActivityDate;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
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

    public Collection<Attendance> getAttendanceCollection() {
        return attendanceCollection;
    }

    public void setAttendanceCollection(Collection<Attendance> attendanceCollection) {
        this.attendanceCollection = attendanceCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (guid != null ? guid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GuestAttendee)) {
            return false;
        }
        GuestAttendee other = (GuestAttendee) object;
        if ((this.guid == null && other.guid != null) || (this.guid != null && !this.guid.equals(other.guid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.caporal7.jroom.common.java.jpa.GuestAttendee[ guid=" + guid + " ]";
    }
    
}
