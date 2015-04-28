package org.jaeyo.dde.telnet;

import javax.inject.Inject;

import org.jaeyo.dde.service.DataFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AddComponent extends Command{
	private static final Logger logger = LoggerFactory.getLogger(AddComponent.class);
	@Inject private DataFlowService dataFlowService;

	public AddComponent(){
	} //INIT

	@Override
	public void execute(String[] cmds) {
		try {
			print("input component name : ");
			String componentName = input.readLine();
			dataFlowService.createNewComponent(componentName, 0, 0);
		} catch (Exception e) {
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			println(msg);
		}
	} //execute
} //class