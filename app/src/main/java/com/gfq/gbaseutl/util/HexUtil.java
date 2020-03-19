package com.gfq.gbaseutl.util;

import java.math.BigInteger;
import java.util.Arrays;

public class HexUtil {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String getHex(byte b) {
        int v = b & 0xFF;
        return getHex(v);
    }

    public static String getHex(int v) {
        v = v & 0xFF;
        return new String(new char[]{HEX_ARRAY[v >>> 4], HEX_ARRAY[v & 0x0F]});
    }

    public static String getHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }



    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     *
     * @param hexStr
     * @return
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    public static int hex2_10(String hex){
        BigInteger bigint=new BigInteger(hex, 16);
        return bigint.intValue();

    }

    public static short intToHex(int n) {
//        StringBuilder sb = new StringBuilder(8);
        short a = 0;
        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        if (n == 0) {
            return '0';
        }
        while (n != 0) {
//            sb = sb.append(b[n % 16]);
            n = n / 16;
        }
//        a = sb.reverse().toString();
        return a;
    }

   /* public static byte int2Byte_16(int number) {
        int i = 0;
        char[] S = new char[100];
        if (number == 0) {
            return 0;
        } else {
            while (number != 0) {
                int t = number % 16;
                if (t >= 0 && t < 10) {
                    S[i] = (char) (t + '0');
                    i++;
                } else {
                    S[i] = (char) (t + 'A' - 10);
                    i++;
                }
                number = number / 16;
            }

            StringBuilder sb =new StringBuilder();
            for (int j = i - 1; j >= 0; j--) {
                System.out.print(S[j]);
                sb.append(S[j]);
            }
            String s = sb.toString();
            if(S.length==1){
                String s1 = Arrays.toString(S);
            }
            int in = Integer.parseInt(s);
            return (byte) in;
        }
    }*/

    /** 卡号位数：8 */
    public static byte CARD_NUM_BIT = 2;

    /**
     * isBlank
     *
     * @param value
     * @return true: blank; false: not blank
     */
    private static boolean isBlank(String value) {
        if (value == null || "".equals(value.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 10进制转16进制，并补齐卡号位数
     *
     * @param str
     * @return
     */
    public static String toHexStr(String str) {
        String result = "";
        String regex = "^\\d{1,}$";
        if (!isBlank(str)) {
            str = str.trim();
            if (str.matches(regex)) {
                String hexStr = Long.toHexString(Long.parseLong(str.trim())).toUpperCase();
                if (hexStr.length() < CARD_NUM_BIT) {
                    hexStr = org.apache.commons.lang3.StringUtils.leftPad(hexStr, CARD_NUM_BIT, '0');
                }
                result = hexStr;
            } else if (isHex(str)) {
                if (str.length() < CARD_NUM_BIT) {
                    str = org.apache.commons.lang3.StringUtils.leftPad(str, CARD_NUM_BIT, '0');
                }
                result = str;
            }
        }
        return result;
    }

    public static boolean isHex(String strHex) {
        int i = 0;
        if (strHex.length() > 2) {
            if (strHex.charAt(0) == '0' && (strHex.charAt(1) == 'X' || strHex.charAt(1) == 'x')) {
                i = 2;
            }
        }
        for (; i < strHex.length(); ++i) {
            char ch = strHex.charAt(i);
            if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f'))
                continue;
            return false;
        }
        return true;
    }

    public static byte[] str2bytes(String[] str) {
        String[] temp = new String[str.length];
        for (int i = 0; i < str.length ; i++) {
            String s = HexUtil.toHexStr(str[i]);
            temp[i] = s;
        }
        System.out.println(Arrays.toString(temp));
        byte[] bytes = new byte[temp.length];
        for (int i = 0; i <temp.length ; i++) {
            short b=Short.parseShort(temp[i],16);
            bytes[i] = (byte) b;
        }
//        System.out.println(Arrays.toString(bytes));
        return bytes;
    }
}
