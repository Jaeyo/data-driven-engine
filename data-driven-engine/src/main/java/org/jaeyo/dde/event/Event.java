package org.jaeyo.dde.event;

public class Event implements Cloneable{
	private Object payload;

	public Event() {}

	public Event(Object payload) {
		this.payload = payload;
	} //INIT
	
	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	@Override
	public Event clone() {
		Event event = new Event();
		event.payload = this.payload;
		return event;
	} //clone
} //class