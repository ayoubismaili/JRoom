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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author JavaDev1
 */
@Entity
@Table(name = "data_stream")
@NamedQueries({
    @NamedQuery(name = "DataStream.findAll", query = "SELECT d FROM DataStream d"),
    @NamedQuery(name = "DataStream.findById", query = "SELECT d FROM DataStream d WHERE d.id = :id"),
    @NamedQuery(name = "DataStream.findByType", query = "SELECT d FROM DataStream d WHERE d.type = :type"),
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
    @Column(name = "type")
    private Integer type;
    @Lob
    @Column(name = "data")
    private byte[] data;
    @Column(name = "format")
    private String format;
    @Column(name = "width")
    private Integer width;
    @Column(name = "height")
    private Integer height;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
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
