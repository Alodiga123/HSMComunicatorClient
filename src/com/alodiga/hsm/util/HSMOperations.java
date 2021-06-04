/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.hsm.util;
import com.alodiga.hsm.CryptoConnection;
import com.alodiga.hsm.OmniCryptoCommand;
import com.alodiga.hsm.ThalesCryptoCommand;
import com.alodiga.hsm.UnpackThalesCryptoCommand;
import com.alodiga.hsm.exception.NotConnectionHSMException;
import com.alodiga.hsm.response.GenerateCVVResponse;
import com.alodiga.hsm.response.GenerateKeyResponse;
import com.alodiga.hsm.response.GenericResponse;
import com.alodiga.hsm.response.HSMStatusResponse;
import com.alodiga.hsm.response.IBMOfSetResponse;

/**
 *
 * @author jose
 */
public class HSMOperations {
    
    public static GenerateKeyResponse generateKey(String keyType, String lenght) throws Exception {
	if ((keyType.equals("KEK")) || (keyType.equals("TPK")) || (keyType.equals("KWP"))|| (keyType.equals("PVK")) || (keyType.equals("CAK")) || (keyType.equals("CVK"))) {
            String requestMessage = "";
            String responseMessage = "";
            requestMessage = CryptoConnection.sendAndReceiveToHSM(ThalesCryptoCommand.generateKey(lenght, keyType));
            responseMessage = UnpackThalesCryptoCommand.unpackGenerateKey(requestMessage);
	    return new GenerateKeyResponse(Constant.SUCCESS, Constant.SUCCESS, responseMessage, lenght);		  
	} else {
            return new GenerateKeyResponse(Constant.KEY_NOT_SUPPORT, Constant.KEY_NOT_SUPPORT_MESSAGE);
	} 
    }
    
    public static GenerateKeyResponse generateSingleKVC() throws Exception {
    	String requestMessage = "";
    	String responseMessage = "";
    	requestMessage = CryptoConnection.sendAndReceiveToHSM(ThalesCryptoCommand.generateKVC());
    	responseMessage = UnpackThalesCryptoCommand.unpackGenerateKey(requestMessage);
    	return new GenerateKeyResponse(Constant.SUCCESS, Constant.SUCCESS, responseMessage,"Single");		
     }
    

    /**
     *@author kerwin Gomez
     *@param CVK o KVC: parametro con la lleve generado y almacenado previamente por la plataforma
     *@param Pan:Numero de Tarjeta
     *@param EndingDate:Fecha de vencimiento de la tarjeta en Formato YYMM
     *@param ServicesCode
     *@return retorna el cvv  de la tarjeta dependiendo del service code o el cvv que se va a validar
     *  //ICVV EMVchip 999, CVV2 Pintado Atras 000, CVV1 201 Banda de la tarjeta  Field 22 , 35

		SubField1 

		PAN entry mode (positions 1 - 2)
		00 – Unknown
		01 – Manual (i.e keypad)===> 000
		02 – Magnetic Stripe (possibly constructed manually, CVV may be checked) ====> 201
		03 – Barcode ====> 000
		04 – OCR ====>
		05 – ICC (CVV may be checked) ==> 999
		07 – Auto - entry via contactless ICC
		90 – Magnetic strip as read from track 2
		91 – Auto - entry via contactless magnetic stripe
		95 – ICC (CVV may not be checked)
		This Value intro field 35
     *    
     */
    
    public static GenerateCVVResponse generateCVV(String cvk,String pan,String endingDate,String serviceCode) throws Exception {
    	String requestMessage = "";
    	String responseMessage = "";
    	requestMessage = CryptoConnection.sendAndReceiveToHSM(ThalesCryptoCommand.generateCVV(cvk,pan,endingDate,serviceCode));
    	responseMessage = UnpackThalesCryptoCommand.unpackGenerateKey(requestMessage);
    	return new GenerateCVVResponse(Constant.SUCCESS, Constant.SUCCESS, responseMessage, "Single");		  
    }
    
    /**
     *@author kerwin Gomez
     *@return retorna una llave conpuesta por la concatenación de 2 llaves CVK simples 
     */
    public static GenerateKeyResponse generateDoubleKVC() throws Exception {
    	GenerateKeyResponse response1 = new GenerateKeyResponse();
    	response1 = HSMOperations.generateSingleKVC();
    	GenerateKeyResponse response2 = new GenerateKeyResponse();
    	response2 = HSMOperations.generateSingleKVC();
    	GenerateKeyResponse responseComplete= new GenerateKeyResponse();
    	responseComplete.setVerificationDigit(response2.getVerificationDigit());
    	responseComplete.setKeyValue(response1.getKeyValue()+response2.getKeyValue());
    	responseComplete.setResponseCode(Constant.SUCCESS);
    	responseComplete.setHeader(response2.getHeader());
    	responseComplete.setResponseMessage(response2.getResponseMessage());
    	return responseComplete;		
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
    
    private static void  translatePINZPKToLMK(String pinBlock, String pan,String kwp,String Longer){
        StringBuilder requestHSM = new StringBuilder();
        requestHSM.append(pinBlock);
        requestHSM.append(",");
        requestHSM.append(pan);
        requestHSM.append(",");
        requestHSM.append(kwp);
        requestHSM.append(",");
        requestHSM.append(Longer);
        try {
            String traslateResponse  = OmniCryptoCommand.ProcessRequest(Integer.parseInt(Constant.TRASLATE_PIN_FROM_KWP_TO_MFK), requestHSM.toString());
            System.out.println(traslateResponse);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();	
        }        
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
        String pan,
        String institutionId,
        String typeOffProduct){
        StringBuilder requestHSM = new StringBuilder();
        String offSetResponse;
        try {
            requestHSM.append(pinELMK);
            requestHSM.append(",");
            requestHSM.append(pan);
            requestHSM.append(",");
            requestHSM.append(institutionId);
            requestHSM.append(",");
            requestHSM.append(typeOffProduct);
            //TODO:Comentar a Alvaro que falta una base de datos
             offSetResponse  = OmniCryptoCommand.ProcessRequest(Integer.parseInt(Constant.GENERATE_IBM_PIN_OFF_SET), requestHSM.toString()); 			
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new IBMOfSetResponse(ConstantResponse.FORMAT_ERROR_RESPONSE_CODE,ConstantResponse.FORMAT_ERROR_RESPONSE_MESSAGE,null);	
        } catch (Exception e) {
            e.printStackTrace();
            return new IBMOfSetResponse(ConstantResponse.ERROR_RESPONSE_CODE,ConstantResponse.ERROR_RESPONSE_MESSAGE,null);
        }
        return new IBMOfSetResponse(ConstantResponse.SUCESSFULL_RESPONSE_CODE,ConstantResponse.SUCESSFULL_RESPONSE_MESSAGE,"0570");
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
    
}
