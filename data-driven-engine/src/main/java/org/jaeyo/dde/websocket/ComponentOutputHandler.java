package org.jaeyo.dde.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ComponentOutputHandler extends TextWebSocketHandler{
	private static final Logger logger = LoggerFactory.getLogger(ComponentOutputHandler.class);

	@Override
	protected void handleTextMessage(final WebSocketSession session, TextMessage message) throws Exception {
		session.sendMessage(new TextMessage("echo :: " + message.getPayload()));
	} //handleTextMessage
} //class