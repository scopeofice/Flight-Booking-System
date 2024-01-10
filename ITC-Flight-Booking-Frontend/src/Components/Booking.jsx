import axios from "axios";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import '../CSS/Booking.css'

export default function Booking() {
  const nav = useNavigate();
  const [user, setUser] = useState(JSON.parse(sessionStorage.getItem("user")));
  const [userDetails, setUserDetails] = useState(user?.accountInfo);
  const [numPassengers, setNumPassengers] = useState(1);
  const [acc,setAcc] = useState('');
  const [bookingDetails, setBookingDetails] = useState(
    JSON.parse(sessionStorage.getItem("selectedFlightSchedule"))
  );
  const [show, setShow] = useState(false);
  const [acknowledgment, setAcknowledgment] = useState({});
  const [showAcknowledgment, setShowAcknowledgment] = useState(false);
  const [showPassengerList, setShowPassengerList] = useState(true);
  const [passengerList, setPassengerList] = useState([
    {
      firstName: "",
      lastName: "",
      age: 0,
      seatNumber: 0,
      gender: "",
    },
  ]);
  

  const handleBooking = () => {
    sessionStorage.setItem("passengerList", JSON.stringify(passengerList));
    setShow(true);
    setShowPassengerList(false);
  };

  const handleNumPassengersChange = (e) => {
    const newNumPassengers = parseInt(e.target.value, 10) || 1;
    setNumPassengers(newNumPassengers);

    const newpassengerList = Array.from(
      { length: newNumPassengers },
      (_, index) => {
        return (
          passengerList[index] || {
            firstName: "",
            lastName: "",
            age: 0,
            seatNumber: 0,
            gender: "",
          }
        );
      }
    );

    setPassengerList(newpassengerList);
  };

  const handlePassengerInfoChange = (index, field, value) => {
    const newpassengerList = [...passengerList];
    newpassengerList[index][field] = value;
    setPassengerList(newpassengerList);
  };

  const handleBookingConfirmation = async () => {
    const bookingRequest = {
      scheduleCode: bookingDetails.scheduleCode,
      email: userDetails.email,
      passengerInfoList: passengerList,
      paymentInfo: {
        accountNo: acc,
        totalAmount: bookingDetails.fare * passengerList.length,
      },
    };
    alert("Flight Ticket has been send to your email. Your Booking will be updated soon")
    nav("/user");
    try {
      const response = await axios.post(
        "http://localhost:8080/api/user/bookFlightTicket",
        bookingRequest
      );
      // setAcknowledgment(response.data);
      // setShow(false);
      // setShowAcknowledgment(true);
      // setAcc("");
      // setPassengerList([]);
      // sessionStorage.removeItem("selectedFlightSchedule")
      // alert("Flight Booked")
    } catch (error) {
      console.log(error);
    }
  };

  const handleChange = (e) => {
    setAcc(e.target.value)
  };

  return (
    <>
    <div>
      <h2>Hy, {userDetails?.firstName}{" "}{userDetails?.lastName}</h2>
    </div>
    {showPassengerList && (
    <div className="form">
      <h4>Please enter the following details</h4>
      <label htmlFor="numPassengers">Number of Passengers:</label>
      <input
        type="number"
        id="numPassengers"
        min="1"
        max="5"
        value={numPassengers}
        onChange={handleNumPassengersChange}
      />

      {passengerList.map((passenger, index) => (
        <div key={index} className="passenger-details">
          <h2>Passenger {index + 1}</h2>
          <div className="form-group">
            <label>First Name:</label>
            <input
              type="text"
              value={passenger.firstName}
              onChange={(e) =>
                handlePassengerInfoChange(index, "firstName", e.target.value)
              }
            />
          </div>
          <div className="form-group">
            <label>Last Name:</label>
            <input
              type="text"
              value={passenger.lastName}
              onChange={(e) =>
                handlePassengerInfoChange(index, "lastName", e.target.value)
              }
            />
          </div>
          <div className="form-group">
            <label>Age:</label>
            <input
              type="number"
              max="100"
              min="0"
              value={passenger.age}
              onChange={(e) =>
                handlePassengerInfoChange(
                  index,
                  "age",
                  parseInt(e.target.value, 10)
                )
              }
            />
          </div>
          <div className="form-group">
            <label>Gender:</label>
            <select
              name="gender"
              id={`gender-${index}`}
              value={passenger.gender}
              onChange={(e) =>
                handlePassengerInfoChange(index, "gender", e.target.value)
              }
            >
              <option value="">Select Gender</option>
              <option value="male">Male</option>
              <option value="female">Female</option>
            </select>
          </div>
          <div className="form-group">
            <label>Seat Number:</label>
            <select
              value={passenger.seatNumber}
              onChange={(e) =>
                handlePassengerInfoChange(
                  index,
                  "seatNumber",
                  parseInt(e.target.value, 10)
                )
              }
            >
              <option value="">0</option>
              {bookingDetails.availableSeats.map((seat, seatIndex) => (
                <option key={seatIndex} value={seat}>
                  {seat}
                </option>
              ))}
            </select>
          </div>
        </div>
      ))}
          <button type="button" onClick={handleBooking}>
            Book
          </button>
        </div>
      )}
      <div>
        {true && (
          <div className="booking-details">
            <h2>Booking Details</h2>
            <p>Source: {bookingDetails.source}</p>
            <p>Destination: {bookingDetails.destination}</p>
            <p>Date: {bookingDetails.travelDate}</p>
            <p>Departure: {bookingDetails.pickupTime}</p>
            <p>Arrival: {bookingDetails.arrivalTime}</p>
            <p>Flight: {bookingDetails.flightName}</p>
            <p>
              <strong>
                Total Amount: Rs.{bookingDetails.fare * passengerList.length}
              </strong>
            </p>

            <h2>Passenger List</h2>
            <ul style={{ listStyleType: "none", padding: 0 }}>
              {passengerList.map((passenger, index) => (
                <li key={index}>
                  <div>
                    {index + 1}. Name: {passenger.firstName}{" "}
                    {passenger.lastName} <br /> Age: {passenger.age}, Gender:{" "}
                    {passenger.gender}
                  </div>
                  <br />
                </li>
              ))}
            </ul>
            <form className="form">
            <h2>Payment</h2>
              <input
                type="text"
                name="accountno"
                id="accountno"
                placeholder="Account Number"
                onChange={handleChange}
              />
              <input
                type="text"
                name="amount"
                id="amount"
                disabled
                placeholder={bookingDetails.fare * passengerList.length}
              />

            <button onClick={handleBookingConfirmation}>Book Flight</button>
            </form>
          </div>
        )}
      </div>
    </>
  );
}
