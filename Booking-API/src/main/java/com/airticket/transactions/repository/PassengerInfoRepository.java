package com.airticket.transactions.repository;

import com.airticket.transactions.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airticket.transactions.entity.PassengerInfo;

import java.util.List;

@Repository
public interface PassengerInfoRepository extends JpaRepository<PassengerInfo, Long> {
    List<PassengerInfo> findAllByBooking(Booking booking);

}
