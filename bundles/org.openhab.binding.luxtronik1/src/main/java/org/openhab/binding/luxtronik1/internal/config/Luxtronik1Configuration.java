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
package org.openhab.binding.luxtronik1.internal.config;

/**
 * The {@link Luxtronik1Configuration} class contains fields mapping thing configuration parameters.
 *
 * @author Martin Hubert - Initial contribution
 */
public class Luxtronik1Configuration {

    /**
     * Sample configuration parameter. Replace with your own.
     */
    public String portName;
    public int refreshInterval;
    //public boolean enableReadCommands;
    //public boolean enableWriteCommands;

    public String toString() {
        return "Luxtronik1Configuration: portName=" + portName +
                ", refreshInterval=" + refreshInterval; // +
                //", enableReadCommands=" + enableReadCommands +
                //", enableWriteCommands=" + enableWriteCommands;
    }
}
