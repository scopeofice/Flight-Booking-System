package com.airticket.itc.service;



import com.airticket.itc.dto.FlightScheduleCreateDTO;
import com.airticket.itc.dto.FlightScheduleUpdateDTO;
import com.airticket.itc.dto.FlightsDTO;
import com.airticket.itc.dto.FlightScheduleDTO;
import com.airticket.itc.exception.FlightNotFoundException;
import com.airticket.itc.exception.FlightScheduleNotFoundException;

import java.util.List;

public interface FlightService {
    List<FlightScheduleDTO> getFlightDetailsByFlightName(String name) throws FlightScheduleNotFoundException;
    List<FlightScheduleDTO> getFlightDetailsBySourceDestinationAndTravelDate(String source, String destination, String travelDate) throws FlightScheduleNotFoundException;
    List<FlightScheduleDTO> getAllFlightDetails();
    List<FlightsDTO> getAllFlights();
    FlightsDTO updateFlight(FlightsDTO flight);
    String addFlight(FlightsDTO flightDTO);
    String deleteFlight(String flightName) throws FlightScheduleNotFoundException;
    String addFlightSchedule(FlightScheduleCreateDTO scheduleDTO) throws FlightNotFoundException;
    String deleteFlightSchedule(String code) throws FlightScheduleNotFoundException;
    FlightScheduleDTO updateFlightSchedule(FlightScheduleUpdateDTO scheduleDTO) throws FlightScheduleNotFoundException, FlightNotFoundException;

}
