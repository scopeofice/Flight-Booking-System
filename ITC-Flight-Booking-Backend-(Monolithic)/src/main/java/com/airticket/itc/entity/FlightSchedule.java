package com.airticket.itc.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightSchedule {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long scheduleId;

        @Column(unique = true, nullable = false)
        private String scheduleCode;

        private Status status;

        @ManyToOne
        @JoinColumn(name = "flight_id", nullable = false)
        private Flights flight;
        private int availableSeats;

        private String source;
        private String destination;

//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        private LocalDate travelDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        private LocalTime takeoffTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        private LocalTime arrivalTime;


        @PrePersist
        public void generateScheduleCode() {
                Random random = new Random();
                int randomNumber = 1000 + random.nextInt(9000);
                this.scheduleCode = String.valueOf(randomNumber);
        }
        @PreUpdate
        public void updateStatusBasedOnDateTime() {
                LocalDateTime currentDateTime = LocalDateTime.now();
                LocalDateTime scheduledDateTime = LocalDateTime.of(this.travelDate, this.takeoffTime);

                if (scheduledDateTime.isBefore(currentDateTime)) {
                        this.status = Status.STATUS_INACTIVE;
                }
        }

}
