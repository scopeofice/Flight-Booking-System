package com.airticket.itc.service;

import com.airticket.itc.dto.*;

import java.util.List;

public interface FlightBookingService {
    FlightBookingAcknowledgement bookFlightTicket(BookingDTO flightBookingRequest);
    PNRResponseDTO bookingDetailsByPNR(String pnr);
    List<BookingResponseDTO> userBookings(String userEmail);
    BookingResponseDTO bookingDetailsById(String bookingId);
    String updateBookings(String bookingID, BookingDTO booking);
    String cancelBooking(BookingCancelDTO cancelRequest);
}
