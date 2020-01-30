package com.sample.server.servlet.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NumberServlet
 */
public class NumberServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	private static final String SERVICE = "service";
	
	private static final String SERVICE_ADD_NUMBERS = "addNumbers";
	
	private static final String SERVICE_ADD_NUMBERS_ARG_1 = "number1";
	
	private static final String SERVICE_ADD_NUMBERS_ARG_2 = "number2";
	
	private static final String UTF_8 = "UTF-8";
	
	private final static Logger logger = Logger.getLogger(NumberServlet.class .getName());
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NumberServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String answer = null;
		String service = null;
		PrintWriter writer = null;
		String parameter1 = null;
		String parameter2 = null;
		int number1 = 0;
		int number2 = 0;
		
		service = request.getParameter(NumberServlet.SERVICE);
		
		writer = response.getWriter();
		
		// A simple service to add two numbers.
		if (NumberServlet.SERVICE_ADD_NUMBERS.equals(service)) {
			parameter1 = request.getParameter(NumberServlet.SERVICE_ADD_NUMBERS_ARG_1);
			parameter2 = request.getParameter(NumberServlet.SERVICE_ADD_NUMBERS_ARG_2);
			
			if (parameter1 != null && parameter2 != null) {	
				number1 = Integer.parseInt(parameter1);
				number2 = Integer.parseInt(parameter2);
				answer = "The sum of the two numbers is : " + (number1 + number2);
			} else {
				answer = "Invalid arguments to the service : addNumbers";
			}
		} else {
			answer = "service : " + service + " is not recognised";
		}
		
		logger.info("response : [" + answer + "] from service : " + service);
		
		writer.print(answer);
		writer.close();
		response.setContentType(NumberServlet.UTF_8);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
