package com.airticket.airlines.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightSchedule {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long scheduleId;

        @ManyToOne
        @JoinColumn(name = "flight_id", nullable = false)
        private Flights flight;

        private String source;
        private String destination;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        private LocalDate travelDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        private LocalTime takeoffTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        private LocalTime arrivalTime;

}
