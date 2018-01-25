package cn.dajiahui.kid.util;

import java.security.MessageDigest;

/**
 * 对外提供getMD5(String)方法
 *
 * @author dingjianwei
 */
public class MD5 {

    public static String getMD5(String str) {
        /**
         * 加密
         * @param plaintext 明文
         * @return ciphertext 密文
         */

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = str.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str1[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str1[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str1[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str1);
        } catch (Exception e) {
            return null;
        }

    }

    private static String getString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(b[i]);
        }
        return sb.toString();
    }
}
