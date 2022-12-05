package com.udp.server.utils;

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
}
