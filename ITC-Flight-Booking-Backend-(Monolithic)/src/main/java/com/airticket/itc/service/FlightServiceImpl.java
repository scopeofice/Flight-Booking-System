package com.airticket.itc.service;


import com.airticket.itc.dto.FlightScheduleCreateDTO;
import com.airticket.itc.dto.FlightScheduleUpdateDTO;
import com.airticket.itc.dto.FlightsDTO;
import com.airticket.itc.dto.FlightScheduleDTO;
import com.airticket.itc.entity.*;
import com.airticket.itc.exception.*;
import com.airticket.itc.repository.BookingRepository;
import com.airticket.itc.repository.FlightScheduleRepository;
import com.airticket.itc.repository.FlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightScheduleRepository flightScheduleRepository;
    @Autowired
    private FlightsRepository flightsRepository;
    @Autowired
    private BookingRepository bookingRepository;

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
        LocalDate parsedTravelDate = LocalDate.parse(travelDate);
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
        int totalSeats = flightSchedule.getFlight().getNumberOfSeats();
        Set<Integer> bookedSeats = new HashSet<>();

        List<Booking> bookings = bookingRepository.findByFlight(flightSchedule);
        for (Booking booking : bookings) {
            for (PassengerInfo passengerInfo : booking.getPassengerInfoList()) {
                bookedSeats.add(passengerInfo.getSeatNumber());
            }
        }
        List<Integer> availableSeats = new ArrayList<>();
        for (int seatNumber = 1; seatNumber <= totalSeats; seatNumber++) {
            if (!bookedSeats.contains(seatNumber)) {
                availableSeats.add(seatNumber);
            }
        }

        flightDTO.setAvailableSeats(availableSeats);
        return flightDTO;
    }

    @Override
    public String addFlight(FlightsDTO flightDTO) throws FlightAlreadyExistsException, FlightNameingException {
        if(flightsRepository.existsByFlightName(flightDTO.getFlightName())){
            throw new FlightAlreadyExistsException("Flight with name "+flightDTO.getFlightName()+" already exists");
        } else if (flightDTO.getFlightName() == "") {
            throw new FlightNameingException("Flight name cannot be blank");
        }
        if(flightDTO.getAirlineName() == "")
        {
            throw new FlightNameingException("Airline name cannot be blank");
        }
        Flights newFlight = Flights.builder()
                .flightName(flightDTO.getFlightName().toUpperCase())
                .airlineName(flightDTO.getAirlineName())
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
                .travelDate(LocalDate.parse(scheduleDTO.getTravelDate()))
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
    public List<FlightsDTO> getAllFlights() {
        List<Flights> allFlights = flightsRepository.findAll();

        return allFlights.stream().map(flights -> FlightsDTO.builder()
                .numberOfSeats(flights.getNumberOfSeats())
                .flightName(flights.getFlightName())
                .airlineName((flights.getAirlineName()))
                .fare(flights.getFare())
                .build()).collect(Collectors.toList());
    }

    @Override
    public FlightsDTO updateFlight(FlightsDTO flight) {
        if(flightsRepository.existsByFlightName(flight.getFlightName())){
            Flights updatedFlight = flightsRepository.findByFlightName(flight.getFlightName()).get();
            updatedFlight.setFare(flight.getFare());
            updatedFlight.setNumberOfSeats(flight.getNumberOfSeats());
            updatedFlight.setAirlineName(flight.getAirlineName());
            flightsRepository.save(updatedFlight);
            return FlightsDTO.builder()
                    .flightName(flight.getFlightName())
                    .fare(flight.getFare())
                    .airlineName(flight.getAirlineName())
                    .numberOfSeats(flight.getNumberOfSeats())
                    .build();
        }
        return null;
    }

    @Override
    public FlightScheduleDTO updateFlightSchedule(FlightScheduleUpdateDTO scheduleDTO) throws FlightScheduleNotFoundException, FlightNotFoundException {
        FlightSchedule existingSchedule = flightScheduleRepository.findByScheduleCode(scheduleDTO.getScheduleCode()).orElseThrow(() ->
                new FlightScheduleNotFoundException("Flight schedule with ID " + scheduleDTO.getScheduleCode() + " not found"));
        if(existingSchedule.getTravelDate().isBefore(LocalDate.now())){
            throw new FlightScheduleUpdateException("Flight can not be updated, Flight already landed");
        } else if(existingSchedule.getTravelDate().equals(LocalDate.now()) && existingSchedule.getArrivalTime().isBefore(LocalTime.now())){
            throw new FlightScheduleUpdateException("Flight can not be updated, Flight already landed");
        }
        System.out.println(existingSchedule.getAvailableSeats());
        System.out.println(flightsRepository.findByFlightName(existingSchedule.getFlight().getFlightName()).get().getNumberOfSeats());
        if(existingSchedule.getAvailableSeats() > flightsRepository.findByFlightName(existingSchedule.getFlight().getFlightName()).get().getNumberOfSeats()){
            throw new FlightScheduleUpdateException("Available seats cannot be more than the total flight seats");
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
        existingSchedule.setTravelDate(LocalDate.parse(scheduleDTO.getTravelDate()));
        existingSchedule.setTakeoffTime(LocalTime.parse(scheduleDTO.getPickupTime()));
        existingSchedule.setArrivalTime(LocalTime.parse(scheduleDTO.getArrivalTime()));

        flightScheduleRepository.save(existingSchedule);
        return mapToFlightScheduleDTO(existingSchedule);
    }

}
