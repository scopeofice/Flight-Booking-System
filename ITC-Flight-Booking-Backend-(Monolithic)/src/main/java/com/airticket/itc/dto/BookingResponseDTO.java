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
public class BookingResponseDTO {
    private String bookingId;
    private String status;
    private FlightScheduleDTO schedule;
    private String email;
    private List<PassengerDTO> passengerInfoList;
    private PaymentResponseDTO paymentInfo;
}
