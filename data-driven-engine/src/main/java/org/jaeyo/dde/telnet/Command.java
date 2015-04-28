package org.jaeyo.dde.telnet;

import java.io.BufferedReader;
import java.io.PrintWriter;

public abstract class Command {
	protected BufferedReader input;
	protected PrintWriter output;

	public abstract void execute(String[] cmds);
	
	public BufferedReader getInput() {
		return input;
	}

	public void setInput(BufferedReader input) {
		this.input = input;
	}

	public PrintWriter getOutput() {
		return output;
	}

	public void setOutput(PrintWriter output) {
		this.output = output;
	}
	
	protected void print(String msg){
		output.print(msg);
		output.flush();
	} //print

	protected void println(String msg){
		output.println(msg);
		output.flush();
	} //print

	protected void println(String msg, Object... args){
		println(String.format(msg, args));
	} //print
} //class