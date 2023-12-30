package com.airticket.itc.exception;

public class FlightScheduleNotFoundException extends Exception{
    public FlightScheduleNotFoundException(String message){
        super(message);
    }
}
