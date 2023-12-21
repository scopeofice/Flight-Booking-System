package com.airticket.transactions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long flightId;
    private List<PassengerDTO> passengerInfoList;
    private PaymentDTO paymentInfo;
}
