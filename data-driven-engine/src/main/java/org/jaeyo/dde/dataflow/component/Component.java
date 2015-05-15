package org.jaeyo.dde.dataflow.component;

import java.util.Properties;
import java.util.UUID;

import org.jaeyo.dde.dataflow.component.processor.Input;
import org.jaeyo.dde.dataflow.component.processor.Output;
import org.jaeyo.dde.exception.UnknownConfigException;
import org.json.JSONObject;

public abstract class Component implements Runnable{
	public static final String DEFAULT_TAG = "default_tag";
	
	private int x;
	private int y;
	private String name;
	private UUID id;
	private Thread worker;
	private boolean isStarted = false;

	public Component(int x, int y, String name, UUID id) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}
	
	public boolean isStarted() {
		return isStarted;
	}

	public void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}

	@Override
	public void run() {
		isStarted = true;
		try{
			this.worker = Thread.currentThread();
			onStart();
		} catch(Exception e){
		} finally{
			isStarted = false;
		}
	} //run
	
	public void stop(){
		onStop();
		isStarted = false;
		this.worker.interrupt();
	} //stop

	public JSONObject getJson(){
		return new JSONObject()
			.put("uuid", getId().toString())
			.put("name", getName())
			.put("type", getComponentType())
			.put("x", getX())
			.put("y", getY())
			.put("inputable", this instanceof Input)
			.put("outputable", this instanceof Output)
			.put("started", isStarted);
	} //getJson
	
	public abstract void onStart();
	public abstract void onStop();
	public abstract String getComponentType();
	public abstract Properties getConfig();
	public abstract void setConfig(Properties config) throws UnknownConfigException;
}