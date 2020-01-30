/**
 * 
 */
package com.sample.client;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

/**
 * @author smcardle
 *
 *
 */
public class InvokeProxyServerThreadTest {

	private final Logger logger = Logger.getLogger(InvokeProxyServerThreadTest.class.getName());
	
	@Test
	public void testInvokeProxyServer() {
		
		boolean passedTest = false;
		Result threadResult = new Result("");
		Result thread1Result = new Result("");
		Result thread2Result = new Result("");
		Result thread3Result = new Result("");
		Result thread4Result = new Result("");
		Result thread5Result = new Result("");
		Result thread6Result = new Result("");
		Result thread7Result = new Result("");
		Result thread8Result = new Result("");
		
		try {
			
			InvokeProxyServerThread thread = null;
			InvokeProxyServerThread thread1 = null;
			InvokeProxyServerThread thread2 = null;
			InvokeProxyServerThread thread3 = null;
			InvokeProxyServerThread thread4 = null;
			InvokeProxyServerThread thread5 = null;
			InvokeProxyServerThread thread6 = null;
			InvokeProxyServerThread thread7 = null;
			InvokeProxyServerThread thread8 = null;
			
			thread = new InvokeProxyServerThread(threadResult);
			thread.start();
			
			thread1 = new InvokeProxyServerThread(thread1Result);
			thread1.start();
			
			thread2 = new InvokeProxyServerThread(thread2Result);
			thread2.start();
			
			thread3 = new InvokeProxyServerThread(thread3Result);
			thread3.start();
			
			thread4 = new InvokeProxyServerThread(thread4Result);
			thread4.start();
			
			thread5 = new InvokeProxyServerThread(thread5Result);
			thread5.start();
			
			thread6 = new InvokeProxyServerThread(thread6Result);
			thread6.start();
			
			thread7 = new InvokeProxyServerThread(thread7Result);
			thread7.start();
			
			thread8 = new InvokeProxyServerThread(thread8Result);
			thread8.start();
			
			thread.join();
			thread1.join();
			thread2.join();
			thread3.join();
			thread4.join();
			thread5.join();
			thread6.join();
			thread7.join();
			thread8.join();

			assertTrue("The sum of the two numbers is : 10".equalsIgnoreCase(threadResult.getValue().trim()));
			assertTrue("The sum of the two numbers is : 10".equalsIgnoreCase(thread1Result.getValue().trim()));
			assertTrue("The sum of the two numbers is : 10".equalsIgnoreCase(thread2Result.getValue().trim()));
			assertTrue("The sum of the two numbers is : 10".equalsIgnoreCase(thread3Result.getValue().trim()));
			assertTrue("The sum of the two numbers is : 10".equalsIgnoreCase(thread4Result.getValue().trim()));
			assertTrue("The sum of the two numbers is : 10".equalsIgnoreCase(thread5Result.getValue().trim()));
			assertTrue("The sum of the two numbers is : 10".equalsIgnoreCase(thread6Result.getValue().trim()));
			assertTrue("The sum of the two numbers is : 10".equalsIgnoreCase(thread7Result.getValue().trim()));
			assertTrue("The sum of the two numbers is : 10".equalsIgnoreCase(thread8Result.getValue().trim()));
			       
			logger.log(Level.INFO, "PASSED TEST");
			
			passedTest = true;
			
		} catch (Exception ex) {
			passedTest = false;
			logger.log(Level.SEVERE, "Error executing thread : " + ex);
		}
		
		if (!passedTest) fail("The InvokeProxyServerThreadTest failed.");
	}
	
}
