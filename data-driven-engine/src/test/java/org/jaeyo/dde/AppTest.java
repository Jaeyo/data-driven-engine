package org.jaeyo.dde;

import static org.junit.Assert.*;

import java.util.UUID;

import org.jaeyo.dde.common.Util;
import org.jaeyo.dde.connectionqueue.ConnectionQueue;
import org.jaeyo.dde.connectionqueue.MemoryConnectionQueue;
import org.jaeyo.dde.exception.AlreadyStartedException;
import org.jaeyo.dde.processor.InputConnectionGroup;
import org.jaeyo.dde.processor.OutProcessor;
import org.jaeyo.dde.processor.OutputRouter;
import org.jaeyo.dde.processor.impl.ConsolePrinter;
import org.jaeyo.dde.processor.impl.FileReader;
import org.jaeyo.dde.processor.impl.SimpleDeliver;
import org.junit.Test;

public class AppTest {

	@Test
	public void test() throws Exception{
		FileReader p1 = new FileReader(UUID.randomUUID(), new OutputRouter());
		SimpleDeliver p2 = new SimpleDeliver(UUID.randomUUID(), new InputConnectionGroup(), new OutputRouter());
		ConsolePrinter p3 = new ConsolePrinter(UUID.randomUUID(), new InputConnectionGroup());
		
		ConnectionQueue conn1to2 = new MemoryConnectionQueue(UUID.randomUUID());
		ConnectionQueue conn2to3 = new MemoryConnectionQueue(UUID.randomUUID());
		
		p1.addOutputConnection(OutProcessor.DEFAULT_TAG, conn1to2);
		p2.addInputConnection(conn1to2);
		p2.addOutputConnection(OutProcessor.DEFAULT_TAG, conn2to3);
		p3.addInputConnection(conn2to3);
		
		p3.start();
		p2.start();
		p1.start();
		
		Util.sleep(5000);
	} //test
} //class