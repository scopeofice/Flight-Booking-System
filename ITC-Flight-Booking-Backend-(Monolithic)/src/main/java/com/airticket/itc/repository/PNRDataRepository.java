package com.airticket.itc.repository;

import com.airticket.itc.entity.PNRData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PNRDataRepository extends JpaRepository<PNRData, Long> {
    Optional<PNRData> findByPNR(String pnr);
    boolean existsByPNR(String pnr);
}
