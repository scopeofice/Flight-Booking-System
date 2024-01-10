import React, { useState, useEffect } from "react";
import axios from "axios";
import "../CSS/User.css";

export default function User() {
  const [cancelConfirmation, setCancelConfirmation] = useState(false);
  const [cancelPassword, setCancelPassword] = useState("");

  const handleCancelBooking = async () => {
    const userPassword = prompt("Enter your password to confirm cancellation:");

    if (userPassword) {
      setCancelPassword(userPassword);
      setCancelConfirmation(true);
    }
  };

  useEffect(() => {
    const cancelBooking = async () => {
      try {
        const response = await axios.put(
          "http://localhost:8080/api/user/cancelBooking",
          {
            email: userDetails.email,
            password: cancelPassword,
            bookingID: selectedBooking.bookingId,
          }
        );

        console.log("Booking cancellation response:", response.data);
        setSelectedBooking.status(INACTIVE);
        setCancelConfirmation(false);
        setCancelPassword("");
        setSelectedBooking(null);
      } catch (error) {
        console.error("Error cancelling booking:", error);
      }
    };

    if (cancelConfirmation) {
      cancelBooking();
    }
  }, [cancelConfirmation]);

  const [user, setUser] = useState(JSON.parse(sessionStorage.getItem("user")));
  const [userDetails, setUserDetails] = useState(user.accountInfo);
  const [bookings, setBookings] = useState([]);
  const [selectedBooking, setSelectedBooking] = useState(null);

  useEffect(() => {
    const fetchUserBookings = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/user/userBooking/${userDetails.email}`
        );
        setBookings(response.data);
      } catch (error) {
        console.error("Error fetching user bookings:", error);
      }
    };

    fetchUserBookings();
  }, [userDetails.email]);

  const handleBookingClick = (booking) => {
    setSelectedBooking(booking);
  };

  function calculateDuration(pickupTime, arrivalTime) {
    const pickupDateTime = new Date(`1970-01-01T${pickupTime}`);
    const arrivalDateTime = new Date(`1970-01-01T${arrivalTime}`);

    const durationInMinutes = (arrivalDateTime - pickupDateTime) / (1000 * 60);

    const hours = Math.floor(durationInMinutes / 60);
    const minutes = durationInMinutes % 60;

    return `${hours}h ${minutes}min`;
  }

  function formatDate(dateString) {
    const date = new Date(dateString);
    const day = date.getDate().toString().padStart(2, "0");
    const month = (date.getMonth() + 1).toString().padStart(2, "0"); // Months are zero-based
    const year = date.getFullYear();

    return `${day}/${month}/${year}`;
  }

  return (
    <>
      <div className="user-info">
        <h1>Welcome</h1>
        <p>
          <strong>Name:</strong> {userDetails.firstName} {userDetails.lastName}
        </p>
        <p>
          <strong>Email:</strong> {userDetails.email}
        </p>
        <p>
          <strong>Phone Number:</strong> {userDetails.phoneNumber}
        </p>
      </div>



      <div className="booking-list">
        <h2>User Bookings</h2>
        <ul className="booking-items">
          {bookings.map((booking, index) => (
            <li key={index} style={{ textDecoration: "none" }}>
              <div
                onClick={() => handleBookingClick(booking)}
                style={{ display: "flex" }}
              >
                <div style={{ display: "flex", flexDirection: "column" }}>
                  <div>{booking.schedule.source}</div>
                  <div>{booking.schedule.pickupTime}</div>
                </div>
                ------
                {calculateDuration(
                  booking.schedule.pickupTime,
                  booking.schedule.arrivalTime
                )}
                ------
                <div style={{ display: "flex", flexDirection: "column" }}>
                  <div>{booking.schedule.destination}</div>
                  <div>{booking.schedule.arrivalTime}</div>
                </div>
              </div>
              <br />
            </li>
          ))}
        </ul>
        <br />
        </div>



        {selectedBooking && (
          <div >
            <div className="flight-details">
                  <h2>Flight Details</h2>
                <p>
                  <strong>Booking Id:</strong>{" "}
                  {selectedBooking.bookingId.substring(0, 8)}
                </p>
                <p>
                  <strong>Booking status:</strong>{" "}
                  {selectedBooking.status.substring(7)}
                </p>
                <p>
                  <strong>Booking Date:</strong> {selectedBooking.bookingDate}
                </p>
                <p>
                  <strong>Booking Time:</strong> {selectedBooking.bookingTime}
                </p>
                <p>
                  <strong>Source:</strong> {selectedBooking.schedule.source}
                </p>
                <p>
                  <strong>Destination:</strong>{" "}
                  {selectedBooking.schedule.destination}
                </p>
                <p>
                  <strong>Flight:</strong> {selectedBooking.schedule.flightName}
                </p>
                <p>
                  <strong>Travel Date:</strong>{" "}
                  {formatDate(selectedBooking.schedule.travelDate)}
                </p>
                <p>
                  <strong>Pickup Time:</strong>{" "}
                  {selectedBooking.schedule.pickupTime}
                </p>
                <p>
                  <strong>Arrival Time:</strong>{" "}
                  {selectedBooking.schedule.arrivalTime}
                </p>
                </div>
                  <h2>Passenger Details</h2>
                {selectedBooking.passengerInfoList.map((passenger, index) => (
                  <div key={index}>
                    <h3>Passenger {index + 1}</h3>
                    <p>
                      <strong>Name:</strong> {passenger.firstName}{" "}
                      {passenger.lastName}
                    </p>
                    <p>
                      <strong>Age:</strong> {passenger.age}
                    </p>
                    <p>
                      <strong>Seat Number:</strong> {passenger.seatNumber}
                    </p>
                    <p>
                      <strong>Gender:</strong> {passenger.gender}
                    </p>
                    <br />
                  </div>
                ))}
              <div className="payment-details">
                <legend>
                  <h2>Payment Details</h2>
                </legend>
                <p>
                  <strong>Payment Id:</strong>{" "}
                  {selectedBooking.paymentInfo.paymentId.substring(0, 8)}
                </p>
                <p>
                  <strong>Account No:</strong>{" "}
                  {selectedBooking.paymentInfo.accountNo}
                </p>
                <p>
                  <strong>Total Amount:</strong>{" "}
                  {selectedBooking.paymentInfo.totalAmount}
                </p>
              </div>
            

            <button onClick={() => setSelectedBooking(null)}>
              Close Details
            </button>
            <button onClick={handleCancelBooking}>Cancel Booking</button>
          </div>
        )}
      
    </>
  );
}
