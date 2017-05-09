package com.iot.utility;

public class Response {

	public ResponseDetails response;
	
	public ResponseDetails getResponse() {
		return response;
	}
	/**
	 * 
	 * @param response the response to set
	 */
	public void setResponse(ResponseDetails response) {
		this.response = response;
	}
	/**
	 * @return the response
	 */
	public Response(){
		response = new ResponseDetails();
	}
}
