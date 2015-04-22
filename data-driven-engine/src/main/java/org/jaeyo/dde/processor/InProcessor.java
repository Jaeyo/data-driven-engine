package org.jaeyo.dde.processor;

import java.util.UUID;

import org.jaeyo.dde.connectionqueue.ConnectionQueue;
import org.jaeyo.dde.event.Event;

public abstract class InProcessor extends Processor implements Input{
	private InputConnectionGroup inputGroup; 
	
	public InProcessor(UUID id, InputConnectionGroup inputGroup) {
		super(id);
		this.inputGroup = inputGroup;
	}

	public void addInputConnection(ConnectionQueue conn){
		inputGroup.addInputConnection(conn);
	} //addInputConnection
	
	public void removeInputConnection(ConnectionQueue conn){
		inputGroup.removeInputConnection(conn);
	} //removeInputConnection
	
	@Override
	public void job() {
		for(;;)
			onEvent(inputGroup.take());
	} //job
	
	public abstract void onEvent(Event event);
} //class