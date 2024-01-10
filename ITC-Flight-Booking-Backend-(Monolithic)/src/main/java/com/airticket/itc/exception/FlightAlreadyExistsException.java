package com.airticket.itc.exception;

public class FlightAlreadyExistsException extends Exception{
    public FlightAlreadyExistsException(String message){
        super(message);
    }
}
