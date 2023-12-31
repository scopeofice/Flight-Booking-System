package com.airticket.itc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightScheduleUpdateDTO {
    private String source;
    private String scheduleCode;
    private String destination;
    private String travelDate;
    private String pickupTime;
    private String arrivalTime;
    private String flightName;
    private String status;
    private int availableSeats;
}
