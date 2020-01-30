package com.sample.server.proxy;

import java.util.logging.Logger;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/*
 * This is proxy server main application class. The proxy server implementation is loaded from the spring context.
 */
public class ProxyServerMain {

	public static String DEFAULT_SPRING_CONFIG_FILE = "proxyServerConfig.xml";
	
	public static String PROXY_SERVER_NAME = "proxyServer";
	
	private static ClassPathXmlApplicationContext applicationContext = null;
	
	private final static Logger logger = Logger.getLogger(ProxyServerMain.class .getName());
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		IProxyServer proxyServer = null;
		
		logger.info("Initialising spring configuration...");
		initialiseSpringConfiguration();
		logger.info("Spring configuration initialised...");
		
		logger.info("Loading the proxy server implementation...");
		proxyServer = (SimpleProxyServer)applicationContext.getBean(ProxyServerMain.PROXY_SERVER_NAME);
		
		logger.info("Proxy Server implementation loaded...");
	
		proxyServer.start();
		logger.info("Proxy Server started...");
	}
	
	/*
	 * Initialise the spring configuration.
	 *
	 */
	private static void initialiseSpringConfiguration() {
		if (applicationContext == null) {
			applicationContext = new ClassPathXmlApplicationContext(ProxyServerMain.DEFAULT_SPRING_CONFIG_FILE);
		}
 
	}
}
