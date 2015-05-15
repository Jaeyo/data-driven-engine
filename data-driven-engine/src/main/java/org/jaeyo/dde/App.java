package org.jaeyo.dde;

import org.jaeyo.dde.common.Conf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) throws InterruptedException {
		logger.info("started");
		
		Conf.set(Conf.PORT, 1234);
		Conf.set(Conf.TELNET_PORT, 1212);
		
		JettyServer jetty = new JettyServer();
		jetty.start();
		jetty.join();
	} //main
} //class
