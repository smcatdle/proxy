/**
 * 
 */
package com.sample.server.proxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.sample.server.proxy.thread.IWorkerThreadFactory;

/**
 * @author smcardle
 * 
 * This is a simple implementation of a proxy server. 
 * 
 * The proxy server enables clients to forward requests through it before forwarding the requests to a server.
 * The proxy server should be deployed on a separate machine from the clients and it should not be possible for 
 * clients to make a direct connection to the server. The clients can send requests to the proxy server as if it 
 * were the real server.
 * 
 *
 */
public class SimpleProxyServer implements IProxyServer {

	private int threadPoolSize;
	
	private int maxBufferSize;
	
	private int localPort;
	
	private int serverPort;
	
	private String serverHost = null;
	
	private int proxySOTimeout = 0;
	
	private int proxyReceiveBufferSize = 1024;
	
	private int serverSOTimeout = 0;
	
	private boolean serverKeepAlive = false;
	
	private int serverLinger = 0;
	
	private boolean serverTCPNoDelay = false;
	
	private int serverReceiveBufferSize = 1024;
	
	private int serverSendBufferSize = 1024;
	
	private int clientSOTimeout = 0;
	
	private boolean clientKeepAlive = false;
	
	private int clientLinger = 0;
	
	private boolean clientTCPNoDelay = false;
	
	private int clientReceiveBufferSize = 1024;
	
	private int clientSendBufferSize = 1024;

	private boolean authentication = false;
	
	private List<String> whiteList = null;
	
	private List<String> blackList = null;
	
	private IWorkerThreadFactory workerThreadFactory = null;
	
	private final static Logger logger = Logger.getLogger(SimpleProxyServer.class.getName());
	
	
	/*
	 * Constructor
	 */
	public SimpleProxyServer() {
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.sample.server.proxy.IProxyServer#start()
	 */
	public void start() {
		Socket clientConnection = null;
		Socket serverConnection = null;
		ServerSocket serverSocket = null;
		ExecutorService threadExecutor = null;
		String ipAddress = null;
		
		try {
			logger.info("Initialising Server socket at port : " + localPort + "...");
			
			// create the listening socket.
			serverSocket = new ServerSocket(localPort);
			serverSocket.setSoTimeout(proxySOTimeout);
			serverSocket.setReceiveBufferSize(proxyReceiveBufferSize);

			
			logger.info("Proxy Server Socket initialised.");
			
			// create the thread executor (to manage thread pools).
			threadExecutor = Executors.newFixedThreadPool(threadPoolSize);
			
			// infinite loop for accepting client connections.
			while (true) {

				// accept incoming client connection.
				clientConnection = serverSocket.accept();
				clientConnection.setSoTimeout(clientSOTimeout);
				clientConnection.setKeepAlive(clientKeepAlive);
				if (serverLinger > 0) serverConnection.setSoLinger(true, clientLinger);
				clientConnection.setTcpNoDelay(clientTCPNoDelay);
				clientConnection.setReceiveBufferSize(clientReceiveBufferSize);
				clientConnection.setSendBufferSize(clientSendBufferSize);
				
				// check if the client IP is blacklisted.
				InetAddress clientInetAdress = clientConnection.getInetAddress();
				ipAddress = clientInetAdress.toString().substring(1);
				
				logger.info("New client connection from IP address : " + ipAddress);
				
				if (!isIPBlacklisted(ipAddress)) {
					
					// make a connection to the configured server for this client.
					serverConnection = new Socket(serverHost, serverPort);
					serverConnection.setSoTimeout(serverSOTimeout);
					serverConnection.setKeepAlive(serverKeepAlive);
					if (serverLinger > 0) serverConnection.setSoLinger(true, serverLinger);
					serverConnection.setTcpNoDelay(serverTCPNoDelay);
					serverConnection.setReceiveBufferSize(serverReceiveBufferSize);
					serverConnection.setSendBufferSize(serverSendBufferSize);
					
					// assign worker threads to input and output connections.
					threadExecutor.execute((Thread) workerThreadFactory
							.createNewInstance(clientConnection, serverConnection,
									maxBufferSize));
					threadExecutor.execute((Thread) workerThreadFactory
							.createNewInstance(serverConnection, clientConnection,
									maxBufferSize));
				} else {
					logger.info("Blocking connection from IP address : " + ipAddress + " as it is blacklisted.");
					clientConnection.close();
				}
				
			}
				
			
		} catch (IOException e) {
			logger.severe("An error occurred : " + e);
		} catch (Exception ex) {
			logger.severe("An error occurred : " + ex);

		} finally {
			
			try {
				// close the connections.
				if (clientConnection != null) clientConnection.close();
				if (serverConnection != null) serverConnection.close();
				if (serverSocket != null) serverSocket.close();
			} catch (IOException e) {
				logger.severe("Error closing server socket : " + e);
			}
			
		}
	}

	/*
	 * Check if the IP address is black listed.
	 * 
	 * @param ipAddress the IP address to check
	 */
	public boolean isIPBlacklisted(String ipAddress) {
		
		for (String blacklistedIP : this.blackList) {
			if (blacklistedIP.equals(ipAddress)) {
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * Check if the IP address is white listed.
	 * 
	 * @param ipAddress the IP address to check
	 */
	public boolean isIPWhitelisted(String ipAddress) {
		
		for (String whitelistedIP : this.whiteList) {
			if (whitelistedIP.equals(ipAddress)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @return the threadPoolSize
	 */
	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	/**
	 * @param threadPoolSize the threadPoolSize to set
	 */
	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	/**
	 * @return the maxBufferSize
	 */
	public int getMaxBufferSize() {
		return maxBufferSize;
	}

	/**
	 * @param maxBufferSize the maxBufferSize to set
	 */
	public void setMaxBufferSize(int maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
	}

	/**
	 * @return the localPort
	 */
	public int getLocalPort() {
		return localPort;
	}

	/**
	 * @param localPort the localPort to set
	 */
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	/**
	 * @return the serverPort
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * @return the serverHost
	 */
	public String getServerHost() {
		return serverHost;
	}

	/**
	 * @param serverHost the serverHost to set
	 */
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	/**
	 * @return the proxySOTimeout
	 */
	public int getProxySOTimeout() {
		return proxySOTimeout;
	}

	/**
	 * @param proxySOTimeout the proxySOTimeout to set
	 */
	public void setProxySOTimeout(int proxySOTimeout) {
		this.proxySOTimeout = proxySOTimeout;
	}

	/**
	 * @return the proxyReceiveBufferSize
	 */
	public int getProxyReceiveBufferSize() {
		return proxyReceiveBufferSize;
	}

	/**
	 * @param proxyReceiveBufferSize the proxyReceiveBufferSize to set
	 */
	public void setProxyReceiveBufferSize(int proxyReceiveBufferSize) {
		this.proxyReceiveBufferSize = proxyReceiveBufferSize;
	}

	/**
	 * @return the serverSOTimeout
	 */
	public int getServerSOTimeout() {
		return serverSOTimeout;
	}

	/**
	 * @param serverSOTimeout the serverSOTimeout to set
	 */
	public void setServerSOTimeout(int serverSOTimeout) {
		this.serverSOTimeout = serverSOTimeout;
	}

	/**
	 * @return the serverKeepAlive
	 */
	public boolean isServerKeepAlive() {
		return serverKeepAlive;
	}

	/**
	 * @param serverKeepAlive the serverKeepAlive to set
	 */
	public void setServerKeepAlive(boolean serverKeepAlive) {
		this.serverKeepAlive = serverKeepAlive;
	}

	/**
	 * @return the serverLinger
	 */
	public int getServerLinger() {
		return serverLinger;
	}

	/**
	 * @param serverLinger the serverLinger to set
	 */
	public void setServerLinger(int serverLinger) {
		this.serverLinger = serverLinger;
	}

	/**
	 * @return the serverTCPNoDelay
	 */
	public boolean isServerTCPNoDelay() {
		return serverTCPNoDelay;
	}

	/**
	 * @param serverTCPNoDelay the serverTCPNoDelay to set
	 */
	public void setServerTCPNoDelay(boolean serverTCPNoDelay) {
		this.serverTCPNoDelay = serverTCPNoDelay;
	}

	/**
	 * @return the serverReceiveBufferSize
	 */
	public int getServerReceiveBufferSize() {
		return serverReceiveBufferSize;
	}

	/**
	 * @param serverReceiveBufferSize the serverReceiveBufferSize to set
	 */
	public void setServerReceiveBufferSize(int serverReceiveBufferSize) {
		this.serverReceiveBufferSize = serverReceiveBufferSize;
	}

	/**
	 * @return the serverSendBufferSize
	 */
	public int getServerSendBufferSize() {
		return serverSendBufferSize;
	}

	/**
	 * @param serverSendBufferSize the serverSendBufferSize to set
	 */
	public void setServerSendBufferSize(int serverSendBufferSize) {
		this.serverSendBufferSize = serverSendBufferSize;
	}

	/**
	 * @return the clientSOTimeout
	 */
	public int getClientSOTimeout() {
		return clientSOTimeout;
	}

	/**
	 * @param clientSOTimeout the clientSOTimeout to set
	 */
	public void setClientSOTimeout(int clientSOTimeout) {
		this.clientSOTimeout = clientSOTimeout;
	}

	/**
	 * @return the clientKeepAlive
	 */
	public boolean isClientKeepAlive() {
		return clientKeepAlive;
	}

	/**
	 * @param clientKeepAlive the clientKeepAlive to set
	 */
	public void setClientKeepAlive(boolean clientKeepAlive) {
		this.clientKeepAlive = clientKeepAlive;
	}

	/**
	 * @return the clientLinger
	 */
	public int getClientLinger() {
		return clientLinger;
	}

	/**
	 * @param clientLinger the clientLinger to set
	 */
	public void setClientLinger(int clientLinger) {
		this.clientLinger = clientLinger;
	}

	/**
	 * @return the clientTCPNoDelay
	 */
	public boolean isClientTCPNoDelay() {
		return clientTCPNoDelay;
	}

	/**
	 * @param clientTCPNoDelay the clientTCPNoDelay to set
	 */
	public void setClientTCPNoDelay(boolean clientTCPNoDelay) {
		this.clientTCPNoDelay = clientTCPNoDelay;
	}

	/**
	 * @return the clientReceiveBufferSize
	 */
	public int getClientReceiveBufferSize() {
		return clientReceiveBufferSize;
	}

	/**
	 * @param clientReceiveBufferSize the clientReceiveBufferSize to set
	 */
	public void setClientReceiveBufferSize(int clientReceiveBufferSize) {
		this.clientReceiveBufferSize = clientReceiveBufferSize;
	}

	/**
	 * @return the clientSendBufferSize
	 */
	public int getClientSendBufferSize() {
		return clientSendBufferSize;
	}

	/**
	 * @param clientSendBufferSize the clientSendBufferSize to set
	 */
	public void setClientSendBufferSize(int clientSendBufferSize) {
		this.clientSendBufferSize = clientSendBufferSize;
	}

	/**
	 * @return the authentication
	 */
	public boolean isAuthentication() {
		return authentication;
	}

	/**
	 * @param authentication the authentication to set
	 */
	public void setAuthentication(boolean authentication) {
		this.authentication = authentication;
	}

	/**
	 * @return the whiteList
	 */
	public List getWhiteList() {
		return whiteList;
	}

	/**
	 * @param whiteList the whiteList to set
	 */
	public void setWhiteList(List whiteList) {
		this.whiteList = whiteList;
	}

	/**
	 * @return the blackList
	 */
	public List getBlackList() {
		return blackList;
	}

	/**
	 * @param blackList the blackList to set
	 */
	public void setBlackList(List blackList) {
		this.blackList = blackList;
	}

	/**
	 * @return the workerThreadFactory
	 */
	public IWorkerThreadFactory getWorkerThreadFactory() {
		return workerThreadFactory;
	}

	/**
	 * @param workerThreadFactory the workerThreadFactory to set
	 */
	public void setWorkerThreadFactory(IWorkerThreadFactory workerThreadFactory) {
		this.workerThreadFactory = workerThreadFactory;
	}
}
