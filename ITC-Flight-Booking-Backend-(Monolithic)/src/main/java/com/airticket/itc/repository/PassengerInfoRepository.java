package com.airticket.itc.repository;

import com.airticket.itc.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airticket.itc.entity.PassengerInfo;

import java.util.List;

@Repository
public interface PassengerInfoRepository extends JpaRepository<PassengerInfo, Long> {
    List<PassengerInfo> findAllByBooking(Booking booking);

}
