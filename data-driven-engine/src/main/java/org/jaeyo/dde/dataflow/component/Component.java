package org.jaeyo.dde.dataflow.component;

import java.util.UUID;

import org.jaeyo.dde.dataflow.component.processor.Input;
import org.jaeyo.dde.dataflow.component.processor.Output;
import org.json.JSONObject;

public abstract class Component {
	public static final String DEFAULT_TAG = "default_tag";
	
	private int x;
	private int y;
	private String name;

	public Component(int x, int y, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
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

	public JSONObject getJson(){
		return new JSONObject()
			.put("uuid", getId().toString())
			.put("name", getName())
			.put("type", getComponentType())
			.put("x", getX())
			.put("y", getY())
			.put("inputable", this instanceof Input)
			.put("outputable", this instanceof Output);
	} //getJson
	
	public abstract UUID getId();
	public abstract String getComponentType();
}