/**
 * 
 */
package com.sample.server.proxy.thread;

import java.net.Socket;

/**
 * @author smcardle
 * 
 * Simple Worker Thread Factory implementation.
 *
 */
public class SimpleWorkerThreadFactory implements IWorkerThreadFactory {

	/*
	 * Constructor
	 */
	private SimpleWorkerThreadFactory() {
		
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.company.server.thread.IWorkerThreadFactory#createNewInstance(java.net.Socket, java.net.Socket, int)
	 */
	public IWorkerThread createNewInstance(Socket clientConnection, Socket serverConnection, int maxBufferSize) {
		
		return new SimpleWorkerThread(clientConnection, serverConnection, maxBufferSize);
	}
}
