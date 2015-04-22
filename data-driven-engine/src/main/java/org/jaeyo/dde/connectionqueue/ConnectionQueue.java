package org.jaeyo.dde.connectionqueue;

import java.util.UUID;

import org.jaeyo.dde.event.Event;

public interface ConnectionQueue {
	public void put(Event event);
	public Event take();
	public boolean isEmpty();
	public UUID getId();
} //interface