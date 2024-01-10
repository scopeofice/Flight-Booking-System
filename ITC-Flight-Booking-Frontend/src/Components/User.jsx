import React, { useState, useEffect } from "react";
import axios from "axios";
import "../CSS/User.css";

export default function User() {
  const [cancelPassword, setCancelPassword] = useState("");
  const [showCancelForm, setShowCancelForm] = useState(false);

  

  const handleCancelBooking = async (booking) => {
    setSelectedBooking(booking);
    setShowCancelForm(true);
    setShowSelectedBooking(true);
  };
  const handlePassword = (e) => {
    setCancelPassword(e.target.value);
  };

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
      setCancelPassword("");
      setSelectedBooking(null);
    } catch (error) {
      console.log("Error cancelling booking:", error);

    }
  };
  useEffect(() => {
    cancelBooking();
  }, []);

  const [user, setUser] = useState(JSON.parse(sessionStorage.getItem("user")));
  const [userDetails, setUserDetails] = useState(user.accountInfo);
  const [bookings, setBookings] = useState([]);
  const [selectedBooking, setSelectedBooking] = useState(null);
  const [showSelectedBooking, setShowSelectedBooking] = useState(false);

  useEffect(() => {
    const fetchUserBookings = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/user/userBooking/${userDetails.email}`
        );
        sessionStorage.removeItem("selectedFlightSchedule")
        sessionStorage.removeItem("passengerList")
        setBookings(response.data);
      } catch (error) {
        console.error("Error fetching user bookings:", error);
      }
    };

    fetchUserBookings();
  }, [userDetails.email, showCancelForm]);

  const handleBookingClick = (booking) => {
    setSelectedBooking(booking);
    setShowSelectedBooking(true);
    showCancelForm(false);
  };
  const [currentPage, setCurrentPage] = useState(1);
  const bookingsPerPage = 4;

  const sortedBookings = bookings.sort((a, b) => {
    const dateA = new Date(`${a.bookingDate} ${a.bookingTime}`);
    const dateB = new Date(`${b.bookingDate} ${b.bookingTime}`);
    return dateB - dateA;
  });

  const indexOfLastBooking = currentPage * bookingsPerPage;
  const indexOfFirstBooking = indexOfLastBooking - bookingsPerPage;
  const currentBookings = sortedBookings.slice(
    indexOfFirstBooking,
    indexOfLastBooking
  );

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  const handleNextPage = () => {
    if (currentPage < Math.ceil(sortedBookings.length / bookingsPerPage)) {
      setCurrentPage(currentPage + 1);
    }
  };

  const handlePrevPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
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
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
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

      <div className="booking-parent">
        <div className="booking-list">
          <h2>User Bookings</h2>
          <ul className="booking-items">
            {currentBookings.map((booking, index) => (
              <li className="booking-items-list" key={index} style={{ textDecoration: "none" }}>
                <div
                  onClick={() => handleBookingClick(booking)}
                  style={{ display: "flex", justifyContent:"space-between",width:"100%" }}
                >
                  <div
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      marginRight: "10px",
                    }}
                  >
                    <div>
                      {index + 1}
                      {"."}
                    </div>
                  </div>
                  <div style={{ display: "flex", flexDirection: "column" }}>
                    <div>{booking.schedule.source}</div>
                    <div>{booking.schedule.pickupTime}</div>
                  </div>
                  <div style={{ color: "grey", margin: "10px" }}>
                    ------
                    {calculateDuration(
                      booking.schedule.pickupTime,
                      booking.schedule.arrivalTime
                    )}
                    ------
                  </div>
                  <div style={{ display: "flex", flexDirection: "column" }}>
                    <div>{booking.schedule.destination}</div>
                    <div>{booking.schedule.arrivalTime}</div>
                  </div>
                  <div
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      marginLeft: "10px",
                    }}
                  >
                    <div>{formatDate(booking.bookingDate)}</div>
                    <div>{booking.bookingTime.substring(0, 5)}</div>
                  </div>
                  <button
                    className="cancel-btn"
                    onClick={() => handleCancelBooking(booking)}
                    disabled={booking.schedule.status === 'STATUS_INACTIVE' || booking.status === 'STATUS_INACTIVE'}
                  >
                    Cancel
                  </button>
                </div>
                <br />
              </li>
            ))}
          </ul>
          <div className="pagination">
  <button onClick={handlePrevPage} disabled={currentPage === 1}>
    {"<"}
  </button>
  {Array.from(
    { length: Math.ceil(sortedBookings.length / bookingsPerPage) },
    (_, index) => (
      <button
        key={index}
        className={currentPage === index + 1 ? "active" : ""}
        style={{ width: "50px", margin: "2px" }}
        onClick={() => paginate(index + 1)}
      >
        {index + 1}
      </button>
    )
  )}
  <button
    onClick={handleNextPage}
    disabled={
      currentPage === Math.ceil(sortedBookings.length / bookingsPerPage)
    }
  >
    {">"}
  </button>
</div>
        </div>

        <div style={{ display: "flex",flexDirection:"column", width:"50%" }}>
          <div>
            {showCancelForm && (
              <>
              <div>
                 <div
                  style={{
                    display: "flex",
                    marginLeft: "10px",
                    marginBottom: "5px",
                    position: "absolute",
                    
                  }}
                >
                  <button
                    style={{ width: "40px" }}
                    onClick={() => {setShowCancelForm(false)}}
                  >
                    X
                  </button>
                </div>
                <form className="form" style={{ marginBottom: "20px" }}>
                  <h2>Cancel Booking</h2>
                  <p>Please enter your Password to cancel booking.</p>
                  <input
                    type="password"
                    name="password"
                    id="password"
                    placeholder="Password"
                    onChange={handlePassword}
                  />
                  <button type="button" onClick={cancelBooking}>
                    Enter
                  </button>
                </form>
              </div>
              </>
            )}
          </div>
          <div>
            {showSelectedBooking && (
              <>
                <div
                  style={{
                    display: "flex",
                    marginLeft: "10px",
                    marginBottom: "5px",
                    position: "absolute",
                  }}
                >
                  <button
                    style={{ width: "40px" }}
                    onClick={() => {setShowSelectedBooking(false)}}
                  >
                    X
                  </button>
                </div>
                <div className="flight-wrapper">
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
                      <strong>Booking Date:</strong>{" "}
                      {selectedBooking.bookingDate}
                    </p>
                    <p>
                      <strong>Booking Time:</strong>{" "}
                      {selectedBooking.bookingTime}
                    </p>
                    <p>
                      <strong>Source:</strong>{selectedBooking.schedule.source}
                    </p>
                    <p>
                      <strong>Destination:</strong>{" "}
                      {selectedBooking.schedule.destination}
                    </p>
                    <p>
                      <strong>Status:</strong>{" "}
                      {selectedBooking.schedule.status.substring(7)}
                    </p>
                    <p>
                      <strong>Flight:</strong>{" "}
                      {selectedBooking.schedule.flightName}
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

                  <div className="passenger-details">
                    <h2>Passenger Details</h2>
                    {selectedBooking.passengerInfoList.map(
                      (passenger, index) => (
                        <div key={index}>
                          <h3>Passenger {index + 1}</h3>
                          <strong>Name:</strong> {passenger.firstName}{" "}
                          {passenger.lastName}
                          <br />
                          <strong>Seat Number:</strong> {passenger.seatNumber}
                          <br />
                          <strong>Gender:</strong> {passenger.gender}
                          {",  "}
                          <strong>Age:</strong> {passenger.age}
                          <br />
                        </div>
                      )
                    )}
                  </div>

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
                </div>
              </>
            )}
          </div>
        </div>
      </div>
    </>
  );
}
