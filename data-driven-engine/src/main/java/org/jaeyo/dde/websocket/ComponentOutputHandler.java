package org.jaeyo.dde.websocket;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ComponentOutputHandler extends TextWebSocketHandler{
	private static final Logger logger = LoggerFactory.getLogger(ComponentOutputHandler.class);
	
	@Override
	protected void handleTextMessage(final WebSocketSession session, TextMessage message) throws Exception {
		logger.info("msg arrived : " + message.getPayload()); //DEBUG
		
		new Thread(){
			@Override
			public void run() {
				for(;;){
					try {
						session.sendMessage(new TextMessage(UUID.randomUUID().toString()));
					} catch (IOException e) {
						logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
					} //catch
				} //for ;;
			} //run
		}.start();
	} //handleTextMessage
	
} //class