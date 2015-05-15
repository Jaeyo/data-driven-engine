package org.jaeyo.dde.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import javax.inject.Inject;

import org.jaeyo.dde.dataflow.Dataflow;
import org.jaeyo.dde.dataflow.component.Component;
import org.jaeyo.dde.dataflow.component.conngroup.InputConnectionGroup;
import org.jaeyo.dde.dataflow.component.conngroup.OutputRouter;
import org.jaeyo.dde.dataflow.component.processor.impl.ConsolePrinter;
import org.jaeyo.dde.dataflow.component.processor.impl.FileReader;
import org.jaeyo.dde.dataflow.component.processor.impl.RandomWordGenerator;
import org.jaeyo.dde.dataflow.component.processor.impl.SimpleDeliver;
import org.jaeyo.dde.exception.AlreadyStartedException;
import org.jaeyo.dde.exception.AlreadyStoppedException;
import org.jaeyo.dde.exception.ConnectionExistsException;
import org.jaeyo.dde.exception.InvalidOperationException;
import org.jaeyo.dde.exception.NotExistsException;
import org.jaeyo.dde.exception.UnknownComponentException;
import org.jaeyo.dde.exception.UnknownConfigException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DataFlowService {
	@Inject
	private Dataflow dataFlow;
	private static final Logger logger = LoggerFactory.getLogger(DataFlowService.class);
	
	public Component createNewComponent(String type, String name, int x, int y) throws UnknownComponentException {
		Component component = createNewComponentInstance(type, name, x, y);
		dataFlow.addComponent(component);
		return component;
	} //createNewComponent
	
	public void updateComponent(String uuid, int x, int y, String name) throws NotExistsException{
		Component component = dataFlow.getComponent(UUID.fromString(uuid));
		if(x >= 0)
			component.setX(x);
		if(y >= 0)
			component.setY(y);
		if(name != null)
			component.setName(name);
	} //updateComponent
	
	public void startComponent(String id) throws NotExistsException, InvalidOperationException, AlreadyStartedException{
		logger.info("component with uuid {} will be started", id);
		dataFlow.startComponent(UUID.fromString(id));
	} //startCompnent
	
	public void stopComponent(String id) throws AlreadyStoppedException, InvalidOperationException, NotExistsException{
		logger.info("component with uuid {} will be stopped", id);
		dataFlow.stopComponent(UUID.fromString(id));
	} //stopComponent
	
	public void removeComponent(String id) throws NotExistsException, ConnectionExistsException{
		logger.info("component with uuid {} will be removed", id);
		dataFlow.removeCompoent(UUID.fromString(id));
	} //removeComponent
	
	public void createNewConnection(String source, String target) throws NotExistsException, InvalidOperationException{
		dataFlow.connect(UUID.fromString(source), UUID.fromString(target));
	} //createNewConnection
	
	public boolean removeConnection(String source, String target) throws NotExistsException, InvalidOperationException{
		return dataFlow.unconnect(UUID.fromString(source), UUID.fromString(target));
	} //removeConnection
	
	public JSONObject getDataFlowMap(){
		return dataFlow.getDataFlowMapJson();
	} //getDataFlowMap
	
	public void setConfig(String uuid, Map<String, String[]> paramsMap) throws NotExistsException, UnknownConfigException{
		Properties config = new Properties();
		for(Entry<String, String[]> entry : paramsMap.entrySet()){
			String key = entry.getKey();
			String value = entry.getValue()[0];
			config.setProperty(key, value);
		} //for entry
		dataFlow.getComponent(UUID.fromString(uuid)).setConfig(config);
	} //setConfig
	
	public JSONObject getConfig(String uuid) throws NotExistsException{
		Component component = dataFlow.getComponent(UUID.fromString(uuid));
		Properties config = component.getConfig();
		JSONObject configJson = new JSONObject();
		for(Entry<Object, Object> entry : config.entrySet()){
			String key = entry.getKey()+"";
			String value = entry.getValue()+"";
			configJson.put(key, value);
		} //for
		return configJson;
	} //getConfig
	
	private Component createNewComponentInstance(String type, String name, int x, int y) throws UnknownComponentException {
		if("ConsolePrinter".equals(type)){
			return new ConsolePrinter(UUID.randomUUID(), x, y, name, new InputConnectionGroup());
		} else if("FileReader".equals(type)) {
			return new FileReader(UUID.randomUUID(), x, y, name, new OutputRouter());
		} else if("SimpleDeliver".equals(type)) {
			return new SimpleDeliver(UUID.randomUUID(), x, y, name, new InputConnectionGroup(), new OutputRouter());
		} else if("RandomWordGenerator".equals(type)) {
			return new RandomWordGenerator(UUID.randomUUID(), x, y, name, new OutputRouter());
		} else{
			throw new UnknownComponentException(type);
		} //if
	} //createNewProcessor
} //class