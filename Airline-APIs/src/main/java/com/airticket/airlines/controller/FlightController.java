package com.airticket.airlines.controller;

import com.airticket.airlines.dto.FlightCreateUpdateDTO;
import com.airticket.airlines.dto.FlightDTO;
import com.airticket.airlines.dto.FlightScheduleCreateUpdateDTO;
import com.airticket.airlines.dto.FlightScheduleUpdateDTO;
import com.airticket.airlines.exception.FlightNotFoundException;
import com.airticket.airlines.exception.FlightScheduleNotFoundException;
import com.airticket.airlines.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/addFlight")
    public ResponseEntity<String> addFlight(@RequestBody FlightCreateUpdateDTO flightDTO) {
        String addedFlight = flightService.addFlight(flightDTO);
        return new ResponseEntity<>(addedFlight, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteFlight/{flightName}")
    public ResponseEntity<String> deleteFlight(@PathVariable String flightName) {
        String resp = flightService.deleteFlight(flightName);
        return new ResponseEntity<>(resp,HttpStatus.NO_CONTENT);
    }

    @PostMapping("/addFlightSchedule")
    public ResponseEntity<String> addFlightSchedule(@RequestBody FlightScheduleCreateUpdateDTO scheduleDTO) throws FlightNotFoundException {
        String  addedSchedule = flightService.addFlightSchedule(scheduleDTO);
        return new ResponseEntity<>(addedSchedule, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteFlightSchedule/{scheduleId}")
    public ResponseEntity<String > deleteFlightSchedule(@PathVariable Long scheduleId) throws FlightScheduleNotFoundException {
        String resp = flightService.deleteFlightSchedule(scheduleId);
        return new ResponseEntity<>(resp,HttpStatus.NO_CONTENT);
    }
    @PutMapping("/updateFlightSchedule/{scheduleId}")
    public ResponseEntity<FlightDTO> updateFlightSchedule(@PathVariable Long scheduleId, @RequestBody FlightScheduleUpdateDTO scheduleDTO) throws FlightScheduleNotFoundException {
        FlightDTO updatedSchedule = flightService.updateFlightSchedule(scheduleId, scheduleDTO);
        return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
    }
}
