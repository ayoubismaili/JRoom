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
import javax.persistence.Id;
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
