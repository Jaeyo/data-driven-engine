package org.jaeyo.dde.dataflow.component.processor;

import org.jaeyo.dde.connectionqueue.ConnectionQueue;
import org.jaeyo.dde.dataflow.component.conngroup.InputConnectionGroup;


public interface Input {
	public void addInputConnection(ConnectionQueue conn);
	public void removeInputConnection(ConnectionQueue conn);
	public InputConnectionGroup getInputConnectionGroup();
} //interface