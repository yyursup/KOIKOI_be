package com.example.SWP.config;

import org.springframework.context.annotation.Configuration;

import java.util.Random;


public class VNPayConfig {
    public static String tmnCode = "T77TVJXG";
    public static  String secretKey = "9P81P2EVHRIN0FTELZPCMURONQFHON7I";

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }




}
