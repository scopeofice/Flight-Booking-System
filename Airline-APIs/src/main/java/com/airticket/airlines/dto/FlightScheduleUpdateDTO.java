package com.airticket.airlines.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightScheduleUpdateDTO {
    private String source;
    private String destination;
    private LocalDate travelDate;
    private LocalTime takeoffTime;
    private LocalTime arrivalTime;
}
