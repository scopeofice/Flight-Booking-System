package com.airticket.airlines.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightCreateUpdateDTO {
    private String flightName;
    private int numberOfSeats;
}
