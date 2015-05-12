package org.jaeyo.dde.dataflow.component.processor.impl;

import java.util.Properties;
import java.util.UUID;

import org.jaeyo.dde.dataflow.component.conngroup.InputConnectionGroup;
import org.jaeyo.dde.dataflow.component.processor.InProcessor;
import org.jaeyo.dde.event.Event;
import org.jaeyo.dde.exception.UnknownConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsolePrinter extends InProcessor{
	private static final Logger logger = LoggerFactory.getLogger(ConsolePrinter.class);
	
	public ConsolePrinter(UUID id, int x, int y, String name, InputConnectionGroup inputGroup) {
		super(id, x, y, name, inputGroup);
	}

	@Override
	public void onEvent(Event event) {
		System.out.println(event.getPayload());
	}

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
		logger.info("ConsolePrinter stopped");
	} //onStop
} //class