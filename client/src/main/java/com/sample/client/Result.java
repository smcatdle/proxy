/**
 * 
 */
package com.sample.client;

/**
 * @author smcardle
 *
 */
public class Result {

	private String value = null;
	
	public Result(String value) {
		this.value = value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
}
