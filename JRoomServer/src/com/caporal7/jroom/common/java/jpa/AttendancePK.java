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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author JavaDev1
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
