package com.airticket.airlines.exception;

public class FlightScheduleNotFoundException extends Exception{
    public FlightScheduleNotFoundException(String message){
        super(message);
    }
}
