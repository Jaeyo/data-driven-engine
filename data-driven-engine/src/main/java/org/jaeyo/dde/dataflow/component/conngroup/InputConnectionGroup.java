package org.jaeyo.dde.dataflow.component.conngroup;

import java.util.List;

import org.jaeyo.dde.common.Util;
import org.jaeyo.dde.connectionqueue.ConnectionQueue;
import org.jaeyo.dde.event.Event;

import com.google.common.collect.Lists;

public class InputConnectionGroup {
	private List<ConnectionQueue> inputs = Lists.newArrayList();
	
	public void addInputConnection(ConnectionQueue conn){
		inputs.add(conn);
	} //addInputConnection
	
	public void removeInputConnection(ConnectionQueue conn){
		inputs.remove(conn);
	} //removeInputConnection

	public Event take(){
		for(;;){
			for (ConnectionQueue input : inputs)
				if(input.isEmpty() == false)
					return input.take();
			
			Util.sleep(100L);
		} //for ;;
	} //take
} //class