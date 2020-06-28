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

/**
 *
 * @author Ayoub Ismaili
 */
public class JRoomAttendance {
    private int registeredAttendeeId;
    private String guestAttendeeGuid;
    private String accessToken;
    private JRoomDataStream voiceStream;
    private JRoomDataStream cameraStream;
    private JRoomDataStream screenStream;
    
    public JRoomAttendance(int registeredAttendeeId, String guestAttendeeGuid, 
            String accessToken, int voiceStreamId, int cameraStreamId, 
            int screenStreamId) {
        this.registeredAttendeeId = registeredAttendeeId;
        this.guestAttendeeGuid = guestAttendeeGuid;
        this.accessToken = accessToken;
        this.voiceStream = new JRoomDataStream(voiceStreamId);
        this.cameraStream = new JRoomDataStream(cameraStreamId);
        this.screenStream = new JRoomDataStream(screenStreamId);
    }

    /**
     * @return the guestAttendeeGuid
     */
    public String getGuestAttendeeGuid() {
        return guestAttendeeGuid;
    }

    /**
     * @param guestAttendeeGuid the guestAttendeeGuid to set
     */
    public void setGuestAttendeeGuid(String guestAttendeeGuid) {
        this.guestAttendeeGuid = guestAttendeeGuid;
    }

    /**
     * @return the registeredAttendeeId
     */
    public int getRegisteredAttendeeId() {
        return registeredAttendeeId;
    }

    /**
     * @param registeredAttendeeId the registeredAttendeeId to set
     */
    public void setRegisteredAttendeeId(int registeredAttendeeId) {
        this.registeredAttendeeId = registeredAttendeeId;
    }

    /**
     * @return the accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @param accessToken the accessToken to set
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * @return the voiceStream
     */
    public JRoomDataStream getVoiceStream() {
        return voiceStream;
    }

    /**
     * @param voiceStream the voiceStream to set
     */
    public void setVoiceStream(JRoomDataStream voiceStream) {
        this.voiceStream = voiceStream;
    }

    /**
     * @return the cameraStream
     */
    public JRoomDataStream getCameraStream() {
        return cameraStream;
    }

    /**
     * @param cameraStream the cameraStream to set
     */
    public void setCameraStream(JRoomDataStream cameraStream) {
        this.cameraStream = cameraStream;
    }

    /**
     * @return the screenStream
     */
    public JRoomDataStream getScreenStream() {
        return screenStream;
    }

    /**
     * @param screenStream the screenStream to set
     */
    public void setScreenStream(JRoomDataStream screenStream) {
        this.screenStream = screenStream;
    }
    
}
