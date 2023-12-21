package com.airticket.airlines.repository;

import com.airticket.airlines.entity.FlightSchedule;
import com.airticket.airlines.entity.Flights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightsRepository extends JpaRepository<Flights, Long> {
    Optional<Flights> findByFlightName(String name);
}

