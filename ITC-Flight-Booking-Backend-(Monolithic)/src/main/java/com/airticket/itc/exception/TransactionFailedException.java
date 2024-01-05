package com.airticket.itc.exception;

public class TransactionFailedException extends RuntimeException {

	public TransactionFailedException(String message){
		super(message);
	}

}
