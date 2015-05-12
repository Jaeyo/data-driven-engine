package org.jaeyo.dde.dataflow.component.processor.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import org.jaeyo.dde.common.Util;
import org.jaeyo.dde.dataflow.component.conngroup.OutputRouter;
import org.jaeyo.dde.dataflow.component.processor.OutProcessor;
import org.jaeyo.dde.event.Event;
import org.jaeyo.dde.exception.NoAvailableOutputException;
import org.jaeyo.dde.exception.UnknownConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileReader extends OutProcessor{
	public static final String PATH = "path";
	
	private static final Logger logger = LoggerFactory.getLogger(FileReader.class);
	private BufferedReader reader;
	
	private String path;

	public FileReader(UUID id, int x, int y, String name, OutputRouter outputRouter) {
		super(id, x, y, name, outputRouter);
	}

	@Override
	public String getComponentType() {
		return getClass().getSimpleName();
	}

	@Override
	public Properties getConfig() {
		Properties config = new Properties();
		config.setProperty(PATH, this.path==null ? "" : this.path);
		return config;
	}

	@Override
	public void setConfig(Properties config) throws UnknownConfigException {
		for(Entry<Object, Object> entry : config.entrySet()){
			String key = entry.getKey()+"";
			String value = entry.getValue()+"";
			
			if(key.equalsIgnoreCase(PATH)){
				this.path = value;
			} else{
				throw new UnknownConfigException(key);
			} //if
		} //for entry
	}

	@Override
	public void onStart() {
		System.out.println("FileReader started");
		
		File file = new File(path);
		try {
			this.reader = new BufferedReader(new java.io.FileReader(file));
		} catch (FileNotFoundException e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()));
		} //catch	
		
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
	public void onStop() {
		logger.info("FileReader stopped");
	} //onStop
} //class