package com.sample.server.proxy;

/*
 * The Proxy Server interface.
 * 
 * All Proxy Server implementations must implement this interface.
 */
public interface IProxyServer {
	
	/*
	 * method for starting proxy servers.
	 */
	public void start();
	
	/*
	 * method for checking if IP address is blacklisted.
	 * 
	 * @param ipAddress the IP address
	 */
	public boolean isIPBlacklisted(String ipAddress);
}
