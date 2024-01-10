import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../CSS/table.css";
import axios from "axios";

export default function Home() {
  const [flightsData, setFlightsData] = useState([]);
  const [user, setUser] = useState(JSON.parse(sessionStorage.getItem("user")) || null);
  const [showPNRInfo, setShowPNRInfo] = useState(false);
  const [showSchedule, setShowSchedule] = useState(false);
  const [pnrResponse, setPnrResponse] = useState({});
  const [pnrNumber, setPnrNumber] = useState("");
  const [formData, setFormData] = useState({
    source: "",
    destination: "",
    traveldate: "",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setShowPNRInfo(false);
    try {
      const response = await fetch(
        `http://localhost:8080/api/flights/bySourceDestinationAndTravelDate?source=${formData.source}&destination=${formData.destination}&travelDate=${formData.traveldate}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!response.ok) {
        throw new Error("Failed to fetch data");
      }

      const data = await response.json();
      setFlightsData(data);
      setShowSchedule(true);
      setFormData({
        source: "",
        destination: "",
        traveldate: "",
      });
    } catch (error) {
      console.error("Error:", error.message);
    }
  };

  const nav = useNavigate();

  const handleSelect = (flightSchedule) => {
    sessionStorage.setItem(
      "selectedFlightSchedule",
      JSON.stringify(flightSchedule)
    );

    if (user === null) {
      nav("/login");
    } else {
      nav("/booking");
    }
  };

  const handelChange = (e) => {
    setPnrNumber(e.target.value);
  };

  const handlePNRInfo = async () => {
    try {
      const resp = await axios.get(
        `http://localhost:8080/api/user/bookingDetailsByPNR/${pnrNumber}`
      );
      console.log(resp.data);
      setPnrResponse(resp.data);
      setShowPNRInfo(true);
      setShowSchedule(false);
      setPnrNumber("");
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <div className="parent">
        <div>
          <form onSubmit={handleSubmit} className="form">
            <h2>Search Flights</h2>
            <input
              type="text"
              name="source"
              id="source"
              placeholder="Source"
              value={formData.source}
              onChange={handleChange}
            />
            <br />
            <input
              type="text"
              name="destination"
              id="destination"
              placeholder="Destination"
              value={formData.destination}
              onChange={handleChange}
            />
            <br />
            <input
              type="date"
              name="traveldate"
              id="traveldate"
              value={formData.traveldate}
              onChange={handleChange}
            />
            <br />
            <button type="submit">Enter</button>
          </form>
          <br />
          <form className="form">
            <h2>Search Booking</h2>
            <input
              type="text"
              name="pnr"
              id="pnr"
              placeholder="PNR Number"
              value={pnrNumber}
              onChange={handelChange}
            />
            <button type="button" onClick={handlePNRInfo}>
              Enter
            </button>
          </form>
        </div>
        {showPNRInfo && (
  <div className="pnr-details-card">
    <div className="booking-section">
      <h2>Booking Details</h2>
      <p>
        <strong>Booking Id:</strong> {pnrResponse.bookingId.substring(0, 8)}
      </p>
      <p>
        <strong>Status:</strong> {pnrResponse.status}
      </p>
      <p>
        <strong>Date:</strong>{" "}
        {new Date(pnrResponse.bookingDate).toLocaleDateString("en-GB")}
      </p>
      <p>
        <strong>Time:</strong> {pnrResponse.bookingTime.substring(0, 5)}
      </p>
    </div>
    <div className="booking-section">
      <h2>Payment Details</h2>
      <p>
        <strong>Payment Id:</strong>{" "}
        {pnrResponse.paymentInfo.paymentId.substring(0, 8)}
      </p>
      <p>
        <strong>Account No:</strong> {pnrResponse.paymentInfo.accountNo}
      </p>
      <p>
        <strong>Total Amount:</strong> {pnrResponse.paymentInfo.totalAmount}
      </p>
    </div>
    <div className="booking-section">
      <h2>Flight Schedule</h2>
      <p>
        <strong>Source:</strong> {pnrResponse.schedule.source}
      </p>
      <p>
        <strong>Destination:</strong> {pnrResponse.schedule.destination}
      </p>
      <p>
        <strong>Travel Date:</strong>{" "}
        {new Date(pnrResponse.schedule.travelDate).toLocaleDateString(
          "en-GB"
        )}
      </p>
      <p>
        <strong>Pickup Time:</strong> {pnrResponse.schedule.pickupTime}
      </p>
      <p>
        <strong>Arrival Time:</strong> {pnrResponse.schedule.arrivalTime}
      </p>
      <p>
        <strong>Flight Name:</strong> {pnrResponse.schedule.flightName}
      </p>
    </div>
    <div className="booking-section">
      <h2>Passenger List</h2>
      <ul className="passenger-list">
        {pnrResponse.passengerInfoList.map((passenger, index) => (
          <li key={index}>
            <div>
            <strong>{index + 1}. Name:</strong> {passenger.firstName} {passenger.lastName} <br />
    <strong>Age:</strong> {passenger.age}, <br />
    <strong>Gender:</strong> {passenger.gender.toUpperCase()}, <br />
    <strong>Seat no:</strong> {passenger.seatNumber}
            </div>
          </li>
        ))}
      </ul>
    </div>
  </div>
)}

        {showSchedule && (
          <div className="flight-schedule">
            <table className="flight-table">
              <thead>
                <tr>
                  <th>Schedule Code</th>
                  <th>Source</th>
                  <th>Destination</th>
                  <th>Travel Date</th>
                  <th>Pickup Time</th>
                  <th>Arrival Time</th>
                  <th>Flight Name</th>
                  <th>Fare</th>
                  <th>Status</th>
                  <th>Available Seat</th>
                </tr>
              </thead>
              <tbody>
                {flightsData.map((flight) => (
                  <tr
                    key={flight.scheduleCode}
                    onClick={() => handleSelect(flight)}
                  >
                    <td>{flight.scheduleCode}</td>
                    <td>{flight.source}</td>
                    <td>{flight.destination}</td>
                    <td>{flight.travelDate}</td>
                    <td>{flight.pickupTime}</td>
                    <td>{flight.arrivalTime}</td>
                    <td>{flight.flightName}</td>
                    <td>{flight.fare}</td>
                    <td>{flight.status}</td>
                    <td>{flight.availableSeat}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </>
  );
}
