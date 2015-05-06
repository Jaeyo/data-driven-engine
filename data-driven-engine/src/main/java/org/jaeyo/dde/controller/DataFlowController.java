package org.jaeyo.dde.controller;

import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jaeyo.dde.dataflow.component.Component;
import org.jaeyo.dde.exception.InvalidOperationException;
import org.jaeyo.dde.exception.NotExistsException;
import org.jaeyo.dde.exception.UnknownComponentException;
import org.jaeyo.dde.exception.UnknownConfigException;
import org.jaeyo.dde.service.DataFlowService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@RequestMapping(value = "/DataFlow/Component/{type}", method = RequestMethod.POST)
	public @ResponseBody String addComponent(
			@PathVariable("type") String type,
			@RequestParam("name") String name) {
		try {
			Component component = dataFlowService.createNewComponent(type, name, 0, 0); //TODO IMME
			JSONObject retJson = new JSONObject();
			retJson.put("success", 1);
			retJson.put("component", component.getJson());
			return retJson.toString();
		} catch (Exception e) {
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", "0").put("msg", msg).toString();
		} //catch
	} // addComponent
	
	@RequestMapping(value = "/DataFlow/Component/{uuid}", method = RequestMethod.PUT)
	public @ResponseBody String updateComponent(
			@PathVariable("uuid") String uuid,
			@RequestParam(value = "x", required = false) String x,
			@RequestParam(value = "y", required = false) String y,
			@RequestParam(value = "name", required = false) String name){
		int xVal = (x == null) ? -1 : Integer.parseInt(x);
		int yVal = (y == null) ? -1 : Integer.parseInt(y);
		try {
			dataFlowService.updateComponent(uuid, xVal, yVal, name);
			return new JSONObject().put("success", 1).toString();
		} catch (Exception e) {
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("msg", msg).toString();
		} //catch
	} //updateComponent

	@RequestMapping(value = "/DataFlow/StartComponent/{id}", method = RequestMethod.GET)
	public @ResponseBody String startComponent(
			@PathVariable("id") String id){
		//TODO
		return null;
	} //startComponent
	
	@RequestMapping(value = "/DataFlow/Connection/{source}/{target}", method = RequestMethod.POST)
	public @ResponseBody String addConnection(
			@PathVariable("source") String source, 
			@PathVariable("target") String target) {
		try {
			dataFlowService.createNewConnection(source, target);
			return new JSONObject().put("success", 1).toString();
		} catch (Exception e) {
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", "0").put("msg", msg).toString();
		} //catch
	} //addConnection
	
	@RequestMapping(value = "/DataFlow/Connection/{source}/{target}", method = RequestMethod.DELETE)
	public @ResponseBody String removeConnection(
			@PathVariable("source") String source,
			@PathVariable("target") String target){
		try {
			boolean result = dataFlowService.removeConnection(source, target);
			if(result == true){
				return new JSONObject().put("success", 1).toString();
			} else {
				return new JSONObject()
						.put("success", 0)
						.put("msg", String.format("failed to find connection between %s and %s", source, target))
						.toString();
			} //if
		} catch (Exception e) {
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", "0").put("msg", msg).toString();
		} //catch
	} //removeConnection
	
	@RequestMapping(value = "/DataFlow/Config/{uuid}", method = RequestMethod.GET)
	public @ResponseBody String getConfig(@PathVariable("uuid") String uuid){
		try {
			return new JSONObject()
				.put("success", 1)
				.put("config", dataFlowService.getConfig(uuid))
				.toString();
		} catch (Exception e) {
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("msg", msg).toString();
		} //catch
	} //config
	
	@RequestMapping(value = "/DataFlow/Config/{uuid}", method = RequestMethod.PUT)
	public @ResponseBody String setConfig(@PathVariable("uuid") String uuid, HttpServletRequest request){
		Map<String, String[]> paramsMap = request.getParameterMap();
		try {
			dataFlowService.setConfig(uuid, paramsMap);
			return new JSONObject().put("success", 1).toString();
		} catch (Exception e) {
			String msg = String.format("%s, errmsg : %s", e.getClass().getSimpleName(), e.getMessage());
			logger.error(msg, e);
			return new JSONObject().put("success", 0).put("msg", msg).toString();
		} //catch
	} //config
	
	@RequestMapping(value = "/DataFlow/Map", method = RequestMethod.GET)
	public @ResponseBody String map() {
		return new JSONObject()
			.put("success", 1)
			.put("map", dataFlowService.getDataFlowMap())
			.toString();
	} // addComponent
} //class