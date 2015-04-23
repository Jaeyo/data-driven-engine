package org.jaeyo.dde.connectionqueue;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jaeyo.dde.event.Event;

import com.google.common.collect.Queues;

public class MemoryConnectionQueue extends ConnectionQueue{
	private LinkedBlockingQueue<Event> queue = Queues.newLinkedBlockingQueue();
	
	public MemoryConnectionQueue(UUID id, UUID source, UUID target) {
		super(id, source, target);
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