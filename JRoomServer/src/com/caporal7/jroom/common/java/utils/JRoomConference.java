/*
 * The MIT License
 *
 * Copyright 2020 Ayoub Ismaili.
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
package com.caporal7.jroom.common.java.utils;

import java.util.ArrayList;

/**
 *
 * @author Ayoub Ismaili
 */
public class JRoomConference {
    private int id;
    private int password;
    private int ownerId;
    private int discussionId;
    private int registeredAnimatorId;
    private String guestAnimatorGuid;
    private JRoomAttendance animatorAttendance;
    private ArrayList<JRoomAttendance> attendances;
    
    public JRoomConference(int id, int password, int ownerId, int discussionId, 
            int registeredAnimatorId, String guestAnimatorGuid) {
        this.id = id;
        this.password = password;
        this.ownerId = ownerId;
        this.discussionId = discussionId;
        this.registeredAnimatorId = registeredAnimatorId;
        this.guestAnimatorGuid = guestAnimatorGuid;
        this.attendances = new ArrayList<>();
    }
    
    public void addAttendance(JRoomAttendance a) {
        this.attendances.add(a);
    }
    
    public JRoomAttendance findGuestAttendance(String guid) {
        for (JRoomAttendance a : attendances) {
            if (a.getGuestAttendeeGuid().equals(guid)) {
                return a;
            }
        }
        return null;
    }
    
    public JRoomAttendance findRegisteredAttendance(int id) {
        for (JRoomAttendance a : attendances) {
            if (a.getRegisteredAttendeeId() == id) {
                return a;
            }
        }
        return null;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the password
     */
    public int getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(int password) {
        this.password = password;
    }

    /**
     * @return the ownerId
     */
    public int getOwnerId() {
        return ownerId;
    }

    /**
     * @param ownerId the ownerId to set
     */
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * @return the discussionId
     */
    public int getDiscussionId() {
        return discussionId;
    }

    /**
     * @param discussionId the discussionId to set
     */
    public void setDiscussionId(int discussionId) {
        this.discussionId = discussionId;
    }

    /**
     * @return the registeredAnimatorId
     */
    public int getRegisteredAnimatorId() {
        return registeredAnimatorId;
    }

    /**
     * @param registeredAnimatorId the registeredAnimatorId to set
     */
    public void setRegisteredAnimatorId(int registeredAnimatorId) {
        this.registeredAnimatorId = registeredAnimatorId;
    }

    /**
     * @return the guestAnimatorGuid
     */
    public String getGuestAnimatorGuid() {
        return guestAnimatorGuid;
    }

    /**
     * @param guestAnimatorGuid the guestAnimatorGuid to set
     */
    public void setGuestAnimatorGuid(String guestAnimatorGuid) {
        this.guestAnimatorGuid = guestAnimatorGuid;
    }

    /**
     * @return the animatorAttendance
     */
    public JRoomAttendance getAnimatorAttendance() {
        return animatorAttendance;
    }

    /**
     * @param animatorAttendance the animatorAttendance to set
     */
    public void setAnimatorAttendance(JRoomAttendance animatorAttendance) {
        this.animatorAttendance = animatorAttendance;
    }
}
