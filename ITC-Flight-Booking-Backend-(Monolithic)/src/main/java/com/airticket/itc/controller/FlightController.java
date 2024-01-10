package com.airticket.itc.controller;

import com.airticket.itc.dto.FlightScheduleCreateDTO;
import com.airticket.itc.dto.FlightScheduleUpdateDTO;
import com.airticket.itc.dto.FlightsDTO;
import com.airticket.itc.dto.FlightScheduleDTO;
import com.airticket.itc.exception.FlightAlreadyExistsException;
import com.airticket.itc.exception.FlightNameingException;
import com.airticket.itc.exception.FlightNotFoundException;
import com.airticket.itc.exception.FlightScheduleNotFoundException;
import com.airticket.itc.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "http://localhost:3000")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/byFlightName/{flightName}")
    public List<FlightScheduleDTO> getFlightDetailsByFlightName(@PathVariable String flightName) throws FlightScheduleNotFoundException {
        return flightService.getFlightDetailsByFlightName(flightName);
    }

    @GetMapping("/bySourceDestinationAndTravelDate")
    public List<FlightScheduleDTO> getFlightDetailsBySourceDestinationAndTravelDate(
            @RequestParam String source, @RequestParam String destination, @RequestParam String travelDate) throws FlightScheduleNotFoundException {
        return flightService.getFlightDetailsBySourceDestinationAndTravelDate(source, destination, travelDate);
    }

    @GetMapping("/schedule/all")
    public List<FlightScheduleDTO> getAllFlightScheduleDetails() {
        return flightService.getAllFlightDetails();
    }

    @GetMapping("/all")
    public List<FlightsDTO> getAllFlightDetails(){ return flightService.getAllFlights(); }

    @PostMapping("/addFlight")
    public ResponseEntity<String> addFlight(@RequestBody FlightsDTO flightDTO) throws FlightNameingException, FlightAlreadyExistsException {
        String addedFlight = flightService.addFlight(flightDTO);
        return new ResponseEntity<>(addedFlight, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteFlight/{flightName}")
    public ResponseEntity<String> deleteFlight(@PathVariable String flightName) throws FlightScheduleNotFoundException {
        String resp = flightService.deleteFlight(flightName);
        return new ResponseEntity<>(resp,HttpStatus.NO_CONTENT);
    }

    @PostMapping("/addFlightSchedule")
    public ResponseEntity<String> addFlightSchedule(@RequestBody FlightScheduleCreateDTO scheduleDTO) throws FlightNotFoundException {
        String  addedSchedule = flightService.addFlightSchedule(scheduleDTO);
        return new ResponseEntity<>(addedSchedule, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteFlightSchedule/{code}")
    public ResponseEntity<String > deleteFlightSchedule(@PathVariable String code) throws FlightScheduleNotFoundException {
        String resp = flightService.deleteFlightSchedule(code);
        return new ResponseEntity<>(resp,HttpStatus.NO_CONTENT);
    }
    @PutMapping("/updateFlightSchedule")
    public ResponseEntity<FlightScheduleDTO> updateFlightSchedule(@RequestBody FlightScheduleUpdateDTO scheduleDTO) throws FlightScheduleNotFoundException, FlightNotFoundException {
        FlightScheduleDTO updatedSchedule = flightService.updateFlightSchedule(scheduleDTO);
        return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
    }
    @PutMapping("/updateFlight")
    public ResponseEntity<FlightsDTO> updateFlight(@RequestBody FlightsDTO flightsDTO){
        FlightsDTO updatedFlight = flightService.updateFlight(flightsDTO);
        return new ResponseEntity<>(updatedFlight,HttpStatus.OK);
    }
}
