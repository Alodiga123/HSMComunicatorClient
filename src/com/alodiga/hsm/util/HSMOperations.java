/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.hsm.util;
import java.security.SecureRandom;

import com.alodiga.hsm.CryptoConnection;
import com.alodiga.hsm.OmniCryptoCommand;
import com.alodiga.hsm.ThalesCryptoCommand;
import com.alodiga.hsm.UnpackThalesCryptoCommand;
import com.alodiga.hsm.data.object.DataEMVField;
import com.alodiga.hsm.exception.ErrorEMVdataException;
import com.alodiga.hsm.exception.NotConnectionHSMException;
import com.alodiga.hsm.response.ARQCEmvDataResponse;
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
    
    private static final byte[] initial_transpose = new byte[]{58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};
    private static final byte[] final_transpose = new byte[]{40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};
    private static final byte[] swap = new byte[]{33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32};
    private static final byte[] key_transpose_1 = new byte[]{57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4};
    private static final byte[] key_transpose_2 = new byte[]{14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32};
    private static final byte[] e_transpose = new byte[]{32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1};
    private static final byte[] p_transpose = new byte[]{16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25};
    private static final byte[][] s = new byte[][]{{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8, 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0, 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}, {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5, 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15, 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}, {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1, 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7, 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}, {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9, 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4, 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}, {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6, 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14, 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}, {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8, 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6, 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}, {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6, 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2, 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}, {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2, 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8, 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}};
    private static final byte[] rots = new byte[]{1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    
    public static String getPinblock(String kwpclear, String pin, String pan) {
        Block block = new Block(kwpclear);
        String s6 = "04" + pin + Convert.resize("", 8, 'F', false) + "FF";
        String s7 = null;
        s7 = "0000" + pan.substring(pan.length() - 13, pan.length() - 1);
        String s8 = "";
        System.out.println("valor de S7:" + s7);
        byte abyte0[] = Convert.fromHexDataToBinData(s6.getBytes());
        byte abyte1[] = Convert.fromHexDataToBinData(s7.getBytes());
        byte abyte2[] = new byte[8];
        for (int i = 0; i < 8; i++)
        abyte2[i] = (byte) (abyte0[i] ^ abyte1[i]);
        s8 = new String(Convert.fromBinDataToHexData(abyte2));
        System.out.println("Valor de S8=" + s8);
        Block block1 = new Block(s8);
        Block block2 = Utils.desEncrypt(block1, block);
        String s9 = block2.toString();
        return s9;
    }
    
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
    
    public static ARQCEmvDataResponse ARQCVerificationAndgenerationARPC(DataEMVField dataEmv,String key, String pan, String schemeID) throws Exception, ErrorEMVdataException {
    	String requestMessage = "";
    	String responseMessage = "";
    	try {
    		String hsmMessage = ThalesCryptoCommand.validateARPCAndGenerationARPC( dataEmv, key,  pan,  schemeID);
    		//////////////////////////////////////////////////////////////////////////////
    		// Cable contra la caja genera numero Ramdon y setea en el OBJECTO   /////////
        	//////////////////////////////////////////////////////////////////////////////
    		//requestMessage = CryptoConnection.sendAndReceiveToHSM(hsmMessage);
        	//responseMessage = UnpackThalesCryptoCommand.unpackGenerateKey(requestMessage);
    		dataEmv.setApplicationCryptogram(getRamdomARPCValue());
    		/*
    		 * 00 : No error 01 : ARQC/TC/AAC verification failed 04 : Mode Flag not 0, 1 or
    		 * 2 05 : Unrecognised Scheme ID 10 : MK parity error 12 : No keys in user
    		 * storage 13 : LMK parity error 15 : Error in input data 21 : Invalid user
    		 * storage index 80 : Data length error 81: Invalid certificate header
    		 */
		} catch (ErrorEMVdataException e) {
			throw new ErrorEMVdataException(e.getMessage(),e);
		}
	
    	return new ARQCEmvDataResponse(Constant.SUCCESS,Constant.SUCCESS, dataEmv);		  
    }
    
	public static String getRamdomValue() throws Exception {
		SecureRandom random = new SecureRandom();
		int num = random.nextInt(0x1000000);
		String formatted = String.format("%06x", num);
		return formatted;
	}
	
	public static String getRamdomARPCValue() throws Exception {	
		return ((getRamdomValue() + getRamdomValue() + getRamdomValue()).substring(0,16));
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
    
    public static String translatePINZPKToLMK(String pinBlock, String pan,String kwp,String Longer){
        StringBuilder requestHSM = new StringBuilder();
        String traslateResponse = "";
        requestHSM.append(pinBlock.trim());
        requestHSM.append(",");
        requestHSM.append(pan.trim());
        requestHSM.append(",");
        requestHSM.append(kwp.trim());
        requestHSM.append(",");
        requestHSM.append(Longer.trim());
        try {
                 traslateResponse  = OmniCryptoCommand.ProcessRequest(Integer.parseInt(Constant.TRASLATE_PIN_FROM_KWP_TO_MFK), requestHSM.toString().trim());
        } catch (NumberFormatException e) {
                e.printStackTrace();
        } catch (Exception e) {
                e.printStackTrace();	
        }
        return traslateResponse.substring(4,9);
        
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

    public static  IBMOfSetResponse generateIBMPinOffSet(
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
    
    private static Block f(int var0, Block var1, Block var2, boolean var3) {
      Block var4 = new Block();
      Block var5 = new Block(var2, e_transpose);
      if (var3) {
         for(int var6 = 0; var6 < rots[var0]; ++var6) {
            var1.rotateLeft();
         }
      }

      Block var10 = new Block(var1, key_transpose_2);
      Block var7 = new Block();

      int var8;
      for(var8 = 0; var8 < 48; ++var8) {
         var7.putBit(var8, var5.getBit(var8) ^ var10.getBit(var8));
      }

      for(var8 = 0; var8 < 8; ++var8) {
         int var9 = 32 * (var7.getBit(6 * var8) ? 1 : 0) + 16 * (var7.getBit(6 * var8 + 5) ? 1 : 0) + 8 * (var7.getBit(6 * var8 + 1) ? 1 : 0) + 4 * (var7.getBit(6 * var8 + 2) ? 1 : 0) + 2 * (var7.getBit(6 * var8 + 3) ? 1 : 0) + (var7.getBit(6 * var8 + 4) ? 1 : 0);
         var4.putBit(4 * var8, (s[var8][var9] & 8) == 8);
         var4.putBit(4 * var8 + 1, (s[var8][var9] & 4) == 4);
         var4.putBit(4 * var8 + 2, (s[var8][var9] & 2) == 2);
         var4.putBit(4 * var8 + 3, (s[var8][var9] & 1) == 1);
      }

      var4.transpose(p_transpose);
      if (!var3) {
         for(var8 = 0; var8 < rots[var0]; ++var8) {
            var1.rotateRight();
         }
      }

      return var4;
    }

    public static Block desEncrypt(Block var0, Block var1) {
      Block var2 = new Block(var0, initial_transpose);
      Block var3 = new Block(var1, key_transpose_1);

      for(int var4 = 0; var4 < 16; ++var4) {
         Block var5 = (Block)var2.clone();

         for(int var6 = 0; var6 < 32; ++var6) {
            var2.putBit(var6, var5.getBit(var6 + 32));
         }

         Block var8 = f(var4, var3, var2, true);

         for(int var7 = 0; var7 < 32; ++var7) {
            var2.putBit(var7 + 32, var5.getBit(var7) ^ var8.getBit(var7));
         }
      }

      var2.transpose(swap);
      var2.transpose(final_transpose);
      return var2;
    }

    public static Block desDecrypt(Block var0, Block var1) {
      Block var2 = new Block(var0, initial_transpose);
      Block var3 = new Block(var1, key_transpose_1);
      var2.transpose(swap);

      for(int var4 = 15; var4 >= 0; --var4) {
         Block var5 = (Block)var2.clone();

         for(int var6 = 0; var6 < 32; ++var6) {
            var2.putBit(var6 + 32, var5.getBit(var6));
         }

         Block var8 = f(var4, var3, var2, false);

         for(int var7 = 0; var7 < 32; ++var7) {
            var2.putBit(var7, var5.getBit(var7 + 32) ^ var8.getBit(var7));
         }
      }

      var2.transpose(final_transpose);
      return var2;
    }

    public static Block tripledesEncrypt(Block var0, Block var1, Block var2, Block var3) {
      return desEncrypt(desDecrypt(desEncrypt(var0, var1), var2), var3);
    }

    public static Block tripledesDecrypt(Block var0, Block var1, Block var2, Block var3) {
      return desDecrypt(desEncrypt(desDecrypt(var0, var3), var2), var1);
    }

    public static Block tripledesEncrypt(Block var0, Block[] var1) {
      switch(var1.length) {
      case 2:
         return tripledesEncrypt(var0, var1[0], var1[1], var1[0]);
      case 3:
         return tripledesEncrypt(var0, var1[0], var1[1], var1[2]);
      default:
         return null;
      }
    }

    public static Block tripledesDecrypt(Block var0, Block[] var1) {
      switch(var1.length) {
      case 2:
         return tripledesDecrypt(var0, var1[0], var1[1], var1[0]);
      case 3:
         return tripledesDecrypt(var0, var1[0], var1[1], var1[2]);
      default:
         return null;
      }
    }

    public static String decimalise(String var0, String var1) {
      String var2 = "";

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         char var4 = var0.charAt(var3);
         if (var4 >= '0' && var4 <= '9') {
            var2 = var2 + var4;
         } else {
            int var5 = var4 - 65 + 10;
            var2 = var2 + var1.charAt(var5);
         }
      }

      return var2;
    }

    public static String addNoCarry(String var0, String var1) {
      String var2 = "";

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         var2 = var2 + (var0.charAt(var3) - 48 + var1.charAt(var3) - 48) % 10;
      }

      return var2;
    }

    public static String scanDigits(int var0, String var1) {
      String var2 = "";

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         char var4 = var1.charAt(var3);
         if (var4 >= '0' && var4 <= '9') {
            var2 = var2 + var4;
         }

         if (var2.length() == var0) {
            return var2;
         }
      }

      return var2;
    }

    public static String scanAlpha(int var0, String var1) {
      String var2 = "";

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         char var4 = var1.charAt(var3);
         if (var4 >= 'A' && var4 <= 'F') {
            var2 = var2 + var4;
         }

         if (var2.length() == var0) {
            return var2;
         }
      }

      return var2;
    }
    
}
