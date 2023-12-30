package com.airticket.itc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PassengerDTO {
    private String firstName;
    private String lastName;
    private int age;
    private int seatNumber;
    private String gender;
}
