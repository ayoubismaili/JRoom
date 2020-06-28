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

import com.caporal7.jroom.common.java.jpa.Attendance;
import com.caporal7.jroom.common.java.jpa.Conference;
import com.caporal7.jroom.common.java.jpa.Discussion;
import com.caporal7.jroom.common.java.jpa.GuestAttendee;
import com.caporal7.jroom.common.java.jpa.RegisteredAttendee;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Ayoub Ismaili
 */
public class JRoomConferenceManager {
    
    private static ArrayList<JRoomConference> conferences = new ArrayList<>();
    
    public static void load() {
        /* We will cache Conferences, Attendances to allow faster access, 
        because when we used direct database access we encountered too much
        problems related to JPA and slow access */
        EntityManager em = JRoomUtils.getEntityManager();
        TypedQuery<Conference> cQuery;
        cQuery = em.createQuery(
            "SELECT c FROM Conference AS c where c.active = :active",
            Conference.class);
        cQuery.setParameter("active", true);
        List<Conference> cResults = cQuery.getResultList();
        for (Conference c : cResults) {
            int confId = c.getId();
            int confPassword = c.getPassword();
            int confOwnerId = -1;
            RegisteredAttendee owner = c.getOwnerId();
            if (owner != null) {
                confOwnerId = owner.getId();
            }
            int confDiscussionId = -1;
            Discussion discussion = c.getDiscussionId();
            if (discussion != null) {
                confDiscussionId = discussion.getId();
            }
            int confRegisteredAnimatorId = -1;
            RegisteredAttendee registeredAnimator = c.getRegisteredAnimatorId();
            if (registeredAnimator != null) {
                confRegisteredAnimatorId = registeredAnimator.getId();
            }
            String confGuestAnimatorGuid = null;
            GuestAttendee guestAnimator = c.getGuestAnimatorGuid();
            if (guestAnimator != null) {
                confGuestAnimatorGuid = guestAnimator.getGuid();
            }
            JRoomConference jc = new JRoomConference(confId, confPassword, 
                    confOwnerId, confDiscussionId, confRegisteredAnimatorId, 
                    confGuestAnimatorGuid);
            TypedQuery<Attendance> aQuery;
            aQuery = em.createQuery(
                "SELECT a FROM Attendance AS a where a.conference.id = :confId",
                Attendance.class);
            aQuery.setParameter("confId", confId);
            List<Attendance> aResults = aQuery.getResultList();
            JRoomAttendance animatorAttendance = null;
            for (Attendance a : aResults) {
                int attRegisteredAttendeeId = -1;
                RegisteredAttendee attRegisteredAttendee = a.getRegisteredAttendee();
                if (attRegisteredAttendee != null) {
                    attRegisteredAttendeeId = attRegisteredAttendee.getId();
                }
                String attGuestAttendeeGuid = null;
                GuestAttendee attGuestAttendee = a.getGuestAttendee();
                if (attGuestAttendee != null) {
                    attGuestAttendeeGuid = attGuestAttendee.getGuid();
                }
                String accessToken = a.getAccessToken();
                int voiceStreamId = a.getVoiceStreamId();
                int cameraStreamId = a.getCameraStreamId();
                int screenStreamId = a.getScreenStreamId();
                JRoomAttendance ja = new JRoomAttendance(
                    attRegisteredAttendeeId, attGuestAttendeeGuid, accessToken,
                    voiceStreamId, cameraStreamId, screenStreamId);
                jc.addAttendance(ja);
                if (confOwnerId == attRegisteredAttendeeId)
                    animatorAttendance = ja;
            }
            jc.setAnimatorAttendance(animatorAttendance);
            conferences.add(jc);
        }
    }
    
    public static JRoomConference findConference(int id) {
        for (JRoomConference conf : conferences) {
            if (conf.getId() == id) {
                return conf;
            }
        }
        return null;
    }
    
    public static void addConference(JRoomConference conf) {
        conferences.add(conf);
    }
    
    public static void update() {
        //TODO: implement caching update
    }
    
}
