package org.jaeyo.dde.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	private static final Logger logger=LoggerFactory.getLogger(HomeController.class);
	
	@Inject
//	private HomeService homeService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView root(HttpServletRequest request) {
		return home(request);
	} // test
	
	@RequestMapping(value="/home", method=RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request){
		return null;
	} //home
} //class