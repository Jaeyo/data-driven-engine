package org.jaeyo.dde.dataflow.component.conngroup;

import java.util.Map;
import java.util.Set;

import org.jaeyo.dde.connectionqueue.ConnectionQueue;
import org.jaeyo.dde.event.Event;
import org.jaeyo.dde.exception.NoAvailableOutputException;
import org.json.JSONArray;
import org.json.JSONObject;

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
	
	public JSONArray getOutputConnectionJson(){
		JSONArray json = new JSONArray();
		for(Set<ConnectionQueue> connections : router.values())
			for(ConnectionQueue connection : connections)
				json.put(new JSONObject().put("source", connection.getSource()).put("target", connection.getTarget()));
		return json;
	} //getOutputConnectionJson
} //class