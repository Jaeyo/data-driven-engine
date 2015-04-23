package org.jaeyo.dde.dataflow.component.processor;

import org.jaeyo.dde.connectionqueue.ConnectionQueue;


public interface Input {
	public void addInputConnection(ConnectionQueue conn);
	public void removeInputConnection(ConnectionQueue conn);
} //interface