package org.jaeyo.dde.dataflow.component.processor.impl;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import org.jaeyo.dde.dataflow.component.conngroup.OutputRouter;
import org.jaeyo.dde.dataflow.component.processor.OutProcessor;
import org.jaeyo.dde.event.Event;
import org.jaeyo.dde.exception.NoAvailableOutputException;
import org.jaeyo.dde.exception.UnknownConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomWordGenerator extends OutProcessor{
	public static final String PERIOD = "period";
	private static final Logger logger = LoggerFactory.getLogger(RandomWordGenerator.class);
	
	long period = 1000;
	
	public RandomWordGenerator(UUID id, int x, int y, String name, OutputRouter outputRouter) {
		super(id, x, y, name, outputRouter);
	} //INIT

	@Override
	public String getComponentType() {
		return getClass().getSimpleName();
	}

	@Override
	public Properties getConfig() {
		Properties config = new Properties();
		config.setProperty(PERIOD, period+"");
		return config;
	}

	@Override
	public void setConfig(Properties config) throws UnknownConfigException {
		for(Entry<Object, Object> entry : config.entrySet()){
			String key = entry.getKey() + "";
			String value = entry.getValue() + "";
			
			if(key.equalsIgnoreCase(PERIOD)){
				this.period = Long.parseLong(value);
			} else{
				throw new UnknownConfigException(key);
			} //if
		} //for entry
	} //setConfig

	@Override
	public void onStart() {
		try {
			for(;;){
				Thread.sleep(period);
				flowAway(DEFAULT_TAG, new Event(UUID.randomUUID().toString()));
			} //for ;;	
		} catch (Exception e) {
			logger.error(String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage()), e);
		} //catch
	}

	@Override
	public void onStop() {
		logger.info("RandomWordGenerator stopped");
	} //onStop
} //class