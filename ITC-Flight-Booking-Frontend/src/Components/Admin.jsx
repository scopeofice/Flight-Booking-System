import React, { useState, useEffect } from 'react';
import axios from 'axios';
import FlightTable from './FlightTable';
import AddFlightForm from './AddFlightForm';

export default function Admin() {
    const [flights, setFlights] = useState([]);
    const [showTable, setShowTable] = useState(true);
    const [showAddForm, setShowAddForm] = useState(false);
    const [user, setUser] = useState(JSON.parse(sessionStorage.getItem("user")));
  const [userDetails, setUserDetails] = useState(user.accountInfo);

  useEffect(() => {
    axios.get('http://localhost:8080/api/flights/all')
      .then((response) => setFlights(response.data))
      .catch((error) => console.error('Error fetching flights:', error));
  }, []);

  const handleEdit = () => {
    // Logic to show edit form
    // You may set state or handle modal visibility
  };

  const handleDelete = (flightName) => {
    // Logic to delete flight
    // Make API call to delete flight by flightName
  };

  const handleAddFlight = () => {
    setShowTable(false);
    setShowAddForm(true);
  };

  return (
    <>
    <div>
        <fieldset>
          <legend><h1>Welcome</h1></legend>
          <p>
            <strong>Name:</strong> {userDetails.firstName} {userDetails.lastName}
          </p>
          <p>
            <strong>Email:</strong> {userDetails.email}
          </p>
          <p>
            <strong>Phone Number:</strong> {userDetails.phoneNumber}
          </p>
        </fieldset>
      </div>
      <div>
      {showTable && (
        <FlightTable
          flights={flights}
          onEdit={handleEdit}
          onDelete={handleDelete}
        />
      )}

      {showAddForm && (
       <AddFlightForm/>
      )}

      <button onClick={handleAddFlight}>Add Flight</button>
    </div>
      
    </>
  )
}
