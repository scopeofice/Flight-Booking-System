package com.airticket.transactions.exception;

import java.lang.reflect.Constructor;

public class InsuffientAmountException extends RuntimeException {
	
	public InsuffientAmountException(String message){
		super(message);
	}

}
