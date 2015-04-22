package org.jaeyo.dde.processor;

import java.util.UUID;

import org.jaeyo.dde.connectionqueue.ConnectionQueue;
import org.jaeyo.dde.event.Event;
import org.jaeyo.dde.exception.NoAvailableOutputException;

public abstract class InOutProcessor extends Processor implements Input, Output{
	public static final String DEFAULT_TAG = "default_tag";
	
	private InputConnectionGroup inputGroup; 
	private OutputRouter outputRouter;
	
	public InOutProcessor(UUID id, InputConnectionGroup inputGroup, OutputRouter outputRouter) {
		super(id);
		this.inputGroup = inputGroup;
		this.outputRouter = outputRouter;
	} //INIT
	
	public void addInputConnection(ConnectionQueue conn){
		inputGroup.addInputConnection(conn);
	} //addInputConnection
	
	public void removeInputConnection(ConnectionQueue conn){
		inputGroup.removeInputConnection(conn);
	} //removeInputConnection
	
	public void addOutputConnection(String tag, ConnectionQueue conn){
		outputRouter.addOutputConnection(tag, conn);
	} //addOutputConnection
	
	public void removeOutputConection(ConnectionQueue conn){
		outputRouter.removeOutputConnection(conn);
	} //removeOutputConnection
	
	@Override
	public void job() {
		for(;;)
			onEvent(inputGroup.take());
	} //job
	
	protected void flowAway(String tag, Event event) throws NoAvailableOutputException{
		outputRouter.flowAway(tag, event);
	} //flowAway
	
	public abstract void onEvent(Event event);
} //class