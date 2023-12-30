package com.airticket.itc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="PassengerInfo")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pid;
	private String firstName;
	private String lastName;
	private int age;
	private int seatNumber;
	private String gender;
	@ManyToOne
	@JoinColumn(name = "booking_id", referencedColumnName = "bookingId")
	private Booking booking;
}
