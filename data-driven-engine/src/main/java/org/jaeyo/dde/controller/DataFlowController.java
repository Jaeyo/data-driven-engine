package org.jaeyo.dde.controller;

import javax.inject.Inject;

import org.jaeyo.dde.exception.InvalidOperationException;
import org.jaeyo.dde.exception.NotExistsException;
import org.jaeyo.dde.exception.UnknownComponentException;
import org.jaeyo.dde.service.DataFlowService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DataFlowController {
	private static final Logger logger = LoggerFactory.getLogger(DataFlowController.class);
	
	@Inject
	private DataFlowService dataFlowService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView main(){
		return new ModelAndView("home");
	} //INIT
	
	@RequestMapping(value = "/DataFlow/AddComponent/{componentName}", method = RequestMethod.POST)
	public @ResponseBody String addComponent(
			@PathVariable("componentName") String componentName) {
		try {
			dataFlowService.createNewComponent(componentName, 0, 0);
			return "{ success : 1 }";
		} catch (UnknownComponentException e) {
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", "0").put("msg", msg).toString();
		} //catch
	} // addComponent

	@RequestMapping(value = "/DataFlow/StartComponent/{id}", method = RequestMethod.GET)
	public @ResponseBody String startComponent(
			@PathVariable("id") String id){
		//TODO
		return null;
	} //startComponent
	
	@RequestMapping(value = "/DataFlow/AddConnection/{source}/{target}", method = RequestMethod.POST)
	public @ResponseBody String addConnection(
			@PathVariable("source") String source, 
			@PathVariable("target") String target) {
		try {
			dataFlowService.createNewConnection(source, target);
			return "{ success : 1 }";
		} catch (NotExistsException | InvalidOperationException e) {
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", "0").put("msg", msg).toString();
		} //catch
	} //addConnection
	
	@RequestMapping(value = "/DataFlow/Map", method = RequestMethod.GET)
	public @ResponseBody String map() {
		return dataFlowService.getDataFlowMap().toString(4);
	} // addComponent
} //class