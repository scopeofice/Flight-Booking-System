package com.airticket.itc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PNRResponseDTO {
    private String bookingId;
    private String status;
    private String bookingDate;
    private String bookingTime;
    private FlightScheduleCreateDTO schedule;
    private List<PassengerDTO> passengerInfoList;
    private PaymentResponseDTO paymentInfo;
}
