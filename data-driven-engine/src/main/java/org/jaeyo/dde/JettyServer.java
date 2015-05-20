package org.jaeyo.dde;

import java.lang.reflect.Field;
import java.util.Set;

import org.apache.jasper.runtime.TldScanner;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jaeyo.dde.common.Conf;
import org.jaeyo.dde.common.Path;
import org.jaeyo.dde.common.SpringBeans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;

public class JettyServer {
	private static final Logger logger = LoggerFactory.getLogger(JettyServer.class);
	
	public static final String MVC_CONTEXT_NAME = "";

	private Server server;

	public void start() {
		try {
			enableJstl();
			int port = Conf.getAs(Conf.PORT);
			
			WebAppContext context = getWebAppContext();
			SpringBeans.setContext(context);
			
			server = new Server(port);
			server.setHandler(context);
			server.start();
		} catch (NumberFormatException e) {
			logger.error("invalid port : {}", Conf.get(Conf.PORT), e);
			throw new RuntimeException();
		} catch (Exception e) {
			logger.error("failed to start server", e);
			throw new RuntimeException();
		} // catch

		logger.info("server started");
	} // start
	
	public void join() throws InterruptedException {
		server.join();
	} //join

	private WebAppContext getWebAppContext(){
		ServletHolder mvcServletHolder = new ServletHolder(MVC_CONTEXT_NAME, new DispatcherServlet());
		mvcServletHolder.setInitParameter("contextConfigLocation", "spring-servlet.xml"); 

		WebAppContext context=new WebAppContext();
		context.setClassLoader(Thread.currentThread().getContextClassLoader());
		context.addServlet(mvcServletHolder, "/");
		context.setResourceBase(Path.getWebInfPath().getAbsolutePath()); 
		context.setContextPath("/" + MVC_CONTEXT_NAME);
		return context;
	} //getWebAppContext
	
	
	private void enableJstl(){
		try {
			Field f=TldScanner.class.getDeclaredField("systemUris");
			f.setAccessible(true);
			((Set)f.get(null)).clear();
		} catch (Exception e) {
			logger.error("could not clear TLD system uris", e);
		} //catch
	} //enableJstl

	private ServletContextHandler getServletHandler() {
		ServletHolder mvcServletHolder = new ServletHolder(MVC_CONTEXT_NAME, new DispatcherServlet());
		
		mvcServletHolder.setInitParameter("contextConfigLocation", "spring-servlet.xml");

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//		context.setAttribute("javax.servlet.context.tempdir", "./tmp/webapp/");
		context.setClassLoader(Thread.currentThread().getContextClassLoader());
		context.addServlet(mvcServletHolder, "/");
		context.setResourceBase(Path.getWebInfPath().getAbsolutePath());
		return context;
	} // getServletHandler
} // class