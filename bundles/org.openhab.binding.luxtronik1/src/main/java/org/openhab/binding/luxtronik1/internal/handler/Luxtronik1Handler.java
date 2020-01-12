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
package org.openhab.binding.luxtronik1.internal.handler;

import static org.openhab.binding.luxtronik1.internal.Luxtronik1BindingConstants.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.luxtronik1.internal.Luxtronik1BindingConstants;
import org.openhab.binding.luxtronik1.internal.config.Luxtronik1Configuration;
import org.openhab.binding.luxtronik1.internal.lux.Lux1Constants;
import org.openhab.binding.luxtronik1.internal.lux.LuxRegisterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * The {@link Luxtronik1Handler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Martin Hubert - Initial contribution
 */
// @ NonNullByDefault
public class Luxtronik1Handler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(Luxtronik1Handler.class);

    private @Nullable Luxtronik1Configuration config;
    private LuxRegisterMapper mapper;
    private ScheduledFuture<?> scheduledRefreshFuture;
    private int refreshInterval;
    private final Map<String, ChannelDescriptor> channelDescriptors = new HashMap<>();
    private LuxConnection connection;

    private static final class ChannelDescriptor {
        private Date lastUpdate;
        private byte[] cachedValue;

        public byte[] cachedValueIfNotExpired(int refreshTime) {
            if (lastUpdate == null || (lastUpdate.getTime() + refreshTime * 900 < new Date().getTime())) {
                return null;
            }

            return cachedValue;
        }

        public void setValue(byte[] value) {
            lastUpdate = new Date();
            cachedValue = value;
        }
    }

    public Luxtronik1Handler(Thing thing) {
        super(thing);
        this.mapper = LuxRegisterMapper.REGO600;
    }

    // @ Override
    protected LuxConnection createConnection() {
        String portName = (String) getConfig().get(Luxtronik1BindingConstants.PORT_NAME);
        return new SerialRegoConnection(portName, 19200);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.info("Executing command '{}' for channel '{}'", command, channelUID.getId());
        if (command instanceof RefreshType) {
            // TODO: handle data refresh
            logger.info("Executing command '{}' for channel '{}'", command, channelUID.getId());
            processChannelReadRequest(channelUID.getId());

        } else {
            LuxRegisterMapper.Channel channel = mapper.map(channelUID.getId());
            if (channel != null) {
                logger.info("Executing command '{}' for channel '{}'", command, channelUID.getId());
                //processChannelWriteRequest(channel, command);
            } else {
                logger.info("Unsupported channel {}", channelUID.getId());
            }

        }

        // TODO: handle command

        // Note: if communication with thing fails for some reason,
        // indicate that by setting the status with detail information:
        // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
        // "Could not control device at IP address x.x.x.x");
    }

    private void processChannelReadRequest(String id) {
        logger.info("Executing processChannelReadRequest {}", id);
        try {
            // Send command to device and wait for response.
            if (!connection.isConnected()) {
                connection.connect();
            }

            // Give heat pump some time between commands. Feeding commands too quickly
            // might cause heat pump not to respond.
            //Thread.sleep(80);
            // Send command
            OutputStream outputStream = connection.outputStream();
            outputStream.write("1700\r".getBytes());
            outputStream.flush();

        } catch (IOException e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize() {
        logger.info("Executing initialize");

        // logger.debug("Start initializing!");
        config = getConfigAs(Luxtronik1Configuration.class);

        mapper = LuxRegisterMapper.REGO600;
        refreshInterval = ((Number) getConfig().get(Lux1Constants.REFRESH_INTERVAL)).intValue();

        connection = createConnection();

        scheduledRefreshFuture = scheduler.scheduleWithFixedDelay(this::refresh, 2, refreshInterval, TimeUnit.SECONDS);

        updateStatus(ThingStatus.UNKNOWN);

        logger.info("Finished initializing!");

    }

    @Override
    public void dispose() {
        super.dispose();

        if (connection != null) {
            connection.close();
        }

        if (scheduledRefreshFuture != null) {
            scheduledRefreshFuture.cancel(true);
            scheduledRefreshFuture = null;
        }

        synchronized (channelDescriptors) {
            channelDescriptors.clear();
        }

        connection = null;
        mapper = null;
    }

    private Collection<String> linkedChannels() {
        return thing.getChannels().stream().map(Channel::getUID).map(ChannelUID::getId).filter(this::isLinked)
                .collect(Collectors.toList());
    }


    private void refresh() {
        for (String channelIID : linkedChannels()) {
            if (Thread.interrupted()) {
                break;
            }

            processChannelReadRequest(channelIID);

            if (thing.getStatus() != ThingStatus.ONLINE) {
                break;
            }
        }
    }


}
