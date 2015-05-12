package org.jaeyo.dde.dataflow.component.processor.impl;

import java.util.Properties;
import java.util.UUID;

import org.jaeyo.dde.dataflow.component.conngroup.InputConnectionGroup;
import org.jaeyo.dde.dataflow.component.conngroup.OutputRouter;
import org.jaeyo.dde.dataflow.component.processor.InOutProcessor;
import org.jaeyo.dde.event.Event;
import org.jaeyo.dde.exception.NoAvailableOutputException;
import org.jaeyo.dde.exception.UnknownConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleDeliver extends InOutProcessor {
	private static final Logger logger = LoggerFactory.getLogger(SimpleDeliver.class);
	
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
	public String getComponentType() {
		return getClass().getSimpleName();
	}
	
	@Override
	public Properties getConfig() {
		return new Properties();
	}

	@Override
	public void setConfig(Properties config) throws UnknownConfigException {
	}

	@Override
	public void onStop() {
		logger.info("SimpleDeliver stopped");
	} //onStop
} //class