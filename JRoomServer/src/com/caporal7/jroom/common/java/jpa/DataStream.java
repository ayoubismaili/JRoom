/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caporal7.jroom.common.java.jpa;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Ayoub Ismaili <ayoubismaili1@gmail.com>
 */
@Entity
@Table(name = "data_stream")
@NamedQueries({
    @NamedQuery(name = "DataStream.findAll", query = "SELECT d FROM DataStream d"),
    @NamedQuery(name = "DataStream.findById", query = "SELECT d FROM DataStream d WHERE d.id = :id"),
    @NamedQuery(name = "DataStream.findByType", query = "SELECT d FROM DataStream d WHERE d.type = :type"),
    @NamedQuery(name = "DataStream.findBySocketReferenceId", query = "SELECT d FROM DataStream d WHERE d.socketReferenceId = :socketReferenceId"),
    @NamedQuery(name = "DataStream.findByFormat", query = "SELECT d FROM DataStream d WHERE d.format = :format"),
    @NamedQuery(name = "DataStream.findByWidth", query = "SELECT d FROM DataStream d WHERE d.width = :width"),
    @NamedQuery(name = "DataStream.findByHeight", query = "SELECT d FROM DataStream d WHERE d.height = :height")})
public class DataStream implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "type")
    private int type;
    @Basic(optional = false)
    @Column(name = "socket_reference_id")
    private int socketReferenceId;
    @Column(name = "format")
    private String format;
    @Basic(optional = false)
    @Column(name = "width")
    private int width;
    @Basic(optional = false)
    @Column(name = "height")
    private int height;
    @JoinColumns({
        @JoinColumn(name = "conference_id", referencedColumnName = "conference_id"),
        @JoinColumn(name = "guest_attendee_guid", referencedColumnName = "guest_attendee_guid"),
        @JoinColumn(name = "registered_attendee_id", referencedColumnName = "registered_attendee_id")})
    @ManyToOne
    private Attendance attendance;

    public DataStream() {
    }

    public DataStream(Integer id) {
        this.id = id;
    }

    public DataStream(Integer id, int type, int socketReferenceId, int width, int height) {
        this.id = id;
        this.type = type;
        this.socketReferenceId = socketReferenceId;
        this.width = width;
        this.height = height;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSocketReferenceId() {
        return socketReferenceId;
    }

    public void setSocketReferenceId(int socketReferenceId) {
        this.socketReferenceId = socketReferenceId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
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
        if (!(object instanceof DataStream)) {
            return false;
        }
        DataStream other = (DataStream) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.caporal7.jroom.common.java.jpa.DataStream[ id=" + id + " ]";
    }
    
}
