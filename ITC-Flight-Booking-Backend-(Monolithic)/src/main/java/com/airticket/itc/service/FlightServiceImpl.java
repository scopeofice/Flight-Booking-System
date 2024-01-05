package com.airticket.itc.service;


import com.airticket.itc.dto.FlightScheduleCreateDTO;
import com.airticket.itc.dto.FlightScheduleUpdateDTO;
import com.airticket.itc.dto.FlightsDTO;
import com.airticket.itc.dto.FlightScheduleDTO;
import com.airticket.itc.entity.FlightSchedule;
import com.airticket.itc.entity.Flights;
import com.airticket.itc.entity.Status;
import com.airticket.itc.exception.FlightNotFoundException;
import com.airticket.itc.exception.FlightScheduleNotFoundException;
import com.airticket.itc.exception.FlightScheduleUpdateException;
import com.airticket.itc.repository.FlightScheduleRepository;
import com.airticket.itc.repository.FlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightScheduleRepository flightScheduleRepository;
    @Autowired
    private FlightsRepository flightsRepository;

    @Override
    public List<FlightScheduleDTO> getFlightDetailsByFlightName(String flightName) throws FlightScheduleNotFoundException {
        List<FlightSchedule> flightSchedules = flightScheduleRepository.findByFlight_FlightName(flightName);
        List<FlightScheduleDTO> activeFlights = flightSchedules.stream().filter(a->a.getStatus().equals(Status.STATUS_ACTIVE))
                .map(this::mapToFlightScheduleDTO)
                .collect(Collectors.toList());
        if(activeFlights.isEmpty()){
            throw new FlightScheduleNotFoundException("No active flights found") ;
        }else {
            return activeFlights;
        }
    }

    @Override
    public List<FlightScheduleDTO> getFlightDetailsBySourceDestinationAndTravelDate(
            String source, String destination, String travelDate) throws FlightScheduleNotFoundException {
        LocalDate parsedTravelDate = LocalDate.parse(travelDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<FlightSchedule> flightSchedules = flightScheduleRepository
                .findBySourceAndDestinationAndTravelDate(source, destination, parsedTravelDate);
        List<FlightScheduleDTO> activeFlights = flightSchedules.stream().filter(a->a.getStatus().equals(Status.STATUS_ACTIVE))
                .map(this::mapToFlightScheduleDTO)
                .collect(Collectors.toList());
        if(activeFlights.isEmpty()){
            throw new FlightScheduleNotFoundException("No active flights found") ;
        }else {
            return activeFlights;
        }
    }

    @Override
    public List<FlightScheduleDTO> getAllFlightDetails() {
        List<FlightSchedule> flightSchedules = flightScheduleRepository.findAll();
        return flightSchedules.stream()
                .map(this::mapToFlightScheduleDTO)
                .collect(Collectors.toList());
    }

    private FlightScheduleDTO mapToFlightScheduleDTO(FlightSchedule flightSchedule) {

        FlightScheduleDTO flightDTO = new FlightScheduleDTO();
        flightDTO.setSource(flightSchedule.getSource());
        flightDTO.setScheduleCode(flightSchedule.getScheduleCode());
        flightDTO.setDestination(flightSchedule.getDestination());
        flightDTO.setTravelDate(flightSchedule.getTravelDate().toString());
        flightDTO.setPickupTime(flightSchedule.getTakeoffTime().toString());
        flightDTO.setArrivalTime(flightSchedule.getArrivalTime().toString());
        flightDTO.setFlightName(flightSchedule.getFlight().getFlightName());
        flightDTO.setStatus(flightSchedule.getStatus().toString());
        flightDTO.setAvailableSeat(flightSchedule.getAvailableSeats());
        flightDTO.setFare(flightSchedule.getFlight().getFare());
        return flightDTO;
    }

    @Override
    public String addFlight(FlightsDTO flightDTO) {
        Flights newFlight = Flights.builder()
                .flightName(flightDTO.getFlightName().toUpperCase())
                .numberOfSeats(flightDTO.getNumberOfSeats())
                .fare(flightDTO.getFare())
                .build();
        flightsRepository.save(newFlight);
        return "Flight added Successfully";
    }
    @Override
    public String deleteFlight(String flightName) throws FlightScheduleNotFoundException {
        Flights flightToDelete = flightsRepository.findByFlightName(flightName).orElseThrow(()->new FlightScheduleNotFoundException("No flight"));
        flightsRepository.delete(flightToDelete);
        return "Flight deleted Successfully";
    }
    @Override
    public String addFlightSchedule(FlightScheduleCreateDTO scheduleDTO) throws FlightNotFoundException {
        Flights flight = flightsRepository.findByFlightName(scheduleDTO.getFlightName()).orElseThrow(()->new FlightNotFoundException("No flight with name: "+scheduleDTO.getFlightName()+" found."));

        FlightSchedule newSchedule = FlightSchedule.builder()
                .flight(flight)
                .source(scheduleDTO.getSource().toUpperCase())
                .status(Status.STATUS_ACTIVE)
                .availableSeats(flight.getNumberOfSeats())
                .destination(scheduleDTO.getDestination().toUpperCase())
                .travelDate(LocalDate.parse(scheduleDTO.getTravelDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")))
                .takeoffTime(LocalTime.parse(scheduleDTO.getPickupTime()))
                .arrivalTime(LocalTime.parse(scheduleDTO.getArrivalTime()))
                .build();

        flightScheduleRepository.save(newSchedule);
        return "Flight Schedule added Successfully";
    }
    @Override
    public String deleteFlightSchedule(String code) throws FlightScheduleNotFoundException {
        FlightSchedule scheduleToDelete = flightScheduleRepository.findByScheduleCode(code).orElseThrow(() ->
                new FlightScheduleNotFoundException("Flight schedule with ID " + code + " not found"));

        flightScheduleRepository.delete(scheduleToDelete);
        return "Flight Schedule deleted Successfully";
    }

    @Override
    public FlightsDTO updateFlight(FlightsDTO flight) {
        if(flightsRepository.existsByFlightName(flight.getFlightName())){
            Flights updateFlight = flightsRepository.findByFlightName(flight.getFlightName()).get();
            updateFlight.setFare(flight.getFare());
            updateFlight.setNumberOfSeats(flight.getNumberOfSeats());
        }
        return null;
    }

    @Override
    public FlightScheduleDTO updateFlightSchedule(FlightScheduleUpdateDTO scheduleDTO) throws FlightScheduleNotFoundException, FlightNotFoundException {
        FlightSchedule existingSchedule = flightScheduleRepository.findByScheduleCode(scheduleDTO.getScheduleCode()).orElseThrow(() ->
                new FlightScheduleNotFoundException("Flight schedule with ID " + scheduleDTO.getScheduleCode() + " not found"));
        if(existingSchedule.getTravelDate().isBefore(LocalDate.now())){
            throw new FlightScheduleUpdateException("Flight can not be updated, Flight already landed");
        } else if (existingSchedule.getArrivalTime().isBefore(LocalTime.now())) {
            throw new FlightScheduleUpdateException("Flight can not be updated, Flight already landed");
        }
        existingSchedule.setSource(scheduleDTO.getSource().toUpperCase());
        existingSchedule.setDestination(scheduleDTO.getDestination().toUpperCase());
        existingSchedule.setAvailableSeats(scheduleDTO.getAvailableSeats());
        existingSchedule.setFlight(flightsRepository.findByFlightName(scheduleDTO.getFlightName().toUpperCase()).orElseThrow(()->new FlightNotFoundException("No flight with name "+scheduleDTO.getFlightName()+" found")));
        if(scheduleDTO.getStatus().equalsIgnoreCase("inactive")){
            existingSchedule.setStatus(Status.STATUS_INACTIVE);
        }else if (scheduleDTO.getStatus().equalsIgnoreCase("active")){
            existingSchedule.setStatus(Status.STATUS_ACTIVE);
        }else {
            throw new FlightScheduleNotFoundException("Invalid status entered");
        }
        existingSchedule.setTravelDate(LocalDate.parse(scheduleDTO.getTravelDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        existingSchedule.setTakeoffTime(LocalTime.parse(scheduleDTO.getPickupTime()));
        existingSchedule.setArrivalTime(LocalTime.parse(scheduleDTO.getArrivalTime()));

        flightScheduleRepository.save(existingSchedule);
        return mapToFlightScheduleDTO(existingSchedule);
    }

}
