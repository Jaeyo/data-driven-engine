package org.jaeyo.dde.telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.inject.Inject;

import org.jaeyo.dde.common.Conf;
import org.jaeyo.dde.service.DataFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TelnetServer {
	private static final Logger logger = LoggerFactory.getLogger(TelnetServer.class);
	@Inject private DataFlowService dataFlowService;
	@Inject private AddComponent addCompCmd;

	public TelnetServer() {
		new Thread(){
			@Override
			public void run() {
				int port = Conf.getAs(Conf.TELNET_PORT);
				try {
					ServerSocket server = new ServerSocket(port);
					new TelnetServerWorker(server.accept()).start();
				} catch (IOException e) {
					logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
				} //catch
			}
		}.start();
	} //INIT

	class TelnetServerWorker extends Thread{
		private BufferedReader input;
		private PrintWriter output;

		public TelnetServerWorker(Socket socket) {
			try {
				this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			} catch (IOException e) {
				logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
			} //catch
		} //INIT

		@Override
		public void run(){
			for(;;){
				try {
					print("# ");
					String cmd = input.readLine();
					if(cmd.trim().equalsIgnoreCase("exit")){
						input.close();
						output.close();
						return;
					} else{
						handleCmd(cmd);
					} //if
				} catch (IOException e) {
					logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
				} //catch
			} //for ;;
		} //run

		private void handleCmd(String cmd){
			if(cmd == null || cmd.trim().length() == 0)
				return;

			logger.info(cmd);
			String[] cmds = cmd.split(" ");
			switch(cmds[0]){
			case "map":{
				println(dataFlowService.getDataFlowMap().toString());
				break;
			} //case map
			case "addcomp":{
				addCompCmd.setInput(input);
				addCompCmd.setOutput(output);
				addCompCmd.execute(cmds);
				break;
			} //case addComp
			default :{
				println("wrong cmd : %s", cmd);
				break;
			} //default
			} //switch
		} //handleCmd

		private void print(String msg){
			output.print(msg);
			output.flush();
		} //print

		private void println(String msg){
			output.println(msg);
			output.flush();
		} //print

		private void println(String msg, Object... args){
			println(String.format(msg, args));
		} //print
	} //class
} // class