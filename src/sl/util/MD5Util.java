package sl.util;

import java.security.MessageDigest;

/**
 * md5����
 * 
 * @author moyer
 * @date 2018��1��28��
 */
public class MD5Util {
    // ����MD5
    public static String getMD5(String message) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // ����һ��md5�㷨����
            byte[] messageByte = message.getBytes("UTF-8");
            byte[] md5Byte = md.digest(messageByte); // ���MD5�ֽ�����,16*8=128λ
            md5 = bytesToHex(md5Byte); // ת��Ϊ16�����ַ���
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    // ������תʮ������
    public static String bytesToHex(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if (num < 0) {
                num += 256;
            }
            if (num < 16) {
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }
}
