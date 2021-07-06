package com.alodiga.hsm.util;

public class ConstantTlv {


	//	Cryptogram returned by the ICC in response of the GENERATE AC command
	public static final String APPLICATION_CRYPTOGRAM = "9F26";
	
	// Indicates the currency code of the transaction according to ISO 4217
	public static final String TRANSACTION_CURRENCY_CODE = "5F2A";
	
	//Indicates the type of financial transaction, represented by the first two digits of ISO 8583:1987 Processing Code
	public static final String TRANSACTION_TYPE = "9C";
	
	//Local date that the transaction was authorised
	public static final String TRANSACTION_DATE = "9A";
	
	//Value to provide variability and uniqueness to the generation of a cryptogram
	public static final String UNPREDICTABLE_NUMBER = "9F37";
	
	//Indicates the capabilities of the card to support specific functions in the application
	public static final String APLICATION_INTERCHANGE_PROFILE = "82";
	
	//Counter maintained by the application in the ICC (incrementing the ATC is managed by the ICC)
	public static final String APLICATION_TRANSACTION_COUNTER = "9F36";
	
	//Unique and permanent serial number assigned to the IFD by the manufacturer
	public static final String INTERFACE_DEVICE_SERIAL_NUMBER = "9F1E";
	
	//Indicates the environment of the terminal, its communications capability, and its operational control	
	public static final String TERMINAL_TYPE = "9F35";
	
	//Indicates the results of the last CVM performed
	public static final String CARD_HOLDER_VERIFICATION_METHOD = "9F34";
	
	//Terminal Capabilities
	public static final String TERMINAL_CAPABILITIES = "9F33";
	
	//Status of the different functions as seen from the terminal
	public static final String TERMINAL_VERIFICATION_RESULT = "95";
	
	//Secondary amount associated with the transaction representing a cashback amount
	public static final String OTHER_AMOUNT = "9F03";
	
	//Indicates the country of the terminal, represented according to ISO 3166
	public static final String TERMINAL_COUNTRY_CODE = "9F1A";
	
	//Authorised amount of the transaction (excluding adjustments)
	public static final String AMOUNT_AUTORISED = "9F02";
	
	//Contains proprietary application data for transmission to the issuer in an online transaction
	public static final String ISSUER_APLICATION_DATA = "9F10";
	
	//Indicates the type of cryptogram and the actions to be performed by the terminal
	public static final String CRYPTOGRAM_INFORMATION_DATA = "9F27";
	
	//Identifies and differentiates cards with the same PAN
	public static final String PSN = "5F34";
	
	
	
	
    //MODE HSM ARPC QRQC result 
    //0 = Perform ARQC verification only
    //1 = Perform ARQC Verification and ARPC 
    public static final String MODE = "1";


	//Identifies and differentiates cards with the same PAN
	public static final String ARC_TEMPORAL_CONSTANT = "0012";
    
    
  //Header from HSM thales
  	public static final String HSM_HEADER = "00003000";
  	
  	
  	//Header from HSM delimiter value
    public static final String HSM_DELIMITER_COMAND = ";";
    
    
    public static final String fieldEmptyMessage = "El campo es vacio :";
      
	
 // Indicates the currency code of the transaction according to ISO 4217
 	public static final String VISA_VALUE_SCHEMEID = "0";
 	public static final String MASTER_VALUE_SCHEMEID = "1";
	
	
	
	
	
	
	
	
	
	
	
	
}
