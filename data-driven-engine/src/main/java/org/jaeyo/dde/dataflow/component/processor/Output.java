package org.jaeyo.dde.dataflow.component.processor;

import org.jaeyo.dde.connectionqueue.ConnectionQueue;
import org.jaeyo.dde.dataflow.component.conngroup.OutputRouter;


public interface Output {
	public void addOutputConnection(ConnectionQueue conn);
	public void addOutputConnection(String tag, ConnectionQueue conn);
	public void removeOutputConnection(ConnectionQueue conn);
	public OutputRouter getOutputRouter();
} //interface