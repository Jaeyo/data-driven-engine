package org.jaeyo.dde.dataflow.component.processor;

import java.util.UUID;

import org.jaeyo.dde.connectionqueue.ConnectionQueue;
import org.jaeyo.dde.dataflow.component.Component;
import org.jaeyo.dde.dataflow.component.conngroup.OutputRouter;
import org.jaeyo.dde.event.Event;
import org.jaeyo.dde.exception.NoAvailableOutputException;

public abstract class OutProcessor extends Processor implements Output{
	private OutputRouter outputRouter;

	public OutProcessor(UUID id, int x, int y, OutputRouter outputRouter) {
		super(id, x, y);
		this.outputRouter = outputRouter;
	} //INIT
	
	@Override
	public void addOutputConnection(String tag, ConnectionQueue conn){
		outputRouter.addOutputConnection(tag, conn);
	} //addOutputConnection
	
	@Override
	public void addOutputConnection(ConnectionQueue conn) {
		addOutputConnection(Component.DEFAULT_TAG, conn);
	}

	@Override
	public void removeOutputConnection(ConnectionQueue conn){
		outputRouter.removeOutputConnection(conn);
	} //removeOutputConnection
	
	@Override
	public OutputRouter getOutputRouter(){
		return this.outputRouter;
	} //getOutputRouter
	
	protected void flowAway(String tag, Event event) throws NoAvailableOutputException{
		outputRouter.flowAway(tag, event);
	} //flowAway
} //class