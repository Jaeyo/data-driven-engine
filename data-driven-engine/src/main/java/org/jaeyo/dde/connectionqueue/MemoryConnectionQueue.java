package org.jaeyo.dde.connectionqueue;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jaeyo.dde.event.Event;

import com.google.common.collect.Queues;

public class MemoryConnectionQueue implements ConnectionQueue{
	private LinkedBlockingQueue<Event> queue = Queues.newLinkedBlockingQueue();
	private UUID id;
	
	public MemoryConnectionQueue(UUID id) {
		this.id = id;
	} //INIT

	@Override
	public void put(Event event) {
		queue.add(event);
	}

	@Override
	public Event take() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} //catch
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public UUID getId() {
		return this.id;
	}
} //class