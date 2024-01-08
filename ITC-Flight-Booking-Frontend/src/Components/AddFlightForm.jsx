// AddFlightForm.js
import React, { useState } from 'react';
import axios from 'axios';

const AddFlightForm = ({ onAddSuccess, onCancel }) => {
  // State for form fields
  const [formData, setFormData] = useState({
    // Add necessary form fields
  });

  const handleInputChange = (e) => {
    // Update form data when input changes
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleAddFlight = () => {
    // Logic to add new flight schedule
    axios.post('/api/flights/addFlightSchedule', formData)
      .then((response) => {
        onAddSuccess(response.data);
      })
      .catch((error) => console.error('Error adding flight schedule:', error));
  };

  return (
    <div>
      {/* Form fields go here */}
      {/* Use handleInputChange to update form data on input change */}
      <button onClick={handleAddFlight}>Add Flight Schedule</button>
      <button onClick={onCancel}>Cancel</button>
    </div>
  );
};

export default AddFlightForm;
