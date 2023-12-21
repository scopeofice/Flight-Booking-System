package com.airticket.airlines.service;

import com.airticket.airlines.dto.FlightCreateUpdateDTO;
import com.airticket.airlines.dto.FlightDTO;
import com.airticket.airlines.dto.FlightScheduleCreateUpdateDTO;
import com.airticket.airlines.dto.FlightScheduleUpdateDTO;
import com.airticket.airlines.entity.FlightSchedule;
import com.airticket.airlines.entity.Flights;
import com.airticket.airlines.exception.FlightNotFoundException;
import com.airticket.airlines.exception.FlightScheduleNotFoundException;
import com.airticket.airlines.repository.FlightScheduleRepository;
import com.airticket.airlines.repository.FlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightScheduleRepository flightScheduleRepository;
    @Autowired
    private FlightsRepository flightsRepository;

    @Override
    public List<FlightDTO> getFlightDetailsByFlightName(String flightName) {
        List<FlightSchedule> flightSchedules = flightScheduleRepository.findByFlight_FlightName(flightName);
        return flightSchedules.stream()
                .map(this::mapToFlightDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlightDTO> getFlightDetailsBySourceDestinationAndTravelDate(
            String source, String destination, String travelDate) {
        LocalDate parsedTravelDate = LocalDate.parse(travelDate);
        List<FlightSchedule> flightSchedules = flightScheduleRepository
                .findBySourceAndDestinationAndTravelDate(source, destination, parsedTravelDate);
        return flightSchedules.stream()
                .map(this::mapToFlightDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlightDTO> getAllFlightDetails() {
        List<FlightSchedule> flightSchedules = flightScheduleRepository.findAll();
        return flightSchedules.stream()
                .map(this::mapToFlightDTO)
                .collect(Collectors.toList());
    }

    private FlightDTO mapToFlightDTO(FlightSchedule flightSchedule) {
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setSource(flightSchedule.getSource());
        flightDTO.setDestination(flightSchedule.getDestination());
        flightDTO.setTravelDate(flightSchedule.getTravelDate());
        flightDTO.setPickupTime(flightSchedule.getTakeoffTime().toString());
        flightDTO.setArrivalTime(flightSchedule.getArrivalTime().toString());
        flightDTO.setFlightName(flightSchedule.getFlight().getFlightName());
        flightDTO.setSeat(flightSchedule.getFlight().getNumberOfSeats());
        return flightDTO;
    }

    @Override
    public String addFlight(FlightCreateUpdateDTO flightDTO) {
        Flights newFlight = Flights.builder()
                .flightName(flightDTO.getFlightName())
                .numberOfSeats(flightDTO.getNumberOfSeats())
                .build();
        flightsRepository.save(newFlight);
        return "Flight added Successfully";
    }
    @Override
    public String deleteFlight(String flightName) {
        List<FlightSchedule> schedulesToDelete = flightScheduleRepository.findByFlight_FlightName(flightName);
        flightScheduleRepository.deleteAll(schedulesToDelete);
        return "Flight deleted Successfully";
    }
    @Override
    public String addFlightSchedule(FlightScheduleCreateUpdateDTO scheduleDTO) throws FlightNotFoundException {
        Flights flight = flightsRepository.findByFlightName(scheduleDTO.getFlightName()).orElseThrow(()->new FlightNotFoundException("No flight with name: "+scheduleDTO.getFlightName()+" found."));

        FlightSchedule newSchedule = FlightSchedule.builder()
                .flight(flight)
                .source(scheduleDTO.getSource())
                .destination(scheduleDTO.getDestination())
                .travelDate(scheduleDTO.getTravelDate())
                .takeoffTime(scheduleDTO.getTakeoffTime())
                .arrivalTime(scheduleDTO.getArrivalTime())
                .build();

        flightScheduleRepository.save(newSchedule);
        return "Flight Schedule added Successfully";
    }
    @Override
    public String deleteFlightSchedule(Long scheduleId) throws FlightScheduleNotFoundException {
        FlightSchedule scheduleToDelete = flightScheduleRepository.findById(scheduleId).orElseThrow(() ->
                new FlightScheduleNotFoundException("Flight schedule with ID " + scheduleId + " not found"));

        flightScheduleRepository.delete(scheduleToDelete);
        return "Flight Schedule deleted Successfully";
    }

    @Override
    public FlightDTO updateFlightSchedule(Long scheduleId, FlightScheduleUpdateDTO scheduleDTO) throws FlightScheduleNotFoundException {
        FlightSchedule existingSchedule = flightScheduleRepository.findById(scheduleId).orElseThrow(() ->
                new FlightScheduleNotFoundException("Flight schedule with ID " + scheduleId + " not found"));

        existingSchedule.setSource(scheduleDTO.getSource());
        existingSchedule.setDestination(scheduleDTO.getDestination());
        existingSchedule.setTravelDate(scheduleDTO.getTravelDate());
        existingSchedule.setTakeoffTime(scheduleDTO.getTakeoffTime());
        existingSchedule.setArrivalTime(scheduleDTO.getArrivalTime());

        flightScheduleRepository.save(existingSchedule);
        return mapToFlightDTO(existingSchedule);
    }

}
