package com.airticket.itc.exception;

public class BookingNotFoundException extends RuntimeException {

	public BookingNotFoundException(String message){
		super(message);
	}

}
