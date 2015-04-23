package org.jaeyo.dde.dataflow;


import java.util.Map;
import java.util.UUID;

import org.jaeyo.dde.connectionqueue.ConnectionQueue;
import org.jaeyo.dde.connectionqueue.MemoryConnectionQueue;
import org.jaeyo.dde.dataflow.component.Component;
import org.jaeyo.dde.dataflow.component.processor.Input;
import org.jaeyo.dde.dataflow.component.processor.Output;
import org.jaeyo.dde.dataflow.component.processor.Processor;
import org.jaeyo.dde.exception.AlreadyStartedException;
import org.jaeyo.dde.exception.AlreadyStoppedException;
import org.jaeyo.dde.exception.InvalidOperationException;
import org.jaeyo.dde.exception.NotExistsException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

@Service
public class Dataflow{
	private Map<UUID, Component> components = Maps.newHashMap();
	
	public void addComponent(Component component){
		components.put(component.getId(), component);
	} //addComponent
	
	public void startComponent(UUID id) throws NotExistsException, InvalidOperationException, AlreadyStartedException{
		getProcessorComponent(id).start();
	} //startCompnent
	
	public void stopComponent(UUID id) throws AlreadyStoppedException, InvalidOperationException, NotExistsException{
		getProcessorComponent(id).stop();
	} //stopComponent
	
	public ConnectionQueue connect(UUID source, UUID target) throws NotExistsException, InvalidOperationException{
		Output sourceComponent = getOutComponent(source);
		Input targetComponent = getInComponent(target);
		
		ConnectionQueue conn = new MemoryConnectionQueue(UUID.randomUUID(), source, target);
		
		sourceComponent.addOutputConnection(conn);
		targetComponent.addInputConnection(conn);
		
		return conn;
	} //addConnection
	
	public JSONObject getDataFlowMapJson(){
		JSONArray componentsJson = new JSONArray();
		JSONArray linesJson = new JSONArray();
		
		for(Component component : components.values()){
			componentsJson.put(component.getJson());
			
			if(component instanceof Output){
				Output out = (Output) component;
				JSONArray connJsons = out.getOutputRouter().getOutputConnectionJson();
				if(connJsons.length() != 0)
					linesJson.put(connJsons);
			} //if
		} //for component
		
		return new JSONObject().put("component", componentsJson).put("lines", linesJson);
	} //getDataFlowMapJson
	
	private Processor getProcessorComponent(UUID id) throws InvalidOperationException, NotExistsException{
		Component component = getComponent(id);
		
		try {
			Preconditions.checkState(component instanceof Processor, "invalid processor component, " + id.toString());
		} catch (IllegalStateException e) {
			throw new InvalidOperationException(e.getMessage());
		} //catch
		
		return (Processor)component;
	} //getProcessorComponent
	
	private Input getInComponent(UUID id) throws NotExistsException, InvalidOperationException{
		Component component = getComponent(id);
		
		try{
			Preconditions.checkState(component instanceof Input, "invalid input component, " + id.toString());
		} catch (IllegalStateException e) {
			throw new InvalidOperationException(e.getMessage());
		} //catch
		
		return (Input)component;
	} //getInComponent
	
	private Output getOutComponent(UUID id) throws NotExistsException, InvalidOperationException{
		Component component = getComponent(id);
		
		try{
			Preconditions.checkState(component instanceof Output, "invalid output component, " + id.toString());
		} catch (IllegalStateException e) {
			throw new InvalidOperationException(e.getMessage());
		} //catch
		
		return (Output)component;
	} //getInComponent
	
	public Component getComponent(UUID id) throws NotExistsException{
		Component component = components.get(id);
		
		try {
			Preconditions.checkNotNull(component, id.toString() + " not exists");
		} catch (NullPointerException e) {
			throw new NotExistsException(e.getMessage());
		} //catch
		
		return component;
	} //getComponent
} //class