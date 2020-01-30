package com.sample.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sample.client.exceptions.NumberServiceException;

public class InvokeProxyServerThread extends Thread {
	
	public static final String proxyUrl = "http://localhost:2000/server-0.0.1-SNAPSHOT/NumberServlet?service=addNumbers&number1=2&number2=8";
		
	private final Logger logger = Logger.getLogger(InvokeProxyServerThread.class.getName());
	private Result result = null;
	
	public InvokeProxyServerThread(Result result) {
		this.result = result;
	}
	
	
	public void run() {
		
		String line = null;
		URL url = null;
		
		//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 2000));
		
		try {
			
			logger.log(Level.INFO, "Executing client thread : [" + Thread.currentThread().getName() + "]");
			
			url = new URL(proxyUrl);

			HttpURLConnection uc;
				uc = (HttpURLConnection)url.openConnection();
				uc.connect(); 
			
			StringBuffer tmp = new StringBuffer();
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(uc.getInputStream()));

			String resultString = result.getValue();
			
			while ((line = in.readLine()) != null) {
				resultString = resultString + line;
			}

			uc.disconnect();
			
			result.setValue(resultString);
			
			logger.log(Level.INFO, "Retrieved result : [" + resultString + "] from thread [" + Thread.currentThread().getName() + "]");
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
