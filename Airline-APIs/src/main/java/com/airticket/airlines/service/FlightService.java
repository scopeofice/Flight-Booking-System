package com.airticket.airlines.service;

import com.airticket.airlines.dto.FlightCreateUpdateDTO;
import com.airticket.airlines.dto.FlightDTO;
import com.airticket.airlines.dto.FlightScheduleCreateUpdateDTO;
import com.airticket.airlines.dto.FlightScheduleUpdateDTO;
import com.airticket.airlines.exception.FlightNotFoundException;
import com.airticket.airlines.exception.FlightScheduleNotFoundException;

import java.util.List;

public interface FlightService {
    List<FlightDTO> getFlightDetailsByFlightName(String name);
    List<FlightDTO> getFlightDetailsBySourceDestinationAndTravelDate(String source,String destination,String travelDate);
    List<FlightDTO> getAllFlightDetails();
    String addFlight(FlightCreateUpdateDTO flightDTO);
    String deleteFlight(String flightName);
    String addFlightSchedule(FlightScheduleCreateUpdateDTO scheduleDTO) throws FlightNotFoundException;
    String deleteFlightSchedule(Long scheduleId) throws FlightScheduleNotFoundException;
    FlightDTO updateFlightSchedule(Long scheduleId, FlightScheduleUpdateDTO scheduleDTO) throws FlightScheduleNotFoundException;
}
