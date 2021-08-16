package com.alodiga.hsm.data.object;

public class DataEMVField {
	
	   /**
	    *@author kerwin Gomez
	    *Esta  Clase dispone de los elementos de información mínimos necesarios para generar un criptograma de
	    *Cierre a parte de un criptograma de apertura valido.  
	    */

	//	Cryptogram returned by the ICC in response of the GENERATE AC command
	private String applicationCryptogram;
	
	// Indicates the currency code of the transaction according to ISO 4217
	private String TransactionCurrencyCode;
	


	//Indicates the type of financial transaction, represented by the first two digits of ISO 8583:1987 Processing Code
	private String TransactionType;
	
	//Local date that the transaction was authorised
	private String TransactionDate;
	
	//Value to provide variability and uniqueness to the generation of a cryptogram
	private String unpredictableNumber;
	
	//Indicates the capabilities of the card to support specific functions in the application
	private String aplicationInterchangeProfile;
	
	//Counter maintained by the application in the ICC (incrementing the ATC is managed by the ICC)
	private String aplicationTransactionCounter;;
	
	//Unique and permanent serial number assigned to the IFD by the manufacturer
	private String interfaceDeviceSerialNumber;
	
	//Indicates the environment of the terminal, its communications capability, and its operational control	
	private String terminalType;
	
	//Indicates the results of the last CVM performed
	private String cardHolderVerificationMethod;
	
	//Terminal Capabilities
	private String terminalCapabilities;
	
	//Status of the different functions as seen from the terminal
	private String terminalVerificationResult;
	
	//Secondary amount associated with the transaction representing a cashback amount
	private String otherAmount;
	
	//Indicates the country of the terminal, represented according to ISO 3166
	private String terminalCountryCode;
	
	//Authorised amount of the transaction (excluding adjustments)
	private String amount;
	
	//Contains proprietary application data for transmission to the issuer in an online transaction
	private String isuuerAplicationDate;
	
	//Indicates the type of cryptogram and the actions to be performed by the terminal
	private String cryptogramAplicationData;
	
	//Identifies and differentiates cards with the same PAN
	private String panSerialNumber;

	public String getApplicationCryptogram() {
		return applicationCryptogram;
	}

	public void setApplicationCryptogram(String applicationCryptogram) {
		this.applicationCryptogram = applicationCryptogram;
	}

	public String getTransactionCurrencyCode() {
		return TransactionCurrencyCode;
	}

	public void setTransactionCurrencyCode(String transactionCurrencyCode) {
		TransactionCurrencyCode = transactionCurrencyCode;
	}

	public String getTransactionType() {
		return TransactionType;
	}

	public void setTransactionType(String transactionType) {
		TransactionType = transactionType;
	}

	public String getTransactionDate() {
		return TransactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		TransactionDate = transactionDate;
	}

	public String getUnpredictableNumber() {
		return unpredictableNumber;
	}

	public void setUnpredictableNumber(String unpredictableNumber) {
		this.unpredictableNumber = unpredictableNumber;
	}

	public String getAplicationInterchangeProfile() {
		return aplicationInterchangeProfile;
	}

	public void setAplicationInterchangeProfile(String aplicationInterchangeProfile) {
		this.aplicationInterchangeProfile = aplicationInterchangeProfile;
	}

	public String getAplicationTransactionCounter() {
		return aplicationTransactionCounter;
	}

	public void setAplicationTransactionCounter(String aplicationTransactionCounter) {
		this.aplicationTransactionCounter = aplicationTransactionCounter;
	}

	public String getInterfaceDeviceSerialNumber() {
		return interfaceDeviceSerialNumber;
	}

	public void setInterfaceDeviceSerialNumber(String interfaceDeviceSerialNumber) {
		this.interfaceDeviceSerialNumber = interfaceDeviceSerialNumber;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getCardHolderVerificationMethod() {
		return cardHolderVerificationMethod;
	}

	public void setCardHolderVerificationMethod(String cardHolderVerificationMethod) {
		this.cardHolderVerificationMethod = cardHolderVerificationMethod;
	}

	public String getTerminalCapabilities() {
		return terminalCapabilities;
	}

	public void setTerminalCapabilities(String terminalCapabilities) {
		this.terminalCapabilities = terminalCapabilities;
	}

	public String getTerminalVerificationResult() {
		return terminalVerificationResult;
	}

	public void setTerminalVerificationResult(String terminalVerificationResult) {
		this.terminalVerificationResult = terminalVerificationResult;
	}

	public String getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(String otherAmount) {
		this.otherAmount = otherAmount;
	}

	public String getTerminalCountryCode() {
		return terminalCountryCode;
	}

	public void setTerminalCountryCode(String terminalCountryCode) {
		this.terminalCountryCode = terminalCountryCode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getIsuuerAplicationDate() {
		return isuuerAplicationDate;
	}

	public void setIsuuerAplicationDate(String isuuerAplicationDate) {
		this.isuuerAplicationDate = isuuerAplicationDate;
	}

	public String getCryptogramAplicationData() {
		return cryptogramAplicationData;
	}

	public void setCryptogramAplicationData(String cryptogramAplicationData) {
		this.cryptogramAplicationData = cryptogramAplicationData;
	}

	public String getPanSerialNumber() {
		return panSerialNumber;
	}

	public void setPanSerialNumber(String panSerialNumber) {
		this.panSerialNumber = panSerialNumber;
	}
	
	
	
	@Override
	public String toString() {
		return "DataEMVField [applicationCryptogram=" + applicationCryptogram + ", TransactionCurrencyCode="
				+ TransactionCurrencyCode + ", TransactionType=" + TransactionType + ", TransactionDate="
				+ TransactionDate + ", unpredictableNumber=" + unpredictableNumber + ", aplicationInterchangeProfile="
				+ aplicationInterchangeProfile + ", aplicationTransactionCounter=" + aplicationTransactionCounter
				+ ", interfaceDeviceSerialNumber=" + interfaceDeviceSerialNumber + ", terminalType=" + terminalType
				+ ", cardHolderVerificationMethod=" + cardHolderVerificationMethod + ", terminalCapabilities="
				+ terminalCapabilities + ", terminalVerificationResult=" + terminalVerificationResult + ", otherAmount="
				+ otherAmount + ", terminalCountryCode=" + terminalCountryCode + ", amount=" + amount
				+ ", isuuerAplicationDate=" + isuuerAplicationDate + ", cryptogramAplicationData="
				+ cryptogramAplicationData + ", panSerialNumber=" + panSerialNumber + "]";
	}

	public Integer getTransactionDataLenght() {
		return getTransactionData().length()/2;
	}
	
	public String getTransactionData() {
		StringBuilder builderTransactionData = new StringBuilder(this.amount);
		builderTransactionData.append(this.otherAmount).append(this.terminalCountryCode).
		append(this.terminalVerificationResult).append(this.TransactionCurrencyCode).append(this.TransactionDate)
		.append(this.TransactionType).append(this.unpredictableNumber).append(this.aplicationInterchangeProfile)
		.append(this.aplicationTransactionCounter).append(this.isuuerAplicationDate);
		String val = builderTransactionData.toString();
		if(val.length()%2 != 0 ) {
			val += "0";
		}
		Integer valByte = val.length() / 2;
		System.out.println(valByte);
		if (valByte % 8 == 0) {
			return val;
		} else {
			while (valByte % 8 != 0) {
				val += "00";
				valByte++;
			}
			return val;
		}
	}
}
