package com.sample.server.proxy.thread;

import java.net.Socket;

/*
 * The worker thread factory interface.
 * 
 * Worker Thread Factorys must implement this interface.
 * 
 */
public interface IWorkerThreadFactory {
	
	public IWorkerThread createNewInstance(Socket clientConnection, Socket serverConnection, int maxBufferSize);

}
