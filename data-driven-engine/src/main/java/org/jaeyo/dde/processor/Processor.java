package org.jaeyo.dde.processor;

import java.util.Properties;
import java.util.UUID;

import org.jaeyo.dde.exception.AlreadyStartedException;
import org.jaeyo.dde.exception.AlreadyStoppedException;

abstract class Processor {
	private Thread worker;
	private boolean isRunning = false;
	private UUID id;

	public Processor(UUID id) {
		this.id = id;
	} //INIT

	public synchronized void start() throws AlreadyStartedException{
		if(isRunning == true)
			throw new AlreadyStartedException();

		beforeStart();
		worker = new Thread(){
			@Override
			public void run() {
				job();
			} //run
		};
		worker.start();
		isRunning = true;
	} //start

	public synchronized void stop() throws AlreadyStoppedException{
		if(isRunning == false)
			throw new AlreadyStoppedException();

		beforeStop();
		worker.interrupt();
		isRunning = false;
	} //stop

	public UUID getId(){
		return this.id;
	} //getId
	
	public abstract void job();
	public abstract void beforeStart();
	public abstract void beforeStop();
	public abstract void config(Properties config);
} //interface