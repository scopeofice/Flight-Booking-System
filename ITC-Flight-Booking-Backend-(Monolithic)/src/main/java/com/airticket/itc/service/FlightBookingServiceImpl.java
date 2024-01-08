package com.airticket.itc.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import com.airticket.itc.dto.*;
import com.airticket.itc.entity.*;
import com.airticket.itc.exception.*;
import com.airticket.itc.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class FlightBookingServiceImpl implements FlightBookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private PassengerInfoRepository passengerInfoRepository;

	@Autowired
	private PaymentInfoRepository paymentInfoRepository;

	@Autowired
	private FlightScheduleRepository flightScheduleRepository;

	@Autowired
	private FlightsRepository flightsRepository;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PNRDataRepository pnrDataRepository;

	@Autowired
	PasswordEncoder passwordEncoder;


	@Override
	public FlightBookingAcknowledgement bookFlightTicket(BookingDTO flightBookingRequest) {
		FlightSchedule flightSchedule = flightScheduleRepository.findByScheduleCode(flightBookingRequest.getScheduleCode()).get();

		if (flightBookingRequest.getPassengerInfoList().size() > flightSchedule.getAvailableSeats()) {
			throw new BookingException("Seats not available");
		} else if (flightSchedule.getStatus().equals(Status.STATUS_INACTIVE)) {
			throw new BookingException("Flight cannot be booked");
		} else {

			Set<Integer> seatNumberSet = new HashSet<>();

			List<Booking> bookingList = bookingRepository.findByFlight(flightSchedule);
			for (Booking booking : bookingList) {
				List<PassengerInfo> passengers = booking.getPassengerInfoList();
				for (PassengerInfo passengerInfo : passengers) {
					seatNumberSet.add(passengerInfo.getSeatNumber());
				}
			}


			List<PassengerInfo> passengerInfoList = flightBookingRequest.getPassengerInfoList().stream()
					.map(passengerDTO -> PassengerInfo.builder()
							.firstName(passengerDTO.getFirstName())
							.lastName(passengerDTO.getLastName())
							.age(passengerDTO.getAge())
							.seatNumber(passengerDTO.getSeatNumber())
							.gender(passengerDTO.getGender())
							.build())
					.collect(Collectors.toList());

			int totalSeats = flightSchedule.getFlight().getNumberOfSeats();
			for (PassengerInfo passenger : passengerInfoList) {
				int seatNumb = passenger.getSeatNumber();
				if (seatNumb < 1 || seatNumb > totalSeats) {
					throw new BookingException("Seat number should be in the range of 1 - " + totalSeats);
				}
				if (!seatNumberSet.add(seatNumb)) {
					throw new BookingException("Seat already booked");
				}
			}

			Optional<User> currentUser = userRepo.findByEmail(flightBookingRequest.getEmail());


			PaymentInfo payment = PaymentInfo.builder()
					.accountNo(flightBookingRequest.getPaymentInfo().getAccountNo())
					.totalAmount(flightBookingRequest.getPaymentInfo().getTotalAmount())
					.build();

			if (payment.getTotalAmount() == flightSchedule.getFlight().getFare() * passengerInfoList.size()) {
				paymentInfoRepository.save(payment);
			} else {
				throw new TransactionFailedException("Wrong Amount");
			}


			Booking bookingInfo = Booking.builder()
					.flight(flightSchedule)
					.status(Status.STATUS_ACTIVE)
					.user(currentUser.get())
					.bookingDate(LocalDate.now())
					.bookingTime(LocalTime.now())
					.passengerInfoList(passengerInfoList)
					.paymentInfo(payment)
					.build();

			passengerInfoList.forEach(passengerInfo -> passengerInfo.setBooking(bookingInfo));

			passengerInfoRepository.saveAll(passengerInfoList);

			String pnr = UUID.randomUUID().toString().split("-")[0];

			PNRData newPNR = PNRData.builder()
					.PNR(pnr)
					.booking(bookingInfo)
					.build();

			pnrDataRepository.save(newPNR);

			flightSchedule.setAvailableSeats(flightSchedule.getAvailableSeats() - flightBookingRequest.getPassengerInfoList().size());
			flightScheduleRepository.save(flightSchedule);

			bookingRepository.save(bookingInfo);


			return new FlightBookingAcknowledgement(
					"SUCCESS", bookingInfo.getPaymentInfo().getTotalAmount(), pnr, passengerInfoList.stream().map(passenger -> PassengerDTO.builder()
					.age(passenger.getAge())
					.seatNumber(passenger.getSeatNumber())
					.lastName(passenger.getLastName())
					.firstName(passenger.getFirstName())
					.gender(passenger.getGender())
					.build()).collect(Collectors.toList())
			);
		}
	}


	@Override
	public FlightBookingAcknowledgement bookingDetailsByPNR(String pnr) {
		if (pnrDataRepository.existsByPNR(pnr)) {
			PNRData pnrData = pnrDataRepository.findByPNR(pnr).orElseThrow(() -> new InvalidPNRNumberException("Wrong PNR number entered"));
			String status = "SUCCESSFULLY BOOKED";
			if(pnrData.getBooking().getStatus().equals(Status.STATUS_INACTIVE)){
				status = "BOOKING CANCELLED";
			}
			return new FlightBookingAcknowledgement(
					status, pnrData.getBooking().getPaymentInfo().getTotalAmount(), pnr, pnrData.getBooking().getPassengerInfoList().stream().map(passenger -> PassengerDTO.builder()
					.age(passenger.getAge())
					.seatNumber(passenger.getSeatNumber())
					.lastName(passenger.getLastName())
					.firstName(passenger.getFirstName())
					.gender(passenger.getGender())
					.build()).collect(Collectors.toList())
			);
		} else {
			return new FlightBookingAcknowledgement("FAILED", 0.0, null, null);
		}
	}

	@Override
	public List<BookingResponseDTO> userBookings(String userEmail) {
		if (userRepo.existsByEmail(userEmail)) {
//			Optional<User> currentUser = userRepo.findByEmail(userEmail);
//			if (currentUser.isPresent()) {
			List<Booking> bookingList = bookingRepository.findByUser_Email(userEmail);

			return bookingList.stream().map(booking -> {
				List<PassengerInfo> passengerInfoList = passengerInfoRepository.findAllByBooking(booking);
				List<PassengerDTO> passengerDTOList = passengerInfoList.stream().map(passengerDTO -> PassengerDTO.builder()
						.firstName(passengerDTO.getFirstName())
						.lastName(passengerDTO.getLastName())
						.age(passengerDTO.getAge())
						.seatNumber(passengerDTO.getSeatNumber())
						.gender(passengerDTO.getGender())
						.build()).collect(Collectors.toList());


				PaymentResponseDTO paymentDTO = PaymentResponseDTO.builder()
						.paymentId(booking.getPaymentInfo().getPaymentId())
						.accountNo(booking.getPaymentInfo().getAccountNo())
						.totalAmount(booking.getPaymentInfo().getTotalAmount())
						.build();
				FlightScheduleDTO flightScheduleDTO = FlightScheduleDTO.builder()
						.flightName(booking.getFlight().getFlight().getFlightName())
						.scheduleCode(booking.getFlight().getScheduleCode())
						.source(booking.getFlight().getSource())
						.destination(booking.getFlight().getDestination())
						.status(booking.getFlight().getStatus().toString())
						.arrivalTime(booking.getFlight().getArrivalTime().toString())
						.pickupTime(booking.getFlight().getTakeoffTime().toString())
						.travelDate(booking.getFlight().getTravelDate().toString())
						.availableSeat(booking.getFlight().getFlight().getNumberOfSeats())
						.build();

				return BookingResponseDTO.builder()
						.bookingId(booking.getBookingId())
						.status(booking.getStatus().toString())
						.schedule(flightScheduleDTO)
						.email(userEmail)
						.passengerInfoList(passengerDTOList)
						.paymentInfo(paymentDTO)
						.build();
			}).collect(Collectors.toList());
		}
//		}
		return Collections.emptyList();
	}

	@Override
	public BookingResponseDTO bookingDetailsById(String bookingId) {
		if (bookingRepository.existsByBookingId(bookingId)) {

			Booking booking = bookingRepository.findByBookingId(bookingId).orElseThrow(() -> new BookingNotFoundException("No Bookings available"));

			List<PassengerInfo> passengerInfoList = passengerInfoRepository.findAllByBooking(booking);
			List<PassengerDTO> passengerDTOList = passengerInfoList.stream().map((passengerDTO) -> PassengerDTO.builder().firstName(passengerDTO.getFirstName())
					.lastName(passengerDTO.getLastName())
					.age(passengerDTO.getAge())
					.seatNumber(passengerDTO.getSeatNumber())
					.gender(passengerDTO.getGender()).build()).collect(Collectors.toList());


			PaymentResponseDTO paymentDTO = PaymentResponseDTO.builder()
					.paymentId(booking.getPaymentInfo().getPaymentId())
					.accountNo(booking.getPaymentInfo().getAccountNo())
					.totalAmount(booking.getPaymentInfo().getTotalAmount())
					.build();
			FlightScheduleDTO flightScheduleDTO = FlightScheduleDTO.builder()
					.flightName(booking.getFlight().getFlight().getFlightName())
					.arrivalTime(booking.getFlight().getArrivalTime().toString())
					.pickupTime(booking.getFlight().getTakeoffTime().toString())
					.availableSeat(booking.getFlight().getFlight().getNumberOfSeats())
					.build();

			BookingResponseDTO bookingDTO = BookingResponseDTO.builder()
					.bookingId(booking.getBookingId())
					.status(booking.getStatus().toString())
					.schedule(flightScheduleDTO)
					.passengerInfoList(passengerDTOList)
					.paymentInfo(paymentDTO)
					.build();
			return bookingDTO;
		} else {
			return new BookingResponseDTO(null,null, null, null, null, null);
		}
	}

	@Override
	public String updateBookings(String bookingID, BookingDTO updatedBooking) {
		if (bookingRepository.existsByBookingId(bookingID)) {
			Booking existingBooking = bookingRepository.findByBookingId(bookingID)
					.orElseThrow(() -> new BookingException("No Booking found with id " + bookingID));

			if (existingBooking.getFlight().getStatus().equals(Status.STATUS_ACTIVE)) {

				if (existingBooking.getPassengerInfoList().size() != updatedBooking.getPassengerInfoList().size()) {
					throw new BookingException("Cannot change the number of passengers.");
				}

				for (int i = 0; i < existingBooking.getPassengerInfoList().size(); i++) {
					PassengerInfo existingPassenger = existingBooking.getPassengerInfoList().get(i);
					PassengerDTO updatedPassengerDTO = updatedBooking.getPassengerInfoList().get(i);

					int updatedSeatNumber = updatedPassengerDTO.getSeatNumber();
					if (updatedSeatNumber < 1 || updatedSeatNumber > existingBooking.getFlight().getAvailableSeats()) {
						throw new BookingException("Seat number should be in the range of 1 - " + existingBooking.getFlight().getAvailableSeats());
					}
					if (!isSeatAvailable(existingBooking.getFlight(), updatedSeatNumber)) {
						throw new BookingException("Seat already booked");
					}
					existingPassenger.setSeatNumber(updatedSeatNumber);

					existingPassenger.setFirstName(updatedPassengerDTO.getFirstName());
					existingPassenger.setLastName(updatedPassengerDTO.getLastName());
					existingPassenger.setAge(updatedPassengerDTO.getAge());
					existingPassenger.setGender(updatedPassengerDTO.getGender());
				}

				passengerInfoRepository.saveAll(existingBooking.getPassengerInfoList());

				return "Booking Updated Successfully";
			} else {
				throw new BookingException("Cannot update booking for an inactive flight.");
			}
		} else {
			throw new BookingNotFoundException("No Booking found with id " + bookingID);
		}
	}

	@Override
	public String cancelBooking(BookingCancelDTO cancelRequest) {
		if(userRepo.existsByEmail(cancelRequest.getEmail())){
			User foundUser = userRepo.findByEmail(cancelRequest.getEmail()).orElseThrow(()-> new UserNotFoundException("User not Found"));
			if(passwordEncoder.matches(cancelRequest.getPassword(), foundUser.getPassword())){
				if(bookingRepository.existsByBookingId(cancelRequest.getBookingID())){
				Booking bookingToCancel = bookingRepository.findByBookingId(cancelRequest.getBookingID()).orElseThrow(()-> new BookingException("Booking with ID "+cancelRequest.getBookingID()+" not found"));
				if(bookingToCancel.getFlight().getStatus().equals(Status.STATUS_INACTIVE)){
					throw new BookingException("Booking cannot be cancelled after flight take off");
				}
				bookingToCancel.setStatus(Status.STATUS_INACTIVE);
				bookingRepository.save(bookingToCancel);
				return "Booking Cancelled Successfully";
		}else {
					return "Invalid Booking Id";
				}
			}else {
					return "Invalid Password";
			}
		}
		return "Booking Cancellation Failed";
	}

	private boolean isSeatAvailable(FlightSchedule flightSchedule, int seatNumber) {
		Set<Integer> bookedSeats = new HashSet<>();
		List<Booking> bookings = bookingRepository.findByFlight(flightSchedule);
		for (Booking booking : bookings) {
			for (PassengerInfo passengerInfo : booking.getPassengerInfoList()) {
				bookedSeats.add(passengerInfo.getSeatNumber());
			}
		}
		return !bookedSeats.contains(seatNumber);
	}




}