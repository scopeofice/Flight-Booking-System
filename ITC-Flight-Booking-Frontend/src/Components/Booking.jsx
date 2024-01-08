import React, { useState } from 'react';

export default function Booking() {
  const [numPassengers, setNumPassengers] = useState(1);
  const [bookingDetails,setBookingDetails] = useState(sessionStorage.getItem('selectedFlightSchedule'));
  const [show, setShow] = useState(false);
  const [passengerInfoList, setPassengerInfoList] = useState([
    {
      firstName: '',
      lastName: '',
      age: 0,
      seatNumber: 0,
      gender: '',
    },
  ]);

  const handleBooking = ()=>{
    
    sessionStorage.setItem("passengerInfoList",passengerInfoList);
    setShow(true);
  }

  const handleNumPassengersChange = (e) => {
    const newNumPassengers = parseInt(e.target.value, 10) || 1;
    setNumPassengers(newNumPassengers);

    const newPassengerInfoList = Array.from({ length: newNumPassengers }, (_, index) => {
      return passengerInfoList[index] || {
        firstName: '',
        lastName: '',
        age: 0,
        seatNumber: 0,
        gender: '',
      };
    });

    setPassengerInfoList(newPassengerInfoList);
  };

  const handlePassengerInfoChange = (index, field, value) => {
    const newPassengerInfoList = [...passengerInfoList];
    newPassengerInfoList[index][field] = value;
    setPassengerInfoList(newPassengerInfoList);
  };

  return (
    <>
    <div>
      <label htmlFor="numPassengers">Number of Passengers:</label>
      <input
        type="number"
        id="numPassengers"
        min="1"
        value={numPassengers}
        onChange={handleNumPassengersChange}
      />

      {passengerInfoList.map((passenger, index) => (
        <div key={index}>
          <h2>Passenger {index + 1}</h2>
          <label>
            First Name:
            <input
              type="text"
              value={passenger.firstName}
              onChange={(e) => handlePassengerInfoChange(index, 'firstName', e.target.value)}
            />
          </label><br />
          <label>
            Last Name:
            <input
              type="text"
              value={passenger.lastName}
              onChange={(e) => handlePassengerInfoChange(index, 'lastName', e.target.value)}
            />
          </label><br />
          <label>
            Age:
            <input
              type="number"
              value={passenger.age}
              onChange={(e) => handlePassengerInfoChange(index, 'age', parseInt(e.target.value, 10))}
            />
          </label><br />
          <label>
            Gender:
            <select
              name="gender"
              id={`gender-${index}`}
              value={passenger.gender}
              onChange={(e) => handlePassengerInfoChange(index, 'gender', e.target.value)}
            >
              <option value="">Select Gender</option>
              <option value="male">Male</option>
              <option value="female">Female</option>
            </select>
          </label>
        </div>
      ))}
      <button type='button' onClick={handleBooking}>Book</button>
    </div>
    <div>
        {show && 
            <div>

            </div>
        }
    </div>
    </>

  );
}
