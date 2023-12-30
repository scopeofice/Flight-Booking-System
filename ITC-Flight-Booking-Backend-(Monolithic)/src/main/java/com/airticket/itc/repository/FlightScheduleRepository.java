package com.airticket.itc.repository;

import com.airticket.itc.entity.FlightSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightScheduleRepository extends JpaRepository<FlightSchedule, Long> {
    List<FlightSchedule> findBySourceAndDestinationAndTravelDate(String source, String destination, LocalDate travelDate);
    Optional<FlightSchedule> findById(Long id);
    Optional<FlightSchedule> findByScheduleCode(String code);
    List<FlightSchedule> findByFlight_FlightName(String flightName);
}
