package com.airticket.itc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDTO {
    private String recipient;
    private String messageBody;
    private String subject;

    public String constructBookingAcknowledgementEmail(FlightBookingAcknowledgement bookingAcknowledgement) {
        StringBuilder messageBody = new StringBuilder();

        messageBody.append("Dear Passenger,\n\n");
        messageBody.append("Thank you for booking your flight with us. Below are the details of your booking:\n\n");

        messageBody.append("Booking Status: ").append(bookingAcknowledgement.getStatus()).append("\n");
        messageBody.append("Total Fare: Rs.").append(bookingAcknowledgement.getTotalFare()).append("\n");
        messageBody.append("PNR Number: ").append(bookingAcknowledgement.getPnrNo()).append("\n\n");

        messageBody.append("Passenger Details:\n");
        List<PassengerDTO> passengerInfoList = bookingAcknowledgement.getPassengerInfo();
        for (PassengerDTO passenger : passengerInfoList) {
            messageBody.append("  - Name: ").append(passenger.getFirstName()).append(" ").append(passenger.getLastName()).append("\n");
            messageBody.append("    Age: ").append(passenger.getAge()).append(", Gender: ").append(passenger.getGender()).append("\n");
            messageBody.append("    Seat Number: ").append(passenger.getSeatNumber()).append("\n\n");
        }

        messageBody.append("Thank you for choosing our airline. Have a safe and pleasant journey!\n\n");
        messageBody.append("Best regards,\nThe Airline Team");

        return messageBody.toString();
    }
}
