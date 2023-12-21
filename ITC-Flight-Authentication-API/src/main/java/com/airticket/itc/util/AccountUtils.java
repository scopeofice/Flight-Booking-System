package com.airticket.itc.util;

import java.time.Year;

public class AccountUtils {
    static Year currentYear = Year.now();
    static int min = 100000;
    static int max = 999999;

    public static String genrateAccountNumber(){
    int randNumber = (int) Math.floor(Math.random() * ( max - min + 1) + min);
    String year = String.valueOf(currentYear);
    String randomNumber = String.valueOf(randNumber);
    StringBuilder accountNumber = new StringBuilder();

    return accountNumber.append(year).append(randomNumber).toString();

    }

    public static final String ACCOUNT_ALREADY_EXISTS_CODE = "001";
    public static final String ACCOUNT_ALREADY_EXISTS_MESSAGE = "Sorry user already exists";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account created successfully";
    public static final String ACCOUNT_NOT_EXISTS = "003";
    public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "Account does not exists";
    public static final String ACCOUNT_FOUND = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "Account Found";
    public static final String AMOUNT_CREDITED_SUCCESS_CODE = "005";
    public static final String AMOUNT_CREDITED_SUCCESS_MESSAGE = "Amount credited successfully";
    public static final String AMOUNT_DEBITED_SUCCESS_CODE = "006";
    public static final String AMOUNT_DEBITED_SUCCESS_MESSAGE = "Amount debited successfully";
    public static final String INSUFFICIENT_BALANCE_CODE = "007";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Amount not debited";
    public static final String TRANSFER_SUCCESS_CODE = "008";
    public static final String TRANSFER_SUCCESS_MESSAGE = "Money transfered successfully";


}
