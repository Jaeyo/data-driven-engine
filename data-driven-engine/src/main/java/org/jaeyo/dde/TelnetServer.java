package org.jaeyo.dde;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.jaeyo.dde.common.Conf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TelnetServer extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(TelnetServer.class);
	private BufferedReader input;
	
	public TelnetServer(Socket socket) {
		try {
			this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
		} //catch
	} //INIT
	
	@Override
	public void run() {
		for(;;){
			try {
				String cmd = input.readLine();
				if(cmd.trim().equalsIgnoreCase("exit")){
					break;
				} else{
					TODO
				} //if
			} catch (IOException e) {
				logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
			} //catch
		} //for ;;
	} //run

	public static void startServer() {
		new Thread(){
			@Override
			public void run() {
				try {
					int port = Conf.getAs(Conf.TELNET_PORT);
					ServerSocket server = new ServerSocket(port);
					
					for(;;)
						new TelnetServer(server.accept()).start();
				} catch (IOException e) {
					logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
				} //catch
			} //run
		}.start();
	} // start
	
} // class