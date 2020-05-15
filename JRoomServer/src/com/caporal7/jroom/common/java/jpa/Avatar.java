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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Ayoub Ismaili <ayoubismaili1@gmail.com>
 */
@Entity
@Table(name = "avatar")
@NamedQueries({
    @NamedQuery(name = "Avatar.findAll", query = "SELECT a FROM Avatar a"),
    @NamedQuery(name = "Avatar.findById", query = "SELECT a FROM Avatar a WHERE a.id = :id"),
    @NamedQuery(name = "Avatar.findByX", query = "SELECT a FROM Avatar a WHERE a.x = :x"),
    @NamedQuery(name = "Avatar.findByY", query = "SELECT a FROM Avatar a WHERE a.y = :y"),
    @NamedQuery(name = "Avatar.findByWidth", query = "SELECT a FROM Avatar a WHERE a.width = :width"),
    @NamedQuery(name = "Avatar.findByHeight", query = "SELECT a FROM Avatar a WHERE a.height = :height")})
public class Avatar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Column(name = "image_blob")
    private byte[] imageBlob;
    @Basic(optional = false)
    @Column(name = "x")
    private int x;
    @Basic(optional = false)
    @Column(name = "y")
    private int y;
    @Basic(optional = false)
    @Column(name = "width")
    private int width;
    @Basic(optional = false)
    @Column(name = "height")
    private int height;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "avatarId")
    private Collection<RegisteredAttendee> registeredAttendeeCollection;

    public Avatar() {
    }

    public Avatar(Integer id) {
        this.id = id;
    }

    public Avatar(Integer id, int x, int y, int width, int height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getImageBlob() {
        return imageBlob;
    }

    public void setImageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public Collection<RegisteredAttendee> getRegisteredAttendeeCollection() {
        return registeredAttendeeCollection;
    }

    public void setRegisteredAttendeeCollection(Collection<RegisteredAttendee> registeredAttendeeCollection) {
        this.registeredAttendeeCollection = registeredAttendeeCollection;
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
        if (!(object instanceof Avatar)) {
            return false;
        }
        Avatar other = (Avatar) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.caporal7.jroom.common.java.jpa.Avatar[ id=" + id + " ]";
    }
    
}
