package com.rsvp.hml;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Component
class RsvpsWebSocketHandler extends AbstractWebSocketHandler {

    private static final Logger logger = Logger.getLogger(RsvpsWebSocketHandler.class.getName());
    private static final String MEETUP_RSVPS_ENDPOINT = "ws://stream.meetup.com/2/rsvps";

    private final HmlHandler hmlHandler;

    public RsvpsWebSocketHandler(HmlHandler hmlHandler) {
        this.hmlHandler = hmlHandler;
    }

    public void doHandshake() {
        WebSocketClient rsvpsSocketClient = new StandardWebSocketClient();

        rsvpsSocketClient.doHandshake(
                this, MEETUP_RSVPS_ENDPOINT);
    }

    @Override
    public void handleMessage(WebSocketSession session,
            WebSocketMessage<?> message) {
        logger.log(Level.INFO, "New RSVP:\n {0}", message.getPayload());

        hmlHandler.insertRsvps((String) message.getPayload());
    }
}