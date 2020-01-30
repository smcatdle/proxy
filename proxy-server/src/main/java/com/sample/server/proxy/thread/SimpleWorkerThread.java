/**
 * 
 */
package com.sample.server.proxy.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author smcardle
 * 
 * The proxy server has been implemented to be multithreaded such that requests can be processed in parallel.
 * There is a thread pool which will create new worker threads to handle each new client request up to the configured
 * threadPoolSize property. When a worker thread has completed its allocated work, it will be returned to the pool until 
 * there is another client request to be processed. The thread pool improves performance as there is a cost in creating new threads.
 *
 */
public class SimpleWorkerThread extends Thread implements IWorkerThread {
	
	private final static Logger logger = Logger.getLogger(SimpleWorkerThread.class .getName());
	
	private Socket inputSocket = null;
	
	private Socket outputSocket = null;
	
	private int bufferSize = 0;
	
	/*
	 * Constructor
	 * 
	 * @param in the input socket
	 * @param out the output socket
	 * @param bufferSize the buffer size
	 */
	public SimpleWorkerThread(Socket in, Socket out, int bufferSize) {
		this.inputSocket = in;
		this.outputSocket = out;
		this.bufferSize = bufferSize;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
		InputStream inputStream =null;
		OutputStream outputStream = null;
		byte[] buffer = null;
		
		try {
			
			// check not null
			if (inputSocket != null && outputSocket != null && bufferSize > 0) {
				
				buffer = new byte[bufferSize];
				
				logger.info("New incoming client connection...");
				
				inputStream = inputSocket.getInputStream();
				outputStream = outputSocket.getOutputStream();
				
				// read bytes from input stream and write to output stream.
				while(inputStream.read(buffer, 0, bufferSize) > 0) {
					outputStream.write(buffer);
				}
				
				// flush the output stream.
				outputStream.flush();
	
				logger.info("Worker thread has completed allocated work..");
			}
			
		} catch (IOException e) {
			logger.severe(e.getMessage());
		} catch (Exception ex) {
			logger.severe(ex.getMessage());

		} finally {
			
			// close the streams and connections.
			try {
				if (outputStream != null) outputStream.close();
				if (inputStream != null) inputStream.close();
				if (inputSocket != null) inputSocket.close();
				if (outputSocket != null) outputSocket.close();
			} catch (IOException e) {
				logger.severe("Error closing streams and sockets : " + e);
			}
			
		}
	}

}
