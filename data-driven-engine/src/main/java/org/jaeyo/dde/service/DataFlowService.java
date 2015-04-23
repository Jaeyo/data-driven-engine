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
	
	public void createNewComponent(String componentName, int x, int y) throws UnknownComponentException {
		Component component = createNewComponentInstance(componentName, x, y);
		dataFlow.addComponent(component);
	} //createNewComponent
	
	public void startComponent(String id) throws NotExistsException, InvalidOperationException, AlreadyStartedException{
		dataFlow.startComponent(UUID.fromString(id));
	} //startCompnent
	
	public void stopComponent(String id) throws AlreadyStoppedException, InvalidOperationException, NotExistsException{
		dataFlow.stopComponent(UUID.fromString(id));
	} //stopComponent
	
	public void createNewConnection(String source, String target) throws NotExistsException, InvalidOperationException{
		dataFlow.addConnection(UUID.fromString(source), UUID.fromString(target));
	} //createNewConnection
	
	public JSONObject getDataFlowMap(){
		return dataFlow.getDataFlowMapJson();
	} //getDataFlowMap
	
	private Component createNewComponentInstance(String componentName, int x, int y) throws UnknownComponentException {
		if("ConsolePrinter".equals(componentName)){
			return new ConsolePrinter(UUID.randomUUID(), x, y, new InputConnectionGroup());
		} else if("FileReader".equals(componentName)) {
			return new FileReader(UUID.randomUUID(), x, y, new OutputRouter());
		} else if("SimpleDeliver".equals(componentName)) {
			return new SimpleDeliver(UUID.randomUUID(), x, y, new InputConnectionGroup(), new OutputRouter());
		} else{
			throw new UnknownComponentException(componentName);
		} //if
	} //createNewProcessor
} //class