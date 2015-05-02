package org.jaeyo.dde.dataflow.component.processor.impl;

import java.util.Properties;
import java.util.UUID;

import org.jaeyo.dde.dataflow.component.conngroup.InputConnectionGroup;
import org.jaeyo.dde.dataflow.component.processor.InProcessor;
import org.jaeyo.dde.event.Event;

public class ConsolePrinter extends InProcessor{
	public ConsolePrinter(UUID id, int x, int y, String name, InputConnectionGroup inputGroup) {
		super(id, x, y, name, inputGroup);
	}

	@Override
	public void onEvent(Event event) {
		System.out.println(event.getPayload());
	}

	@Override
	public void beforeStart() {
		System.out.println("ConsolePrinter started");
	}

	@Override
	public void beforeStop() {
		System.out.println("ConsolePrinter stopped");
	}

	@Override
	public void config(Properties config) {
		
	}

	@Override
	public String getComponentType() {
		return getClass().getSimpleName();
	}
} //class