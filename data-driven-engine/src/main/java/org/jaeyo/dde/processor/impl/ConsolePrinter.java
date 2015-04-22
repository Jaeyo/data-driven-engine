package org.jaeyo.dde.processor.impl;

import java.util.Properties;
import java.util.UUID;

import org.jaeyo.dde.event.Event;
import org.jaeyo.dde.processor.InProcessor;
import org.jaeyo.dde.processor.InputConnectionGroup;

public class ConsolePrinter extends InProcessor{

	public ConsolePrinter(UUID id, InputConnectionGroup inputGroup) {
		super(id, inputGroup);
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
} //class