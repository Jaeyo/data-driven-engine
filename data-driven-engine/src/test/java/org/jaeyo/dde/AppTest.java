package org.jaeyo.dde;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.jaeyo.dde.common.Util;
import org.jaeyo.dde.connectionqueue.ConnectionQueue;
import org.jaeyo.dde.connectionqueue.MemoryConnectionQueue;
import org.jaeyo.dde.dataflow.component.conngroup.InputConnectionGroup;
import org.jaeyo.dde.dataflow.component.conngroup.OutputRouter;
import org.jaeyo.dde.dataflow.component.processor.OutProcessor;
import org.jaeyo.dde.dataflow.component.processor.impl.ConsolePrinter;
import org.jaeyo.dde.dataflow.component.processor.impl.FileReader;
import org.jaeyo.dde.dataflow.component.processor.impl.SimpleDeliver;
import org.junit.Test;

public class AppTest {

	@Test
	public void test() throws Exception{
		FileReader p1 = new FileReader(UUID.randomUUID(), 0, 0, new OutputRouter());
		SimpleDeliver p2 = new SimpleDeliver(UUID.randomUUID(), 0, 0, new InputConnectionGroup(), new OutputRouter());
		ConsolePrinter p3 = new ConsolePrinter(UUID.randomUUID(), 0, 0, new InputConnectionGroup());
		
		ConnectionQueue conn1to2 = new MemoryConnectionQueue(UUID.randomUUID(), p1.getId(), p2.getId());
		ConnectionQueue conn2to3 = new MemoryConnectionQueue(UUID.randomUUID(), p2.getId(), p3.getId());
		
		p1.addOutputConnection(OutProcessor.DEFAULT_TAG, conn1to2);
		p2.addInputConnection(conn1to2);
		p2.addOutputConnection(OutProcessor.DEFAULT_TAG, conn2to3);
		p3.addInputConnection(conn2to3);
		
		p3.start();
		p2.start();
		p1.start();
		
		Util.sleep(5000);
	} //test
	
	@Test
	public void test1(){
		abstract class ClassA{}
		abstract class ClassB extends ClassA{}
		class ClassC extends ClassB{}
		
		ClassA clazz = new ClassC();
		assertTrue(clazz instanceof ClassA);
		assertTrue(clazz instanceof ClassB);
		assertTrue(clazz instanceof ClassC);
	} //test1
	
	interface InterfaceA{}
	interface InterfaceB extends InterfaceA{}
	class ClassC implements InterfaceB{}
	
	@Test
	public void test2(){
		ClassC clazz = new ClassC();
		assertTrue(clazz instanceof InterfaceB);
		assertTrue(clazz instanceof InterfaceA);
	} //test2
} //class