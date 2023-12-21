package com.airticket.transactions.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.airticket.transactions.dto.*;
import org.springframework.transaction.annotation.Transactional;
import com.airticket.transactions.entity.Booking;
import com.airticket.transactions.entity.PNRData;
import com.airticket.transactions.entity.PassengerInfo;
import com.airticket.transactions.exception.BookingNotFoundException;
import com.airticket.transactions.exception.InvalidPNRNumberException;
import com.airticket.transactions.repository.BookingRepository;
import com.airticket.transactions.repository.PNRDataRepository;
import com.airticket.transactions.repository.PassengerInfoRepository;
import com.airticket.transactions.repository.PaymentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.airticket.transactions.entity.PaymentInfo;


@Service
@Transactional
public class FlightBookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private PassengerInfoRepository passengerInfoRepository;

	@Autowired
	private PaymentInfoRepository paymentInfoRepository;

	@Autowired
	private PNRDataRepository pnrDataRepository;

	public FlightBookingAcknowledgement bookFlightTicket(FlightBookingRequest flightBookingRequest) {

		List<PassengerInfo> passengerInfoList = flightBookingRequest.getBookingInfo().getPassengerInfoList().stream()
				.map(passengerDTO -> PassengerInfo.builder()
						.firstName(passengerDTO.getFirstName())
						.lastName(passengerDTO.getLastName())
						.age(passengerDTO.getAge())
						.seatNumber(passengerDTO.getSeatNumber())
						.gender(passengerDTO.getGender())
						.build())
				.collect(Collectors.toList());

		PaymentInfo paymentInfo = PaymentInfo.builder()
				.accountNo(flightBookingRequest.getBookingInfo().getPaymentInfo().getAccountNo())
				.totalAmount(flightBookingRequest.getBookingInfo().getPaymentInfo().getTotalAmount())
				.build();

		paymentInfo = paymentInfoRepository.save(paymentInfo);

		Booking bookingInfo = Booking.builder()
				.flightId(flightBookingRequest.getBookingInfo().getFlightId())
				.bookingDate(LocalDate.now())
				.bookingTime(LocalTime.now())
				.passengerInfoList(passengerInfoList)
				.paymentInfo(paymentInfo)
				.build();

		passengerInfoList.forEach(passengerInfo -> passengerInfo.setBooking(bookingInfo));

		passengerInfoRepository.saveAll(passengerInfoList);

		paymentInfo.setBooking(bookingInfo);

		String pnr = UUID.randomUUID().toString().split("-")[0];

		PNRData newPNR = PNRData.builder()
				.PNR(pnr)
				.booking(bookingInfo)
				.build();

		pnrDataRepository.save(newPNR);

		bookingRepository.save(bookingInfo);


		return new FlightBookingAcknowledgement(
				"SUCCESS",bookingInfo.getPaymentInfo().getTotalAmount(),pnr,passengerInfoList
		);
	}


	public FlightBookingAcknowledgement bookingDetailsByPNR(String pnr){
		if(pnrDataRepository.existsByPNR(pnr)){
			PNRData pnrData = pnrDataRepository.findByPNR(pnr).orElseThrow(()->new InvalidPNRNumberException("Wrong PNR number entered"));
			return new FlightBookingAcknowledgement(
					"SUCCESS",pnrData.getBooking().getPaymentInfo().getTotalAmount(),pnr,pnrData.getBooking().getPassengerInfoList()
			);
		}else {
			return new FlightBookingAcknowledgement("FAILED",0.0,null,null);
		}
	}


	public BookingDTO bookingDetailsById(Long bookingId){
		if(bookingRepository.existsById(bookingId)){

			Booking booking = bookingRepository.findById(bookingId).orElseThrow(()->new BookingNotFoundException("No Bookings available"));

			List<PassengerInfo> passengerInfoList = passengerInfoRepository.findAllByBooking(booking);
			List<PassengerDTO> passengerDTOList = passengerInfoList.stream().map((passengerDTO)->PassengerDTO.builder().firstName(passengerDTO.getFirstName())
					.lastName(passengerDTO.getLastName())
					.age(passengerDTO.getAge())
					.seatNumber(passengerDTO.getSeatNumber())
					.gender(passengerDTO.getGender()).build()).collect(Collectors.toList());

			PaymentInfo paymentInfo = paymentInfoRepository.findByBooking(booking).orElseThrow(()->new BookingNotFoundException("Payment with this booking not found"));

			PaymentDTO paymentDTO = PaymentDTO.builder()
					.accountNo(paymentInfo.getAccountNo())
					.totalAmount(paymentInfo.getTotalAmount())
					.build();

			BookingDTO bookingDTO = BookingDTO.builder()
					.flightId(booking.getFlightId())
					.passengerInfoList(passengerDTOList)
					.paymentInfo(paymentDTO)
					.build();
			return bookingDTO;
		}else {
			return new BookingDTO(null,null,null);
		}
	}
}

