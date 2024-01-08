import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import "../CSS/table.css"

export default function Home() {
  const [flightsData, setFlightsData] = useState([]);
  const [user, setUser] = useState(JSON.parse(sessionStorage.getItem("user")));
  const [userDetails, setUserDetails] = useState(user?.accountInfo);
  const [formData, setFormData] = useState({
    source: '',
    destination: '',
    traveldate: ''
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };
  

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      
      const response = await fetch(
        `http://localhost:8080/api/flights/bySourceDestinationAndTravelDate?source=${formData.source}&destination=${formData.destination}&travelDate=${formData.traveldate}`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );

      if (!response.ok) {
        throw new Error('Failed to fetch data');
      }

      const data = await response.json();
      setFlightsData(data);
    } catch (error) {
      console.error('Error:', error.message);
    }
  };

  const nav = useNavigate();

  const handleSelect = (flightSchedule) =>{
    sessionStorage.setItem('selectedFlightSchedule', flightSchedule);
    if(userDetails != null){
      nav("/booking");
    }else{
      nav("/login")
    }
  }


  return (
    <>
     <div>
      <div>
      <form onSubmit={handleSubmit}>
            <fieldset>
              <legend>Search Flights</legend>
              <input
                type="text"
                name="source"
                id="source"
                placeholder="Source"
                value={formData.source}
                onChange={handleChange}
              /><br/>
              <input
                type="text"
                name="destination"
                id="destination"
                placeholder="Destination"
                value={formData.destination}
                onChange={handleChange}
              /><br/>
              <input
                type="date"
                name="traveldate"
                id="traveldate"
                value={formData.traveldate}
                onChange={handleChange}
              /><br/>
              <button type="submit">Enter</button>
            </fieldset>
          </form>
        <form>
          <fieldset>
            <legend>Search Flights</legend>
            <input type="text" name="pnr" id="pnr" placeholder='PNR Number'/>
            <button type='submit'>Enter</button>
          </fieldset>
        </form>
        
      </div>
      <div>
      <table className='flight-table'>
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
                <tr key={flight.scheduleCode} onClick={() => handleSelect(flight)}>
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
      </div> 
    </>
  )
}
