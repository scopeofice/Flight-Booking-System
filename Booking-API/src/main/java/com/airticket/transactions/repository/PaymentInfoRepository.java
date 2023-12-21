package com.airticket.transactions.repository;

import com.airticket.transactions.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airticket.transactions.entity.PaymentInfo;

import java.util.Optional;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Integer> {
    Optional<PaymentInfo> findByBooking(Booking booking);
}
