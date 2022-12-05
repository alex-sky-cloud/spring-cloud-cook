package com.udp.client.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ConvertUtils {

    public static String convertByteArrToString(byte[] array) {

        Charset charset = StandardCharsets.UTF_8;

        return new String(array, charset);
    }


    public static byte[] convertStringToByteArr(String message) {

        Charset charset = StandardCharsets.UTF_8;

        return message.getBytes(charset);
    }

    public static String addStringWhiteSpaces(String inputString, int maxLength){

        int max = maxLength - inputString.length();
        return inputString + " ".repeat(Math.max(0, max));
    }
}
