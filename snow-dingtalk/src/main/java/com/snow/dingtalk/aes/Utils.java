package com.snow.dingtalk.aes;

import java.util.Random;

/**
 * @author qimingjin
 * @date 2022-08-04 14:12
 * @Description:
 */
public class Utils {

    public Utils() {
    }

    public static String getRandomStr(int count) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < count; ++i) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }

        return sb.toString();
    }

    public static byte[] int2Bytes(int count) {
        byte[] byteArr = new byte[]{(byte)(count >> 24 & 255), (byte)(count >> 16 & 255), (byte)(count >> 8 & 255), (byte)(count & 255)};
        return byteArr;
    }

    public static int bytes2int(byte[] byteArr) {
        int count = 0;

        for(int i = 0; i < 4; ++i) {
            count <<= 8;
            count |= byteArr[i] & 255;
        }

        return count;
    }
}
