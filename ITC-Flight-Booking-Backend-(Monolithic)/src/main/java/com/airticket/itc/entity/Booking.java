package com.airticket.itc.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String bookingId;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userid")
    private User user;
    @ManyToOne
    @JoinColumn(name = "flight_schedule_id", nullable = false)
    @JsonIgnore
    private FlightSchedule flight;
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PassengerInfo> passengerInfoList;
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private PaymentInfo paymentInfo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate bookingDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime bookingTime;

}
