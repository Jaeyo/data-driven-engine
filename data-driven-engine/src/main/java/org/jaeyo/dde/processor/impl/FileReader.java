package org.jaeyo.dde.processor.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import org.jaeyo.dde.common.Util;
import org.jaeyo.dde.event.Event;
import org.jaeyo.dde.exception.NoAvailableOutputException;
import org.jaeyo.dde.processor.OutProcessor;
import org.jaeyo.dde.processor.OutputRouter;

public class FileReader extends OutProcessor{
	private BufferedReader reader;

	public FileReader(UUID id, OutputRouter outputRouter) {
		super(id, outputRouter);
	}

	@Override
	public void job() {
		String line = null;
		try {
			for(;;){
				while((line = reader.readLine()) != null){
					flowAway(DEFAULT_TAG, new Event(line));
				} //while

				Util.sleep(100);
			} //for ;;
		} catch (NoAvailableOutputException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void beforeStart() {
		System.out.println("FileReader started");
		
		File file = new File("d:\\tmp\\tmp\\test.txt");
		try {
			this.reader = new BufferedReader(new java.io.FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} //catch
	}

	@Override
	public void beforeStop() {
		System.out.println("FileReader stopped");
	}

	@Override
	public void config(Properties config) {
	}

}
