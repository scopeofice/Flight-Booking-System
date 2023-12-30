package com.airticket.itc.repository;


import com.airticket.itc.entity.Flights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface FlightsRepository extends JpaRepository<Flights, Long> {
    Optional<Flights> findByFlightName(String name);
    boolean existsByFlightName(String name);
}

