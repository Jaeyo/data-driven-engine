package org.jaeyo.dde.dataflow;

import static org.junit.Assert.*;

import java.util.UUID;

import org.jaeyo.dde.common.ReflectionUtil;
import org.jaeyo.dde.dataflow.component.Component;
import org.jaeyo.dde.dataflow.component.conngroup.InputConnectionGroup;
import org.jaeyo.dde.dataflow.component.conngroup.OutputRouter;
import org.jaeyo.dde.dataflow.component.processor.impl.ConsolePrinter;
import org.jaeyo.dde.dataflow.component.processor.impl.FileReader;
import org.jaeyo.dde.dataflow.component.processor.impl.SimpleDeliver;
import org.junit.Test;

public class DataflowTest {
	@Test
	public void test_getProcessorComponent(){
		try{
			Dataflow dataflow = new Dataflow();

			UUID comp1Id = UUID.randomUUID();
			UUID comp2Id = UUID.randomUUID();
			UUID comp3Id = UUID.randomUUID();

			FileReader comp1 = new FileReader(comp1Id, 0, 0, new OutputRouter());
			SimpleDeliver comp2 = new SimpleDeliver(comp2Id, 0, 0, new InputConnectionGroup(), new OutputRouter());
			ConsolePrinter comp3 = new ConsolePrinter(comp3Id, 0, 0, new InputConnectionGroup());

			dataflow.addComponent(comp1);
			dataflow.addComponent(comp2);
			dataflow.addComponent(comp3);

			assertTrue(ReflectionUtil.invokePrivateMethod(dataflow, Dataflow.class, "getComponent", Component.class, comp1Id).equals(comp1));
			assertTrue(ReflectionUtil.invokePrivateMethod(dataflow, Dataflow.class, "getComponent", Component.class, comp2Id).equals(comp2));
			assertTrue(ReflectionUtil.invokePrivateMethod(dataflow, Dataflow.class, "getComponent", Component.class, comp3Id).equals(comp3));
			
			TODO IMME
		} catch(Exception e){
			e.printStackTrace();
			fail();
		} //catch
	}
} //class