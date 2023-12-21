package com.airticket.itc.util;


import java.util.Random;

public class AccountUtils {

    public static String generateOTP() {
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);
        return String.valueOf(randomNumber);
    }

    public static final String OTP_MATCH_CODE = "001";
    public static final String OTP_MATCH_MESSAGE = "OTP matched!";
    public static final String OTP_NOT_MATCH_CODE = "002";
    public static final String OTP_NOT_MATCH_MESSAGE = "OTP did not matched!";


}
