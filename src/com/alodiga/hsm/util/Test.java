package com.alodiga.hsm.util;

import com.alodiga.hsm.OmniCryptoCommand;
import com.alodiga.hsm.exception.NotConnectionHSMException;
import com.alodiga.hsm.response.GenerateCVVResponse;
import com.alodiga.hsm.response.GenerateKeyResponse;
import com.alodiga.hsm.response.GenericResponse;
import com.alodiga.hsm.response.HSMStatusResponse;
import com.alodiga.hsm.response.IBMOfSetResponse;


public class Test {

	public static void main(String[] args) {
	
		//validateAllProccess();
		
  	//a) llaves que adminite este método (KEK,KWP,PVK,CAK,CVK,CAK)
			try {
				GenerateKeyResponse responseKey = new GenerateKeyResponse();
				responseKey = OmniCryptoCommand.generateKey("KWP","Single");
				System.out.println("----------------------------------------------");
				System.out.println("key: "+responseKey.getKeyValue());
				System.out.println("header: "+ responseKey.getHeader());
				System.out.println("virificationDigit: "+ responseKey.getVerificationDigit());
				System.out.println("----------------------------------------------");
			} catch (Exception e) {
				e.printStackTrace();
			}		

			//////////////////////////////////////////////////////////////////
//			///////////////////////////////////KVC//////////////////////////////
		    /////////////////////////////////////////////////////////////////////
			
//			try {
//				GenerateKeyResponse responseKey = new GenerateKeyResponse();
//				System.out.println("----------------------------------------------");
//				responseKey = HSMOperations.generateDoubleKVC();
//				System.out.println("----------------------------------------------");
//				System.out.println("key: "+responseKey.getKeyValue());
//				System.out.println("header: "+ responseKey.getHeader());
//				System.out.println("virificationDigit: "+ responseKey.getVerificationDigit());
//				System.out.println("----------------------------------------------");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
		
		
		//////////////////////////////////////////////////////////////////
//		///////////////////////////////////CVV//////////////////////////////
	    ///////////////////////////////////////////////////////////////////
		
//		try {
//			GenerateCVVResponse responseKey = new GenerateCVVResponse();
//			System.out.println("----------------------------------------------");
//			responseKey = HSMOperations.generateCVV("EC338F63A9ADFCF8B484745B4199DE10","54123456789012347","8701","201");
//			System.out.println("----------------------------------------------");
//			System.out.println("key: "+responseKey.getKeyValue());
//			System.out.println("header: "+ responseKey.getHeader());
//			System.out.println("CVV: "+ responseKey.getCvv());
//			System.out.println("----------------------------------------------");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
		//b) Genera el digito de chequeo 
        //GenerateaKeyCheckValue("ECAB0BBF46CA06AF661B2D486290E7BF");
		//c) 
//		exportKey("913849889255C605", "50E24F36ADE5E958");
//		 try {
////			OmniCryptoCommand.exportKey("ECAB0BBF46CA06AF661B2D486290E7BF", "D1930F74F2CD7880");
//		} catch (Exception e) {
	    // TODO Auto-generated catch block
        //			e.printStackTrace();
        //		}
        // d) translatePINZPKToLMK 
		// String responsePinELMK;
	    // responsePinELMK = translatePINZPKToLMK("75D5BD6ACB4FB723", "820008415730", "274474634FBFF3FF","Single");
		// System.out.println("responsePinELMK="+responsePinELMK);
		//e) generateIBMPinOffSet	
        IBMOfSetResponse ibmOfSetResponse =  generateIBMPinOffSet("04321", "820008415730");
		System.out.println("ibmOfSetResponse="+ibmOfSetResponse.getIBMoffset());
	}
	
	
	
    private static GenericResponse getHsmFinware(){
		String firmwareResponse = "";
		try {
			firmwareResponse = OmniCryptoCommand.ProcessRequest(Integer.parseInt(Constant.GET_HSMFIRMWARE_OPERATOR), null); 			
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return new GenericResponse(ConstantResponse.FORMAT_ERROR_RESPONSE_CODE,ConstantResponse.FORMAT_ERROR_RESPONSE_MESSAGE,null);	
		}catch (NotConnectionHSMException e) {
			e.printStackTrace();
			return new GenericResponse(ConstantResponse.HSM_NOT_AVAILABLE_ERROR_RESPONSE_CODE,ConstantResponse.HSM_NOT_AVAILABLE_RROR_RESPONSE_MESSAGE,"");
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse(ConstantResponse.ERROR_RESPONSE_CODE,ConstantResponse.ERROR_RESPONSE_MESSAGE,null);
		}
        return new GenericResponse(ConstantResponse.SUCESSFULL_RESPONSE_CODE,ConstantResponse.SUCESSFULL_RESPONSE_MESSAGE,firmwareResponse.toString().trim());
    }
    
    
    private static HSMStatusResponse getHSMStatus(){
		String hsmStatusResponse = "";
		try {
			hsmStatusResponse = OmniCryptoCommand.ProcessRequest(Integer.parseInt(Constant.GET_HSM_STATUS_OPERATOR), null); 			
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return new HSMStatusResponse(ConstantResponse.FORMAT_ERROR_RESPONSE_CODE,ConstantResponse.FORMAT_ERROR_RESPONSE_MESSAGE,null);	
		} catch (NotConnectionHSMException e) {
			e.printStackTrace();
			return new HSMStatusResponse(ConstantResponse.HSM_NOT_AVAILABLE_ERROR_RESPONSE_CODE,ConstantResponse.HSM_NOT_AVAILABLE_RROR_RESPONSE_MESSAGE,"");
		}catch (Exception e) {
			e.printStackTrace();
			return new HSMStatusResponse(ConstantResponse.ERROR_RESPONSE_CODE,ConstantResponse.ERROR_RESPONSE_MESSAGE,null);
		}
        return new HSMStatusResponse(ConstantResponse.SUCESSFULL_RESPONSE_CODE,ConstantResponse.SUCESSFULL_RESPONSE_MESSAGE,hsmStatusResponse.toString().trim());
    }
    

    private static  IBMOfSetResponse generateIBMPinOffSet(
    		String pinELMK,
    		String pan){
		StringBuilder requestHSM = new StringBuilder();
		String offSetResponse;
		try {
			requestHSM.append(pinELMK);
			requestHSM.append(",");
			requestHSM.append(pan);
			requestHSM.append(",");
			//TODO:Comentar a Alvaro que falta una base de datos
			 offSetResponse  = OmniCryptoCommand.ProcessRequest(Integer.parseInt(Constant.GENERATE_IBM_PIN_OFF_SET), requestHSM.toString()); 			
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return new IBMOfSetResponse(ConstantResponse.FORMAT_ERROR_RESPONSE_CODE,ConstantResponse.FORMAT_ERROR_RESPONSE_MESSAGE,null);	
		} catch (Exception e) {
			e.printStackTrace();
			return new IBMOfSetResponse(ConstantResponse.ERROR_RESPONSE_CODE,ConstantResponse.ERROR_RESPONSE_MESSAGE,null);
		}
        return new IBMOfSetResponse(ConstantResponse.SUCESSFULL_RESPONSE_CODE,ConstantResponse.SUCESSFULL_RESPONSE_MESSAGE,offSetResponse);
    }
    
    private static String translatePINZPKToLMK(String pinBlock, String pan,String kwp,String Longer){
		StringBuilder requestHSM = new StringBuilder();
		String traslateResponse = "";
		requestHSM.append(pinBlock);
		requestHSM.append(",");
		requestHSM.append(pan);
		requestHSM.append(",");
		requestHSM.append(kwp);
		requestHSM.append(",");
		requestHSM.append(Longer);
		try {
			 traslateResponse  = OmniCryptoCommand.ProcessRequest(Integer.parseInt(Constant.TRASLATE_PIN_FROM_KWP_TO_MFK), requestHSM.toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();	
		}
		return traslateResponse.substring(4,9);
        
    }
    
    
    private static void GenerateaKeyCheckValue(String kek){
		StringBuilder requestHSM = new StringBuilder();
		String resultValidatePin = "";
			requestHSM.append(kek);
			requestHSM.append(",");
			
			try {
				resultValidatePin  = OmniCryptoCommand.ProcessRequest(Integer.parseInt(Constant.GENERATE_KEY_CHECK_VALUE), requestHSM.toString());
			} catch (Exception e) {
		
				e.printStackTrace();
			}
			
			System.out.println("-------------------------------------------------------------------");
			System.out.println("resultValidatePin "+ resultValidatePin);
    }
    
    private static void exportKey(String kek, String kwp){
		StringBuilder requestHSM = new StringBuilder();
		String resultValidatePin = "";
			requestHSM.append(kek);
			requestHSM.append(",");
			requestHSM.append(kwp);
			try {
				resultValidatePin  = OmniCryptoCommand.ProcessRequest(Integer.parseInt(Constant.EXPORT_KEY), requestHSM.toString());
			} catch (Exception e) {
		
				e.printStackTrace();
			}
			
			System.out.println("-------------------------------------------------------------------");
			System.out.println("resultValidatePin "+ resultValidatePin);
    }
    
    private static void validateAllProccess() {
//    	
//    	String kekKey = "";
//    	String kwpKey = "";
//    	//////////////////////////.1 Generate KEK///////////////////////////////////////
//		try {
//			GenerateKeyResponse responseKey = new GenerateKeyResponse();
//			responseKey = OmniCryptoCommand.generateKey("KEK","Double");
//			System.out.println("----------------------------------------------");
//			System.out.println("key: "+responseKey.getKeyValue());
//			System.out.println("header: "+ responseKey.getHeader());
//			System.out.println("virificationDigit: "+ responseKey.getVerificationDigit());
//			System.out.println("----------------------------------------------");
//			//kekKey = responseKey.getKeyValue();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//		/////////////////////////////////////////////////////////////////////////////
//    	//////////////////////////.2 Generate KWP///////////////////////////////////////
//		try {
//			GenerateKeyResponse responseKey = new GenerateKeyResponse();
//			responseKey = OmniCryptoCommand.generateKey("KWP","Single");
//			System.out.println("----------------------------------------------");
//			System.out.println("key: "+responseKey.getKeyValue());
//			System.out.println("header: "+ responseKey.getHeader());
//			System.out.println("virificationDigit: "+ responseKey.getVerificationDigit());
//			System.out.println("----------------------------------------------");
//			kwpKey = responseKey.getKeyValue();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//		/////////////////////////////////////////////////////////////////////////////
//    	//////////////////////////.3 Check DigitKEK///////////////////////////////////////
//    	
//		GenerateaKeyCheckValue(kekKey);
//		
//		/////////////////////////////////////////////////////////////////////////////
//    	//////////////////////////.4 ExportKey///////////////////////////////////////
//    	
//		exportKey(kekKey, kwpKey);
//		
//		
//		System.out.println("kwpKey="+kwpKey);
//		
//		////////////////////////////////////////////////////////////////////////////////
//		//////////////////////////////////////////////////////////////////////////////////
//		////////////////////////////////////////////////////////////////////////////////
//		//Segunda Parte
//		//////////////////////////////////////////////////////////////////////////////////
//		////////////////////////////////////////////////////////////////////////////////
//		//////////////////////////////////////////////////////////////////////////////////
//		
		
		String pinBlock = Utils.getPinblock("E5614FF24C765137", "2822", "501878200084157306");
		
//		System.out.println("pinBlock="+pinBlock);
		
		translatePINZPKToLMK(pinBlock, "820008415730", "B563D6ABD6692220","Single");
		//generateIBMPinOffSet("02822", "820008415730", "2", "D");
    }
    
    
    
	

}
