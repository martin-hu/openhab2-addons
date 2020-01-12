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

import static org.openhab.binding.luxtronik1.internal.Luxtronik1BindingConstants.*;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.luxtronik1.internal.handler.Luxtronik1Handler;
import org.openhab.binding.luxtronik1.internal.lux.Lux1Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link Luxtronik1HandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Martin Hubert - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.luxtronik1", service = ThingHandlerFactory.class)
public class Luxtronik1HandlerFactory extends BaseThingHandlerFactory {

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(THING_TYPE_SERIAL_LUX1);

    private final Logger logger = LoggerFactory.getLogger(Luxtronik1HandlerFactory.class);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        logger.info("supportThings? {}", thingTypeUID);
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        logger.info("createHandler? {}", thingTypeUID);

        if (THING_TYPE_SERIAL_LUX1.equals(thingTypeUID)) {
            logger.info("createHandler type  THING_TYPE_SERIAL_LUX1");
            return new Luxtronik1Handler(thing);
        }

        return null;
    }
}
