package com.airticket.itc.controller;

import com.airticket.itc.dto.BookingDTO;
import com.airticket.itc.dto.BookingResponseDTO;
import com.airticket.itc.dto.FlightBookingAcknowledgement;
import com.airticket.itc.service.FlightBookingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {
    @Autowired
    private FlightBookingServiceImpl flightBookingService;

    @PostMapping("/bookFlightTicket")
    public FlightBookingAcknowledgement bookFlightTicket(@RequestBody BookingDTO request) {
        return flightBookingService.bookFlightTicket(request);
    }
    @GetMapping("/bookingDetailsByPNR/{pnr}")
    public FlightBookingAcknowledgement bookingDetailsByPNR(@PathVariable String pnr) {
        return flightBookingService.bookingDetailsByPNR(pnr);
    }

    @GetMapping("/bookingDetailsById/{bookingId}")
    public BookingResponseDTO bookingDetailsById(@PathVariable Long bookingId) {
        return flightBookingService.bookingDetailsById(bookingId);
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<BookingResponseDTO>> getUserBookings(@PathVariable String userEmail) {
        List<BookingResponseDTO> userBookings = flightBookingService.userBookings(userEmail);
        if (!userBookings.isEmpty()) {
            return new ResponseEntity<>(userBookings, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
