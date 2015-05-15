package org.jaeyo.dde.dataflow.component.processor;

import java.util.UUID;

import org.jaeyo.dde.connectionqueue.ConnectionQueue;
import org.jaeyo.dde.dataflow.component.Component;
import org.jaeyo.dde.dataflow.component.conngroup.InputConnectionGroup;
import org.jaeyo.dde.dataflow.component.conngroup.OutputRouter;
import org.jaeyo.dde.event.Event;
import org.jaeyo.dde.exception.NoAvailableOutputException;

public abstract class InOutProcessor extends Component implements Input, Output{
	private InputConnectionGroup inputGroup; 
	private OutputRouter outputRouter;
	
	public InOutProcessor(UUID id, int x, int y, String name, InputConnectionGroup inputGroup, OutputRouter outputRouter) {
		super(x, y, name, id);
		this.inputGroup = inputGroup;
		this.outputRouter = outputRouter;
	} //INIT
	
	@Override
	public void addInputConnection(ConnectionQueue conn){
		inputGroup.addInputConnection(conn);
	} //addInputConnection
	
	@Override
	public void removeInputConnection(ConnectionQueue conn){
		inputGroup.removeInputConnection(conn);
	} //removeInputConnection
	
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
	public InputConnectionGroup getInputConnectionGroup() {
		return this.inputGroup;
	} //getInputConnectionGroup

	@Override
	public OutputRouter getOutputRouter(){
		return this.outputRouter;
	} //getOutputRouter
	
	protected void flowAway(String tag, Event event) throws NoAvailableOutputException{
		outputRouter.flowAway(tag, event);
	} //flowAway
	
	@Override
	public void onStart() {
		for(;;)
			onEvent(inputGroup.take());
	}

	public abstract void onEvent(Event event);
} //class