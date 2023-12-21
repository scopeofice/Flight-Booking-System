package com.airticket.transactions.repository;

import com.airticket.transactions.entity.Booking;
import com.airticket.transactions.entity.PassengerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

}
