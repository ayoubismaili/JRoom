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
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "attendance")
@NamedQueries({
    @NamedQuery(name = "Attendance.findAll", query = "SELECT a FROM Attendance a"),
    @NamedQuery(name = "Attendance.findByConferenceId", query = "SELECT a FROM Attendance a WHERE a.attendancePK.conferenceId = :conferenceId"),
    @NamedQuery(name = "Attendance.findByGuestAttendeeGuid", query = "SELECT a FROM Attendance a WHERE a.attendancePK.guestAttendeeGuid = :guestAttendeeGuid"),
    @NamedQuery(name = "Attendance.findByRegisteredAttendeeId", query = "SELECT a FROM Attendance a WHERE a.attendancePK.registeredAttendeeId = :registeredAttendeeId"),
    @NamedQuery(name = "Attendance.findByLastActivityDate", query = "SELECT a FROM Attendance a WHERE a.lastActivityDate = :lastActivityDate"),
    @NamedQuery(name = "Attendance.findByAccessToken", query = "SELECT a FROM Attendance a WHERE a.accessToken = :accessToken"),
    @NamedQuery(name = "Attendance.findByVoiceStreamId", query = "SELECT a FROM Attendance a WHERE a.voiceStreamId = :voiceStreamId"),
    @NamedQuery(name = "Attendance.findByCameraStreamId", query = "SELECT a FROM Attendance a WHERE a.cameraStreamId = :cameraStreamId"),
    @NamedQuery(name = "Attendance.findByScreenStreamId", query = "SELECT a FROM Attendance a WHERE a.screenStreamId = :screenStreamId")})
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AttendancePK attendancePK;
    @Basic(optional = false)
    @Column(name = "last_activity_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActivityDate;
    @Basic(optional = false)
    @Column(name = "access_token")
    private String accessToken;
    @Column(name = "voice_stream_id")
    private Integer voiceStreamId;
    @Column(name = "camera_stream_id")
    private Integer cameraStreamId;
    @Column(name = "screen_stream_id")
    private Integer screenStreamId;
    @OneToMany(mappedBy = "attendance")
    private Collection<DataStream> dataStreamCollection;
    @JoinColumn(name = "conference_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Conference conference;
    @JoinColumn(name = "guest_attendee_guid", referencedColumnName = "guid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private GuestAttendee guestAttendee;
    @JoinColumn(name = "registered_attendee_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private RegisteredAttendee registeredAttendee;

    public Attendance() {
    }

    public Attendance(AttendancePK attendancePK) {
        this.attendancePK = attendancePK;
    }

    public Attendance(AttendancePK attendancePK, Date lastActivityDate, String accessToken) {
        this.attendancePK = attendancePK;
        this.lastActivityDate = lastActivityDate;
        this.accessToken = accessToken;
    }

    public Attendance(int conferenceId, String guestAttendeeGuid, int registeredAttendeeId) {
        this.attendancePK = new AttendancePK(conferenceId, guestAttendeeGuid, registeredAttendeeId);
    }

    public AttendancePK getAttendancePK() {
        return attendancePK;
    }

    public void setAttendancePK(AttendancePK attendancePK) {
        this.attendancePK = attendancePK;
    }

    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getVoiceStreamId() {
        return voiceStreamId;
    }

    public void setVoiceStreamId(Integer voiceStreamId) {
        this.voiceStreamId = voiceStreamId;
    }

    public Integer getCameraStreamId() {
        return cameraStreamId;
    }

    public void setCameraStreamId(Integer cameraStreamId) {
        this.cameraStreamId = cameraStreamId;
    }

    public Integer getScreenStreamId() {
        return screenStreamId;
    }

    public void setScreenStreamId(Integer screenStreamId) {
        this.screenStreamId = screenStreamId;
    }

    public Collection<DataStream> getDataStreamCollection() {
        return dataStreamCollection;
    }

    public void setDataStreamCollection(Collection<DataStream> dataStreamCollection) {
        this.dataStreamCollection = dataStreamCollection;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public GuestAttendee getGuestAttendee() {
        return guestAttendee;
    }

    public void setGuestAttendee(GuestAttendee guestAttendee) {
        this.guestAttendee = guestAttendee;
    }

    public RegisteredAttendee getRegisteredAttendee() {
        return registeredAttendee;
    }

    public void setRegisteredAttendee(RegisteredAttendee registeredAttendee) {
        this.registeredAttendee = registeredAttendee;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (attendancePK != null ? attendancePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Attendance)) {
            return false;
        }
        Attendance other = (Attendance) object;
        if ((this.attendancePK == null && other.attendancePK != null) || (this.attendancePK != null && !this.attendancePK.equals(other.attendancePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.caporal7.jroom.common.java.jpa.Attendance[ attendancePK=" + attendancePK + " ]";
    }
    
}
