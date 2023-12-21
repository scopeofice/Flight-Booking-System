package com.airticket.airlines.repository;

import com.airticket.airlines.entity.FlightSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlightScheduleRepository extends JpaRepository<FlightSchedule, Long> {
    List<FlightSchedule> findByFlight_FlightName(String flightName);
    List<FlightSchedule> findBySourceAndDestinationAndTravelDate(String source, String destination, LocalDate travelDate);
}
