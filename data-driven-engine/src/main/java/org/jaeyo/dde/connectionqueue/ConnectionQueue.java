package org.jaeyo.dde.connectionqueue;

import java.util.UUID;

import org.jaeyo.dde.event.Event;

public abstract class ConnectionQueue {
	protected UUID id;
	protected UUID source;
	protected UUID target;
	
	public ConnectionQueue(UUID id, UUID source, UUID target) {
		this.id = id;
		this.source = source;
		this.target = target;
	} //INIT
	
	public UUID getSource() {
		return source;
	}

	public void setSource(UUID source) {
		this.source = source;
	}

	public UUID getTarget() {
		return target;
	}

	public void setTarget(UUID target) {
		this.target = target;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public abstract void put(Event event);
	public abstract Event take();
	public abstract boolean isEmpty();
	public abstract UUID getId();
} //interface