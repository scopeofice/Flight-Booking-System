package com.airticket.itc.repository;

import com.airticket.itc.entity.Booking;
import com.airticket.itc.entity.PNRData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PNRDataRepository extends JpaRepository<PNRData, Long> {
    Optional<PNRData> findByPNR(String pnr);
    Optional<PNRData> findByBooking(Booking booking);
    boolean existsByPNR(String pnr);
}
