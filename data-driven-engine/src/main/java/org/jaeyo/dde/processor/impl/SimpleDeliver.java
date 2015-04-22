package org.jaeyo.dde.processor.impl;

import java.util.Properties;
import java.util.UUID;

import org.jaeyo.dde.event.Event;
import org.jaeyo.dde.exception.NoAvailableOutputException;
import org.jaeyo.dde.processor.InOutProcessor;
import org.jaeyo.dde.processor.InputConnectionGroup;
import org.jaeyo.dde.processor.OutputRouter;

public class SimpleDeliver extends InOutProcessor {

	public SimpleDeliver(UUID id, InputConnectionGroup inputGroup, OutputRouter outputRouter) {
		super(id, inputGroup, outputRouter);
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
}