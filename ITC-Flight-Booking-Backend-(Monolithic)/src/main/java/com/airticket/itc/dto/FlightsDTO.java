package com.airticket.itc.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightsDTO {
    @NonNull
    private String flightName;
    @NonNull
    private String airlineName;
    @NonNull
    private int numberOfSeats;
    @NonNull
    private double fare;

}
