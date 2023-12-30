package com.airticket.itc.repository;

import com.airticket.itc.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airticket.itc.entity.PaymentInfo;

import java.util.Optional;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Integer> {
}
