package com.airticket.transactions.controller;

import com.airticket.transactions.dto.BookingDTO;
import com.airticket.transactions.dto.FlightBookingAcknowledgement;
import com.airticket.transactions.service.FlightBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.airticket.transactions.dto.FlightBookingRequest;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {
    @Autowired
    private FlightBookingService flightBookingService;

    @PostMapping("/bookFlightTicket")
    public FlightBookingAcknowledgement bookFlightTicket(@RequestBody FlightBookingRequest request) {
        return flightBookingService.bookFlightTicket(request);
    }
    @GetMapping("/bookingDetailsByPNR/{pnr}")
    public FlightBookingAcknowledgement bookingDetailsByPNR(@PathVariable String pnr) {
        return flightBookingService.bookingDetailsByPNR(pnr);
    }

    @GetMapping("/bookingDetailsById/{bookingId}")
    public BookingDTO bookingDetailsById(@PathVariable Long bookingId) {
        return flightBookingService.bookingDetailsById(bookingId);
    }
}
