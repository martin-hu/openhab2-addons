/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.luxtronik1.internal.protocol;

import gnu.io.NRSerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The {@link SerialLuxtronik1Connection} is responsible for creating serial connections to clients.
 *
 * @author Boris Krivonog - Initial contribution
 */
public class SerialLuxtronik1Connection implements Luxtronik1Connection {
    private final Logger logger = LoggerFactory.getLogger(SerialLuxtronik1Connection.class);
    private final int baudRate;
    private final String portName;
    private NRSerialPort serialPort;

    public SerialLuxtronik1Connection(String portName, int baudRate) {
        this.portName = portName;
        this.baudRate = baudRate;
    }

    @Override
    public void connect() throws IOException {
        if (isPortNameExist(portName)) {
            serialPort = new NRSerialPort(portName, baudRate);
            if (!serialPort.connect()) {
                throw new IOException("Failed to connect on port " + portName);
            }

            logger.debug("Connected to {}", portName);
        } else {
            throw new IOException("Serial port with name " + portName + " does not exist. Available port names: "
                    + NRSerialPort.getAvailableSerialPorts());
        }
    }

    @Override
    public boolean isConnected() {
        return serialPort != null && serialPort.isConnected();
    }

    @Override
    public void close() {
        if (serialPort != null) {
            serialPort.disconnect();
            serialPort = null;
        }
    }

    @Override
    public OutputStream outputStream() throws IOException {
        return serialPort.getOutputStream();
    }

    @Override
    public InputStream inputStream() throws IOException {
        return serialPort.getInputStream();
    }

    private boolean isPortNameExist(String portName) {
        return NRSerialPort.getAvailableSerialPorts().contains(portName);
    }
}
