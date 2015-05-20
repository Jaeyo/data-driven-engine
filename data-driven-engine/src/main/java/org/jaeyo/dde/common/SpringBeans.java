package org.jaeyo.dde.common;

import java.util.Collection;

import org.eclipse.jetty.webapp.WebAppContext;

public class SpringBeans {
	private static WebAppContext context;

	public static WebAppContext getContext() {
		return context;
	}

	public static void setContext(WebAppContext context) {
		SpringBeans.context = context;
	}
	
	public <T> T getBean(Class<T> clazz){
		return context.getBean(clazz);
	} //getBean
	
	public <T> Collection<T> getBeans(Class<T> clazz){
		return context.getBeans(clazz);
	} //getBeans
} // class