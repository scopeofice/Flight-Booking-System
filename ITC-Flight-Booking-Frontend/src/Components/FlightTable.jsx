// FlightTable.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const FlightTable = ({ flights, onEdit, onDelete }) => {
  const [selectedFlight, setSelectedFlight] = useState(null);

  const handleEdit = (flight) => {
    setSelectedFlight(flight);
    onEdit();
  };

  const handleDelete = (flightName) => {
    if (window.confirm('Are you sure you want to delete this flight?')) {
      onDelete(flightName);
    }
  };

  return (
    <table>
      <thead>
        <tr>
          <th>Flight Name</th>
          <th>Number of Seats</th>
          <th>Fare</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {flights.map((flight) => (
          <tr key={flight.flightName}>
            <td>{flight.flightName}</td>
            <td>{flight.numberOfSeats}</td>
            <td>{flight.fare}</td>
            <td>
              <button onClick={() => handleEdit(flight)}>Edit</button>
              <button onClick={() => handleDelete(flight.flightName)}>Delete</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default FlightTable;
