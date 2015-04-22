package org.jaeyo.dde.processor;

import java.util.UUID;

import org.jaeyo.dde.connectionqueue.ConnectionQueue;
import org.jaeyo.dde.event.Event;
import org.jaeyo.dde.exception.NoAvailableOutputException;

public abstract class OutProcessor extends Processor implements Output{
	public static final String DEFAULT_TAG = "default_tag";
	
	private OutputRouter outputRouter;

	public OutProcessor(UUID id, OutputRouter outputRouter) {
		super(id);
		this.outputRouter = outputRouter;
	} //INIT
	
	public void addOutputConnection(String tag, ConnectionQueue conn){
		outputRouter.addOutputConnection(tag, conn);
	} //addOutputConnection
	
	public void removeOutputConection(ConnectionQueue conn){
		outputRouter.removeOutputConnection(conn);
	} //removeOutputConnection
	
	protected void flowAway(String tag, Event event) throws NoAvailableOutputException{
		outputRouter.flowAway(tag, event);
	} //flowAway
} //class