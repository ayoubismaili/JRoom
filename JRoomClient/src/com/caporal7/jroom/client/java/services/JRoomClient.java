/*
 * The MIT License
 *
 * Copyright 2020 Ayoub Ismaili <ayoubismaili1@gmail.com>.
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

package com.caporal7.jroom.client.java.services;

import com.caporal7.jroom.common.java.JRoomException;
import com.caporal7.jroom.common.java.JRoomSettings;
import java.io.IOException;
import java.net.Socket;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class JRoomClient {
    
    private Socket socket;
    
    public JRoomClient() throws IOException, ConfigurationException, JRoomException {
        XMLConfiguration config = JRoomSettings.getSettings();
        boolean useSSL = config.getBoolean("use-ssl", JRoomSettings.DEFAULT_USE_SSL);
        if (useSSL) {
            /* TODO: implement SSL */
        } else {
            socket = new Socket(
                config.getString("host", JRoomSettings.DEFAULT_HOST), 
                config.getInt("port", JRoomSettings.DEFAULT_PORT)
            );
        }
    }
    
    public JRoomClient(String host, int port, boolean useSSL) throws IOException {
        if (useSSL) {
            /* TODO: implement SSL */
        } else {
            socket = new Socket(host, port);
        }
    }
    
    public byte[] readNBytes(int n) throws IOException {
        return socket.getInputStream().readNBytes(n);
    }
    
    public void write(byte[] data) throws IOException {
        socket.getOutputStream().write(data);
    }
    
    public void flush() throws IOException {
        socket.getOutputStream().flush();
    }
}
