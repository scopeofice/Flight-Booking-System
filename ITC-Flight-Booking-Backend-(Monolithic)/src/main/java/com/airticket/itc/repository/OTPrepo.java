package com.airticket.itc.repository;

import com.airticket.itc.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPrepo extends JpaRepository<OTP,Long> {
    Optional<OTP> findByEmail(String email);
    Boolean existsByEmail(String email);
}
