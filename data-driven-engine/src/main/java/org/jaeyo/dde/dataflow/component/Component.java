package org.jaeyo.dde.dataflow.component;

import java.util.UUID;

import org.json.JSONObject;

public abstract class Component {
	public static final String DEFAULT_TAG = "default_tag";
	
	private int x;
	private int y;

	public Component(int x, int y) {
		this.x = x;
		this.y = y;
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
	
	public JSONObject getJson(){
		return new JSONObject().put("id", getId().toString()).put("type", getComponentType());
	} //getJson
	
	public abstract UUID getId();
	public abstract String getComponentType();
}