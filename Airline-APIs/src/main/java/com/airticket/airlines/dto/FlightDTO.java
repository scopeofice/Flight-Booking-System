package com.airticket.airlines.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
    private String source;
    private String destination;
    private LocalDate travelDate;
    private String pickupTime;
    private String arrivalTime;
    private String flightName;
    private int seat;
}
