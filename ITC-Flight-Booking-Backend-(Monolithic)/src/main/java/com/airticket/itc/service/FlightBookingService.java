package com.airticket.itc.service;

import com.airticket.itc.dto.BookingDTO;
import com.airticket.itc.dto.BookingResponseDTO;
import com.airticket.itc.dto.FlightBookingAcknowledgement;

import java.util.List;

public interface FlightBookingService {
    FlightBookingAcknowledgement bookFlightTicket(BookingDTO flightBookingRequest);
    FlightBookingAcknowledgement bookingDetailsByPNR(String pnr);
    List<BookingResponseDTO> userBookings(String userEmail);
    BookingResponseDTO bookingDetailsById(Long bookingId);
}
