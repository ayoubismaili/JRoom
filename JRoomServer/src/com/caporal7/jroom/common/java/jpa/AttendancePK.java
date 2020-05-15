/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caporal7.jroom.common.java.jpa;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Ayoub Ismaili <ayoubismaili1@gmail.com>
 */
@Embeddable
public class AttendancePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "conference_id")
    private int conferenceId;
    @Basic(optional = false)
    @Column(name = "guest_attendee_guid")
    private String guestAttendeeGuid;
    @Basic(optional = false)
    @Column(name = "registered_attendee_id")
    private int registeredAttendeeId;

    public AttendancePK() {
    }

    public AttendancePK(int conferenceId, String guestAttendeeGuid, int registeredAttendeeId) {
        this.conferenceId = conferenceId;
        this.guestAttendeeGuid = guestAttendeeGuid;
        this.registeredAttendeeId = registeredAttendeeId;
    }

    public int getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(int conferenceId) {
        this.conferenceId = conferenceId;
    }

    public String getGuestAttendeeGuid() {
        return guestAttendeeGuid;
    }

    public void setGuestAttendeeGuid(String guestAttendeeGuid) {
        this.guestAttendeeGuid = guestAttendeeGuid;
    }

    public int getRegisteredAttendeeId() {
        return registeredAttendeeId;
    }

    public void setRegisteredAttendeeId(int registeredAttendeeId) {
        this.registeredAttendeeId = registeredAttendeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) conferenceId;
        hash += (guestAttendeeGuid != null ? guestAttendeeGuid.hashCode() : 0);
        hash += (int) registeredAttendeeId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AttendancePK)) {
            return false;
        }
        AttendancePK other = (AttendancePK) object;
        if (this.conferenceId != other.conferenceId) {
            return false;
        }
        if ((this.guestAttendeeGuid == null && other.guestAttendeeGuid != null) || (this.guestAttendeeGuid != null && !this.guestAttendeeGuid.equals(other.guestAttendeeGuid))) {
            return false;
        }
        if (this.registeredAttendeeId != other.registeredAttendeeId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.caporal7.jroom.common.java.jpa.AttendancePK[ conferenceId=" + conferenceId + ", guestAttendeeGuid=" + guestAttendeeGuid + ", registeredAttendeeId=" + registeredAttendeeId + " ]";
    }
    
}
