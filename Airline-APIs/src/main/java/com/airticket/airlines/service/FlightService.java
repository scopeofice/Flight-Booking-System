package com.airticket.airlines.service;

import com.airticket.airlines.dto.FlightCreateUpdateDTO;
import com.airticket.airlines.dto.FlightDTO;
import com.airticket.airlines.exception.FlightNotFoundException;

import java.util.List;

public interface FlightService {
    List<FlightDTO> getFlightDetailsByFlightName(String name);
    List<FlightDTO> getFlightDetailsBySourceDestinationAndTravelDate(String source,String destination,String travelDate);
    List<FlightDTO> getAllFlightDetails();
    FlightDTO addFlight(FlightCreateUpdateDTO flightDTO);
    void deleteFlight(String flightName);
}
