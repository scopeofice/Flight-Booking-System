package com.airticket.itc.repository;

import com.airticket.itc.entity.Booking;
import com.airticket.itc.entity.FlightSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser_Email(String email);
    List<Booking> findByFlight(FlightSchedule flight);
    Optional<Booking> findByBookingId(String bookingId);
    boolean existsByBookingId(String bookingId);
}
