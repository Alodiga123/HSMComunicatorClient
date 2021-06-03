package com.alodiga.hsm.response;

public class GenerateCVVResponse extends GeneralResponse {

	private String cvv;
	private String header;
	private String keyValue;
	
	


	public GenerateCVVResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GenerateCVVResponse(String responseCode,String responseMessage,String response,String lenghtKey) {
		super();
		
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.cvv = response.substring(response.length()-5);
		System.out.println("cvv:"+cvv);
		
		this.header = response.substring(0,4) ;
	     
	}
	
	public GenerateCVVResponse(String responseCode,String responseMessage) {
		super();

		this.responseCode = responseCode;
		this.responseMessage = responseMessage;

	}


	
	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	@Override
	public String toString() {
		return "GenerateKeyResponse [cvv=" + cvv + ", header=" + header + ", keyValue="
				+ keyValue + "]";
	}



}
