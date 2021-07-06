/*     */ package com.alodiga.hsm;

import java.util.HashMap;
import java.util.Map;

import com.alodiga.hsm.data.object.DataEMVField;
import com.alodiga.hsm.exception.EMVEmptydataException;
import com.alodiga.hsm.exception.ErrorEMVdataException;
import com.alodiga.hsm.util.ConstantTlv;
import com.alodiga.hsm.util.Tlv;
import com.alodiga.hsm.util.TlvUtils;

public class ThalesCryptoCommand {
   private static final String[] Commands = new String[50];
   private static final String MsgHeader = "00003000";
   private static final String[] KeyTypes;
   private static final String[] KeySchemes;
   private static final String[] PinBlockFormat;

   static {
      Commands[0] = "B2";
      Commands[1] = "CC";
      Commands[2] = "JE";
      Commands[3] = "CY";
      Commands[4] = "A0";
      Commands[5] = "A8";
      Commands[6] = "A6";
      Commands[7] = "EA";
      Commands[8] = "EC";
      Commands[9] = "KQ";
      Commands[10] = "DE";
      Commands[11] = "DG";
      Commands[12] = "IA";
      Commands[13] = "BU";
      Commands[14] = "GC";
      Commands[15] = "NO";
      Commands[16] = "NC";
      Commands[17] = "CW";
      
      KeyTypes = new String[50];
      KeyTypes[0] = "000";
      KeyTypes[1] = "001";
      KeyTypes[2] = "002";
      KeyTypes[3] = "402";
      KeyTypes[4] = "109";
      KeySchemes = new String[50];
      KeySchemes[0] = "Z";
      KeySchemes[1] = "U";
      KeySchemes[2] = "T";
      KeySchemes[3] = "X";
      KeySchemes[4] = "Y";
      PinBlockFormat = new String[50];
      PinBlockFormat[0] = "1";
      PinBlockFormat[1] = "2";
      PinBlockFormat[2] = "5";
      PinBlockFormat[3] = "8";
      PinBlockFormat[4] = "11";
      PinBlockFormat[4] = "13";
   }

   public static String firmwareCommand() {
      String message = "00003000" + Commands[16];
      System.out.println(message);
      return message;
   }

   public static String statusCommand() {
      String message = "00003000" + Commands[15] + "00";
      System.out.println(message);
      return message;
   }

   public static String echoTestMessage() {
      String message = "00003000" + Commands[0] + "001B" + "111010110111100110100010101";
      System.out.println(message);
      return message;
   }

   public static String generateCheckDigit(String Cryptogram, String CryptoLength, String Keytype) {
      String LengtKey = null;
      String keytypecode = null;
      LengtKey = obtainlenghtkey(CryptoLength.trim());
      String lenghtcommand = null;
      if (LengtKey.equals("U")) {
         lenghtcommand = "1";
      }

      if (LengtKey.equals("Z")) {
         lenghtcommand = "0";
      }

      if (LengtKey.equals("T")) {
         lenghtcommand = "2";
      }

      if (Keytype.equals("KEK")) {
         keytypecode = KeyTypes[0];
      }

      if (Keytype.equals("TPK")) {
         keytypecode = KeyTypes[2];
      }

      if (Keytype.equals("KWP")) {
         keytypecode = KeyTypes[1];
      }

      if (Keytype.equals("PVK")) {
         keytypecode = KeyTypes[2];
      }

      if (Keytype.equals("CVK")) {
         keytypecode = KeyTypes[3];
      }

      if (Keytype.equals("CAK")) {
         keytypecode = KeyTypes[4];
      }

      String message = "00003000" + Commands[13] + "FF" + lenghtcommand + LengtKey + Cryptogram + ";" + keytypecode + ";" + "0" + "0" + "1";
      System.out.println(message);
      return message;
   }

   public static String generateKey(String CryptoLength, String Keytype) {
      String keytypecode = null;
      String lenghtcommand = null;
      String LengtKey = obtainlenghtkey(CryptoLength);
      if (LengtKey.equals("U")) {
         lenghtcommand = "1";
      }
      if (LengtKey.equals("Z")) {
         lenghtcommand = "0";
      }
      if (LengtKey.equals("T")) {
         lenghtcommand = "2";
      }
      if (Keytype.equals("KEK")) {
         keytypecode = KeyTypes[0];
      }

      if (Keytype.equals("TPK")) {
         keytypecode = KeyTypes[2];
      }

      if (Keytype.equals("KWP")) {
         keytypecode = KeyTypes[1];
      }

      if (Keytype.equals("PVK")) {
         keytypecode = KeyTypes[2];
      }

      if (Keytype.equals("CVK")) {
         keytypecode = KeyTypes[3];
      }

      if (Keytype.equals("CAK")) {
         keytypecode = KeyTypes[4];
      }
      String message = "00003000" + Commands[4] + "0" + keytypecode + LengtKey;
      System.out.println("Comando generate key..............: " + message);
      return message;
   }
   
   public static String generateKVC() {
	      String message = "00003000"+ Commands[4]+"0"+KeyTypes[3] + "Z";
	      return message;
    }
  
   public static String generateCVV(String cvk,String pan,String endingDate,String serviceCode) { 
	   String message = "00003000"+Commands[17]+cvk+pan+";"+endingDate+serviceCode;
	   //String message = "00003000"+Commands[17]+cvk+"54123456789012345"+";"+"8701"+"999";
	   return message;
   }
   
   

   /**
    *@author kerwin Gomez
    *@param key: Key generado previamente
    *@param pan:Numero de Tarjeta de 16 a 19 digitos
    *@param field55:  Campo 55 on el campo deonde venga la data del chip.
    *@param schemeID: Campo que sirve para indicar el método de validación y generación de ARPC
    *   0 = Visa VSDC or UKIS
		1 = Europay or MasterCard M/Chip 
    * 
    * 
    *@return 
    *    
    */
   public static String validateARPCAndGenerationARPC(DataEMVField dataEmv,String key, String pan, String schemeID) throws ErrorEMVdataException { 
		try {
			validateEmtyObject(dataEmv);	
		} catch (EMVEmptydataException e) {
			throw new ErrorEMVdataException(e.getMessage(), e);
		}
		String message = ConstantTlv.HSM_HEADER + Commands[9] + ConstantTlv.MODE + schemeID + key + formatedPan(pan, dataEmv.getPanSerialNumber())
				+ dataEmv.getAplicationTransactionCounter() + dataEmv.getUnpredictableNumber() + Integer.toHexString(dataEmv.getTransactionDataLenght()) + dataEmv.getTransactionData() + ConstantTlv.HSM_DELIMITER_COMAND
				+ dataEmv.getCryptogramAplicationData() + ConstantTlv.ARC_TEMPORAL_CONSTANT;
		System.out.println("message:" + message);
		return message;
	}

	// psn pan secuentNumber
	public static String formatedPan(String pan, String psn) {
		return pan.substring(pan.length() - 14, pan.length()) + psn;
	}

	public static void validateEmtyObject(DataEMVField dataEMV) throws EMVEmptydataException {
		try {
			dataEMV.getPanSerialNumber();
		} catch (NullPointerException e) {
			throw new EMVEmptydataException(ConstantTlv.fieldEmptyMessage + ConstantTlv.PSN , e);
		}
		
		try {
			dataEMV.getAplicationTransactionCounter();
		} catch (NullPointerException e) {
			throw new EMVEmptydataException(ConstantTlv.fieldEmptyMessage + ConstantTlv.APLICATION_TRANSACTION_COUNTER , e);
		}
		
		try {
			dataEMV.getUnpredictableNumber();
		} catch (NullPointerException e) {
			throw new EMVEmptydataException(ConstantTlv.fieldEmptyMessage + ConstantTlv.UNPREDICTABLE_NUMBER , e);
		}
		
		try {
			dataEMV.getApplicationCryptogram();
		} catch (NullPointerException e) {
			throw new EMVEmptydataException(ConstantTlv.fieldEmptyMessage + ConstantTlv.APPLICATION_CRYPTOGRAM , e);
		}

		try {
			dataEMV.getAmount();
		} catch (NullPointerException e) {
			throw new EMVEmptydataException(ConstantTlv.fieldEmptyMessage + ConstantTlv.AMOUNT_AUTORISED , e);
		}
		
		try {
			dataEMV.getOtherAmount();
		} catch (NullPointerException e) {
			throw new EMVEmptydataException(ConstantTlv.fieldEmptyMessage + ConstantTlv.OTHER_AMOUNT , e);
		}
		
		try {
			dataEMV.getTerminalVerificationResult();
		} catch (NullPointerException e) {
			throw new EMVEmptydataException(ConstantTlv.fieldEmptyMessage + ConstantTlv.TERMINAL_VERIFICATION_RESULT , e);
		}
		
		try {
			dataEMV.getTerminalCountryCode();
		} catch (NullPointerException e) {
			throw new EMVEmptydataException(ConstantTlv.fieldEmptyMessage+ConstantTlv.TERMINAL_COUNTRY_CODE , e);
		}
		
		try {
			 dataEMV.getTransactionCurrencyCode();
		} catch (NullPointerException e) {
			throw new EMVEmptydataException(ConstantTlv.fieldEmptyMessage + ConstantTlv.TRANSACTION_CURRENCY_CODE , e);
		}
		
		try {
			dataEMV.getTransactionDate();
		} catch (NullPointerException e) {
			throw new EMVEmptydataException(ConstantTlv.fieldEmptyMessage + ConstantTlv.TRANSACTION_DATE , e);
		}
		
		try {
			dataEMV.getTransactionType();
		} catch (NullPointerException e) {
			throw new EMVEmptydataException(ConstantTlv.fieldEmptyMessage + ConstantTlv.TRANSACTION_TYPE , e);
		}
		
		try {
			dataEMV.getAplicationInterchangeProfile();
		} catch (NullPointerException e) {
			throw new EMVEmptydataException(ConstantTlv.fieldEmptyMessage + ConstantTlv.APLICATION_INTERCHANGE_PROFILE , e);
		}
		
		try {
			dataEMV.getIsuuerAplicationDate();
		} catch (NullPointerException e) {
			throw new EMVEmptydataException(ConstantTlv.fieldEmptyMessage + ConstantTlv.ISSUER_APLICATION_DATA , e);
		}
		
	}

   public static String transletePintoMFKZPKtoLMK(String lenghtCryptoKWP,String cryptoKWP,String pinBlock,String lenght,String accountNumber) { 
	   String Lengtkwp = null;
	      //Lengtkwp = obtainlenghtkey(lenghtCryptoKWP);
	      String message = "00003000" + Commands[2] +  cryptoKWP + pinBlock + "01" + accountNumber;
	      return message;
   }

   public static String translatePintoKWP(String lenghtCryptoKWPOrg, String cryptoKWPOrg, String lenghtCryptoKWPDest, String cryptoKWPDest, String pinLenght, String orgPinBlockFormat, String DestPinBlockFormat, String pinBlock, String accountNumber) {
      String Lengtkwporg = null;
      Lengtkwporg = obtainlenghtkey(lenghtCryptoKWPOrg);
      String Lengtkwpdest = obtainlenghtkey(lenghtCryptoKWPDest);
      String message = "00003000" + Commands[1] + Lengtkwporg + cryptoKWPOrg + Lengtkwpdest + cryptoKWPDest + pinLenght + pinBlock + orgPinBlockFormat + DestPinBlockFormat + accountNumber;
      return message;
   }

   public static String verifyVISACvv(String cryptoCVK, String valueCVV, String expdate, String serviceCode, String accountNumber) {
      String message = "00003000" + Commands[3] + cryptoCVK + valueCVV + accountNumber + expdate + serviceCode;
      return message;
   }

   public static String verifyIBMPin(String lenghtCryptoKWP, String cryptoKWP, String cryptoPVK, String maxPinLenght, String minPinLengt, String pinBlockFormat, String pinBlock, String accountNumber, String decimalizationTable, String pinValidationData, String pinOffset) {
      String Lengtkwp = null;
      Lengtkwp = obtainlenghtkey(lenghtCryptoKWP);
      String message = "00003000" + Commands[7] + Lengtkwp + cryptoKWP + cryptoPVK + maxPinLenght + pinBlock + pinBlockFormat + minPinLengt + accountNumber + decimalizationTable + pinValidationData + pinOffset;
      return message;
   }

   public static String verifyVISAPin(String lenghtCryptoKWP, String cryptoKWP, String cryptoPVK, String pvki, String pvv, String pinBlockFormat, String pinBlock, String accountNumber) {
      String Lengtkwp = null;
      Lengtkwp = obtainlenghtkey(lenghtCryptoKWP);
      String message = "00003000" + Commands[8] + Lengtkwp + cryptoKWP + cryptoPVK + pinBlock + pinBlockFormat + accountNumber + pvki + pvv;
      return message;
   }

   public static String generateIBMOffset(String crytpoPvkLenght, String cryptoPVK, String pinUnderLMK, String minPinLengt, String decimalizationTable, String pinValidationData, String accountNumber) {
      String lenghtPvk = obtainlenghtkey(crytpoPvkLenght);
      
     //String message = "00003000DEUC695FD6E424E17AB3E23F7A6E4C8EC47032740444116125484201234567890123456048441161N0";
     String message = "00003000" + Commands[10] + cryptoPVK + pinUnderLMK + minPinLengt + accountNumber + decimalizationTable + pinValidationData;
     
      return message;
   }

   public static String generateVISAOffset(String cryptoPVK, String pinUnderLMK, String pvki, String accountNumber) {
      String message = "00003000" + Commands[11] + cryptoPVK + pinUnderLMK + accountNumber + pvki;
      return message;
   }

   public static String pinMngt(String cryptoKEK) {
      String message = "00003000" + Commands[12] + cryptoKEK;
      return message;
   }

   public static String obtainlenghtkey(String originalLenght) {
      String resultLenght = null;
      System.out.println(originalLenght);
      if (originalLenght.equals("Single")) {
         resultLenght = KeySchemes[0];
         
      }

      if (originalLenght.equals("Double")) {
         resultLenght = KeySchemes[1];
         
      }

      if (originalLenght.equals("Triple")) {
         resultLenght = KeySchemes[2];
         
      }

      System.out.println("Resultado Final KeyScheme: " + resultLenght);
      return resultLenght;
   }
   
   
   public static String generateaKeyCheckValue(String kek) {
	      String message = "00003000" + Commands[13] + "FF1U" + kek + ";000" + ";XU1";
	      return message;
	   }
   
   public static String exportKey(String kek,String kwp) {
	      String message = "00003000" + Commands[5] + "001U" + kek + kwp + "Z";
	      return message;
	   }


   public static void main(String[] args) {
      System.out.println(generateaKeyCheckValue("142EE05FC05CEBF2224F597AB3713CC4"));
   }
}