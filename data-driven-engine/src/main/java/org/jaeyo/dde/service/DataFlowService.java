package org.jaeyo.dde.service;

import java.util.UUID;

import javax.inject.Inject;

import org.jaeyo.dde.dataflow.Dataflow;
import org.jaeyo.dde.dataflow.component.Component;
import org.jaeyo.dde.dataflow.component.conngroup.InputConnectionGroup;
import org.jaeyo.dde.dataflow.component.conngroup.OutputRouter;
import org.jaeyo.dde.dataflow.component.processor.impl.ConsolePrinter;
import org.jaeyo.dde.dataflow.component.processor.impl.FileReader;
import org.jaeyo.dde.dataflow.component.processor.impl.SimpleDeliver;
import org.jaeyo.dde.exception.AlreadyStartedException;
import org.jaeyo.dde.exception.AlreadyStoppedException;
import org.jaeyo.dde.exception.InvalidOperationException;
import org.jaeyo.dde.exception.NotExistsException;
import org.jaeyo.dde.exception.UnknownComponentException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class DataFlowService {
	@Inject
	private Dataflow dataFlow;
	
	public Component createNewComponent(String type, String name, int x, int y) throws UnknownComponentException {
		Component component = createNewComponentInstance(type, name, x, y);
		dataFlow.addComponent(component);
		return component;
	} //createNewComponent
	
	public void updateComponent(String uuid, int x, int y) throws NotExistsException{
		Component component = dataFlow.getComponent(UUID.fromString(uuid));
		if(x >= 0)
			component.setX(x);
		if(y >= 0)
			component.setY(y);
	} //updateComponent
	
	public void startComponent(String id) throws NotExistsException, InvalidOperationException, AlreadyStartedException{
		dataFlow.startComponent(UUID.fromString(id));
	} //startCompnent
	
	public void stopComponent(String id) throws AlreadyStoppedException, InvalidOperationException, NotExistsException{
		dataFlow.stopComponent(UUID.fromString(id));
	} //stopComponent
	
	public void createNewConnection(String source, String target) throws NotExistsException, InvalidOperationException{
		dataFlow.connect(UUID.fromString(source), UUID.fromString(target));
	} //createNewConnection
	
	public JSONObject getDataFlowMap(){
		return dataFlow.getDataFlowMapJson();
	} //getDataFlowMap
	
	private Component createNewComponentInstance(String type, String name, int x, int y) throws UnknownComponentException {
		if("ConsolePrinter".equals(type)){
			return new ConsolePrinter(UUID.randomUUID(), x, y, name, new InputConnectionGroup());
		} else if("FileReader".equals(type)) {
			return new FileReader(UUID.randomUUID(), x, y, name, new OutputRouter());
		} else if("SimpleDeliver".equals(type)) {
			return new SimpleDeliver(UUID.randomUUID(), x, y, name, new InputConnectionGroup(), new OutputRouter());
		} else{
			throw new UnknownComponentException(type);
		} //if
	} //createNewProcessor
} //class