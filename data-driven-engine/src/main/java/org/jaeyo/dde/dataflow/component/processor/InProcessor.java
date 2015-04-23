package org.jaeyo.dde.dataflow.component.processor;

import java.util.UUID;

import org.jaeyo.dde.connectionqueue.ConnectionQueue;
import org.jaeyo.dde.dataflow.component.conngroup.InputConnectionGroup;
import org.jaeyo.dde.event.Event;

public abstract class InProcessor extends Processor implements Input{
	private InputConnectionGroup inputGroup; 
	
	public InProcessor(UUID id, int x, int y, InputConnectionGroup inputGroup) {
		super(id, x, y);
		this.inputGroup = inputGroup;
	}

	@Override
	public void addInputConnection(ConnectionQueue conn){
		inputGroup.addInputConnection(conn);
	} //addInputConnection
	
	@Override
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