package com.alodiga.hsm.response;

import com.alodiga.hsm.data.object.DataEMVField;

public class ARQCEmvDataResponse extends GeneralResponse {
	
	private String responseCode;
	private String responseMessage;
	private DataEMVField dataemvField;
	

	public ARQCEmvDataResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ARQCEmvDataResponse(String responseCode,String responseMessage,DataEMVField dataObject) {
		super();
		
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.dataemvField = dataObject;
	     
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public DataEMVField getDataemvField() {
		return dataemvField;
	}

	public void setDataemvField(DataEMVField dataemvField) {
		this.dataemvField = dataemvField;
	}

	@Override
	public String toString() {
		return "ARQCEmvDataResponse [responseCode=" + responseCode + ", responseMessage=" + responseMessage
				+ ", dataemvField=" + dataemvField.toString() + "]";
	}

}
