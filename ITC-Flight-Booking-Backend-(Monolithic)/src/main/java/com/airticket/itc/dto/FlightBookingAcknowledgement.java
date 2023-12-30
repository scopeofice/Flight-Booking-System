package com.airticket.itc.dto;

import com.airticket.itc.entity.PassengerInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightBookingAcknowledgement {

	private String status;
	private double totalFare;
	private String pnrNo;
	private List<PassengerDTO> passengerInfo;
}
