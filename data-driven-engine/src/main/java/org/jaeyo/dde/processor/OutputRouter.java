package org.jaeyo.dde.processor;

import java.util.Map;
import java.util.Set;

import org.jaeyo.dde.connectionqueue.ConnectionQueue;
import org.jaeyo.dde.event.Event;
import org.jaeyo.dde.exception.NoAvailableOutputException;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class OutputRouter {
	protected Map<String, Set<ConnectionQueue>> router = Maps.newHashMap();
	
	public void addOutputConnection(String tag, ConnectionQueue conn){
		Set<ConnectionQueue> conns = router.get(tag);
		if(conns == null)
			conns = Sets.newHashSet();
		conns.add(conn);
		router.put(tag, conns);
	} //addOutputConnection
	
	public void removeOutputConnection(ConnectionQueue conn){
		for(Set<ConnectionQueue> conns : router.values()){
			if(conns.contains(conn)){
				conns.remove(conn);
				continue;
			} //if
		} //for conns
	} //removeOutputConnection
	
	public void flowAway(String tag, Event event) throws NoAvailableOutputException{
		Set<ConnectionQueue> conns = router.get(tag);
		if(conns == null || conns.size() == 0)
			throw new NoAvailableOutputException();
	
		for (ConnectionQueue conn : conns)
			conn.put(event.clone());
	} //flowAway
} //class