package com.airticket.airlines.controller;

import com.airticket.airlines.dto.FlightDTO;
import com.airticket.airlines.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/byFlightName/{flightName}")
    public List<FlightDTO> getFlightDetailsByFlightName(@PathVariable String flightName) {
        return flightService.getFlightDetailsByFlightName(flightName);
    }

    @GetMapping("/bySourceDestinationAndTravelDate")
    public List<FlightDTO> getFlightDetailsBySourceDestinationAndTravelDate(
            @RequestParam String source, @RequestParam String destination, @RequestParam String travelDate) {
        return flightService.getFlightDetailsBySourceDestinationAndTravelDate(source, destination, travelDate);
    }

    @GetMapping("/all")
    public List<FlightDTO> getAllFlightDetails() {
        return flightService.getAllFlightDetails();
    }
}
