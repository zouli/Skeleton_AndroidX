package com.riverside.skeleton.android.util.resource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String getMD5(String val) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] encryption = md5.digest(val.getBytes());

            return byte2String(encryption);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    private static String byte2String(byte[] digest) {
        StringBuilder str = new StringBuilder();
        String tempStr;
        for (int i = 1; i < digest.length; i++) {
            tempStr = (Integer.toHexString(digest[i] & 0xff));
            if (tempStr.length() == 1) {
                str.append("0").append(tempStr);
            } else {
                str.append(tempStr);
            }
        }
        return str.toString().toLowerCase();
    }
}
