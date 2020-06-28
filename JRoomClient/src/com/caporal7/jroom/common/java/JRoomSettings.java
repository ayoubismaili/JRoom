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

package com.caporal7.jroom.common.java;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class JRoomSettings {
    /* Global settings */
    public static final String VERSION = "0.1.0";
    /* Some default settings */
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final int DEFAULT_PORT = 8803;
    public static final boolean DEFAULT_USE_SSL = false;
    /* XML configuration file */
    public static String CONFIG_XML = "config.xml";
    
    private static XMLConfiguration xmlConfig = null;
    
    public static XMLConfiguration getSettings()
            throws IOException, ConfigurationException, JRoomException {
        if (xmlConfig == null) {
            xmlConfig = new XMLConfiguration();
            File xmlFile = new File(CONFIG_XML);
            if (xmlFile.isDirectory()) {
                throw new JRoomException("File " + CONFIG_XML + " must not be a directory");
            }
            if(!xmlFile.exists()) {
                xmlConfig.addProperty("host", DEFAULT_HOST);
                xmlConfig.addProperty("port", DEFAULT_PORT);
                xmlConfig.addProperty("use-ssl", DEFAULT_USE_SSL);
                saveSettings();
            }
            Parameters params = new Parameters();
            FileBasedConfigurationBuilder<XMLConfiguration> builder =
                new FileBasedConfigurationBuilder<>(XMLConfiguration.class)
                .configure(params.fileBased()
                    .setFile(xmlFile));
            builder.setAutoSave(true);
            xmlConfig = builder.getConfiguration();
            
            if(!xmlConfig.containsKey("host")){
                xmlConfig.addProperty("host", DEFAULT_HOST);
            }
            if(!xmlConfig.containsKey("port")){
                xmlConfig.addProperty("port", DEFAULT_PORT);
            }
            if(!xmlConfig.containsKey("use-ssl")){
                xmlConfig.addProperty("use-ssl", DEFAULT_USE_SSL);
            }
        }
        return xmlConfig;
    }
    
    public static void saveSettings()
            throws IOException, ConfigurationException {
        if (xmlConfig != null) {
            try (Writer writer = new FileWriter(CONFIG_XML)) {
                xmlConfig.write(writer);
            }
        }
    }
}

