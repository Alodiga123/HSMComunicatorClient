package com.alodiga.hsm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public abstract class TlvUtils {
	 /** 
     * Convert a hexadecimal string to a list of TLV objects 
     * @param hexString 
     * @return 
     */  
    public static List<Tlv> builderTlvList(String hexString) {  
        List<Tlv> tlvs = new ArrayList<Tlv>();  
  
        int position = 0;  
        while (position != StringUtils.length(hexString)) {  
            String _hexTag = getTag(hexString, position);  
            position += _hexTag.length();  
  
            LPositon l_position = getLengthAndPosition(hexString, position);  
            int _vl = l_position.get_vL();  
  
            position = l_position.get_position();  
  
            String _value = StringUtils.substring(hexString, position, position + _vl * 2);  
  
            position = position + _value.length();  
  
            tlvs.add(new Tlv(_hexTag, _vl, _value));  
        }  
        return tlvs;  
    }  
      
    /** 
     * Convert hexadecimal string to TLV object MAP 
     * @param hexString 
     * @return 
     */  
    public static Map<String, Tlv> builderTlvMap(String hexString) {  
  
        Map<String, Tlv> tlvs = new HashMap<String, Tlv>();  
  
        int position = 0;  
        while (position != hexString.length()) {  
            String _hexTag = getTag(hexString, position);  
              
            position += _hexTag.length();  
              
            LPositon l_position = getLengthAndPosition(hexString, position);  
              
            int _vl = l_position.get_vL();  
            position = l_position.get_position();  
            String _value = hexString.substring(position, position + _vl * 2);  
            position = position + _value.length();  
              
            tlvs.put(_hexTag, new Tlv(_hexTag, _vl, _value));  
        }  
        return tlvs;  
    }  
      
    /** 
     * Return the length of the last Value 
     *  
     * @param hexString 
     * @param position 
     * @return  
     */  
    private static LPositon getLengthAndPosition(String hexString, int position) {  
        String firstByteString = hexString.substring(position, position + 2);  
        int i = Integer.parseInt(firstByteString, 16);  
        String hexLength = "";  
  
        if (((i >>> 7) & 1) == 0) {  
            hexLength = hexString.substring(position, position + 2);  
            position = position + 2;  
        } else {  
            //When the leftmost bit is 1, get the last 7 bits.  
            int _L_Len = i & 127;  
            position = position + 2;  
            hexLength = hexString.substring(position, position + _L_Len * 2);  
            //position represents the first byte, followed by how many bytes to represent the subsequent Value  
            position = position + _L_Len * 2;  
        }  
        return new LPositon(Integer.parseInt(hexLength, 16), position);  
  
    }  
  
    /** 
     * Get subdomain Tag Tags 
     *  
     * @param hexString 
     * @param position 
     * @return 
     */  
    private static String getTag(String hexString, int position) {  
        String firstByte = StringUtils.substring(hexString, position, position + 2);  
        int i = Integer.parseInt(firstByte, 16);  
        if ((i & 0x1f) == 0x1f) {  
            return hexString.substring(position, position + 4);  
  
        } else {  
            return hexString.substring(position, position + 2);  
        }  
    }  
}
