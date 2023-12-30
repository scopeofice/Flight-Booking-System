package com.airticket.itc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flights {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long flightId;
        @Column(unique = true)
        private String flightName;
        private int numberOfSeats;
        private int availableSeats;
        private double fare;

}
