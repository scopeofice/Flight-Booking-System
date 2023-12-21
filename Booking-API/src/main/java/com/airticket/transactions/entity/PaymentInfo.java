package com.airticket.transactions.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name="Payment_Info")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class PaymentInfo {

	@Id
	@GeneratedValue(generator = "uuid2")
	private int paymentId;
	private String accountNo;
	private double totalAmount;
	@OneToOne
	@JoinColumn(name = "booking_id")
	@JsonIgnore
	private Booking booking;


}
