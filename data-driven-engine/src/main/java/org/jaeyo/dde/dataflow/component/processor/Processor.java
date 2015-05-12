package org.jaeyo.dde.dataflow.component.processor;

import java.util.UUID;

import org.jaeyo.dde.dataflow.component.Component;

public abstract class Processor extends Component implements Runnable{
	private UUID id;
	private Thread worker;

	public Processor(UUID id, int x, int y, String name) {
		super(x, y, name);
		this.id = id;
	} //INIT

	public UUID getId(){
		return this.id;
	} //getId
	
	@Override
	public void run() {
		this.worker = Thread.currentThread();
		try{
			onStart();
		} catch(Exception e){
		} //catch
	} //run
	
	public void stop(){
		onStop();
		this.worker.interrupt();
	} //stop

	public abstract void onStart();
	public abstract void onStop();
} //interface