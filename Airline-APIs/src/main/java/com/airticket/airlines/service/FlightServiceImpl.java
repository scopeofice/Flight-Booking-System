package com.airticket.airlines.service;

import com.airticket.airlines.dto.FlightDTO;
import com.airticket.airlines.entity.FlightSchedule;
import com.airticket.airlines.repository.FlightScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightScheduleRepository flightScheduleRepository;

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
}
