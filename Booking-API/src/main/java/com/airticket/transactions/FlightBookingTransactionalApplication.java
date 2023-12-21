package com.airticket.transactions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class FlightBookingTransactionalApplication {


	
	public static void main(String[] args) {
		SpringApplication.run(FlightBookingTransactionalApplication.class, args);
		
	}

}




/*
 * { "passengerInfo":{ "name":"Shubham", "email":"shubhamg@gmail.com",
 * "source":"pune", "destination":"delhi", "travelDate":"12-12-2023",
 * "pickupTime":"4-pm", "arrivalTime":"6-pm", "fare":12000 },"paymentInfo":{
 * "accountNo":"acc1", "cardType":"Debit" } }
 */