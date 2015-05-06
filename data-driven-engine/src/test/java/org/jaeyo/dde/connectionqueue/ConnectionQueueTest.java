package org.jaeyo.dde.connectionqueue;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.google.common.collect.Sets;

public class ConnectionQueueTest {

	@Test
	public void test() {
		UUID source = UUID.randomUUID();
		UUID target = UUID.randomUUID();
		
		MemoryConnectionQueue q1 = new MemoryConnectionQueue(UUID.randomUUID(), source, target);
		MemoryConnectionQueue q2 = new MemoryConnectionQueue(UUID.randomUUID(), source, target);
		assertTrue(q1.equals(q2));
		Set<ConnectionQueue> set = Sets.newHashSet();
		set.add(q1);
		set.add(q2);
		assertTrue(set.size() == 1);
	}
}