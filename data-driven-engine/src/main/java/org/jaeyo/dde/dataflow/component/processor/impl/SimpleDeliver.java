package org.jaeyo.dde.dataflow.component.processor.impl;

import java.util.Properties;
import java.util.UUID;

import org.jaeyo.dde.dataflow.component.conngroup.InputConnectionGroup;
import org.jaeyo.dde.dataflow.component.conngroup.OutputRouter;
import org.jaeyo.dde.dataflow.component.processor.InOutProcessor;
import org.jaeyo.dde.event.Event;
import org.jaeyo.dde.exception.NoAvailableOutputException;

public class SimpleDeliver extends InOutProcessor {

	public SimpleDeliver(UUID id, int x, int y, String name, InputConnectionGroup inputGroup, OutputRouter outputRouter) {
		super(id, x, y, name, inputGroup, outputRouter);
	} //INIT

	@Override
	public void onEvent(Event event) {
		try {
			flowAway(DEFAULT_TAG, event);
		} catch (NoAvailableOutputException e) {
			e.printStackTrace();
		} //catch
	} //onEvent

	@Override
	public void beforeStart() {
		System.out.println("SimpleDeliver started");
	}

	@Override
	public void beforeStop() {
		System.out.println("SimpleDeliver stopped");
	}

	@Override
	public void config(Properties config) {
	}

	@Override
	public String getComponentType() {
		return getClass().getSimpleName();
	}
}