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
package org.openhab.binding.luxtronik1.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link Luxtronik1BindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Martin Hubert - Initial contribution
 */
@NonNullByDefault
public class Luxtronik1BindingConstants {

    private static final String BINDING_ID = "luxtronik1";

    // List of all Thing Type UIDs

    // List of all Channel ids
    public static final ThingTypeUID THING_TYPE_SERIAL_LUX1 = new ThingTypeUID(BINDING_ID, "serialLux1");

    public static final String CHANNEL_GROUP_DEVICE_VALUES = "deviceValues#";

    // Serial thing
    public static final String PORT_NAME = "portName";

}
