import React, { useState, useEffect } from "react";
import axios from "axios";

const FlighScheduletTable = () => {
  const [flightSchedule, setFlightSchedule] = useState([]);
  const [show, setShow] = useState(false);
  const [del,setDel] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [selectedSchedule, setSelectedSchedule] = useState({});
  const [newSchedule, setNewSchedule] = useState({
    source: "",
    destination: "",
    travelDate: "",
    pickupTime: "",
    arrivalTime: "",
    flightName: "",
  });

  const handleChange = (e) => {
    setNewSchedule({
      ...newSchedule,
      [e.target.name]: e.target.value,
    });
  };

  

  const handleNewSchudule = async () => {
    try {
      const resp = await axios.post(
        "http://localhost:8080/api/flights/addFlightSchedule",
        newSchedule
      );
      console.log(resp.data);
      setNewSchedule({
        source: "",
        destination: "",
        travelDate: "",
        pickupTime: "",
        arrivalTime: "",
        flightName: "",
      });
      setShow(true);
    } catch (error) {
      console.log(error);
    }
  };

  const handleEditChange = (e) => {
    setSelectedSchedule({
      ...selectedSchedule,
      [e.target.name]: e.target.value,
    });
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8080/api/flights/schedule/all"
        );
        console.log(response.data);
        setFlightSchedule(response.data);
      } catch (error) {
        console.error("Error fetching flights:", error);
      }
    };
    setDel(false)
    setShow(false)
    fetchData();
  }, [show,del]);

  const handleShowForm = () => {
    setShow(true);
  };

  const handleDelete = async (scheduleCode) => {
    console.log(scheduleCode)
    try {
      const resp = await axios.delete(
        `http://localhost:8080/api/flights/deleteFlightSchedule/${scheduleCode}`
      );
      console.log(resp.data);
      setDel(true)
    } catch (error) {
      console.log(error.response.data);
    }
  };

  const handleEdit = async () => {
    try {
      const resp = await axios.put(
        "http://localhost:8080/api/flights/updateFlightSchedule",
        selectedSchedule
      );
      console.log(resp.data);
      setShow(true);
      setEditMode(false)
    } catch (error) {
      console.log(error.resp.data);
    }
  };

  const handleEditClick = (flight) => {
    setEditMode(true);
    setSelectedSchedule(flight);
    setShow(true);
  };

  const handleClose = () => {
    setEditMode(false);
    setShow(false);
  };

  return (
    <>
    <div style={{ display: "flex", height:"60vh",marginBottom:"20px" }}>
      {/* <button onClick={handleShowForm}>Add Flight</button> */}
      {show && (
        <div>
          <form>
            <fieldset>
              <legend>{editMode ? "Edit Flight" : "Add New Schedule"}</legend>
              <input
                type="text"
                name="source"
                id="source"
                placeholder="Source"
                value={editMode ? selectedSchedule.source : newSchedule.source}
                onChange={editMode ? handleEditChange : handleChange}
              />
              <input
                type="text"
                name="destination"
                id="destination"
                placeholder="Destination"
                value={
                  editMode
                    ? selectedSchedule.destination
                    : newSchedule.destination
                }
                onChange={editMode ? handleEditChange : handleChange}
              />
              <input
                type="date"
                name="travelDate"
                id="travelDate"
                placeholder="Travel Date"
                value={
                  editMode
                    ? selectedSchedule.travelDate
                    : newSchedule.travelDate
                }
                onChange={editMode ? handleEditChange : handleChange}
              />
              <input
                type="text"
                name="pickupTime"
                id="pickupTime"
                placeholder="Pickup Time"
                value={
                  editMode
                    ? selectedSchedule.pickupTime
                    : newSchedule.pickupTime
                }
                onChange={editMode ? handleEditChange : handleChange}
              />
              <input
                type="text"
                name="arrivalTime"
                id="arrivalTime"
                placeholder="Arrival Time"
                value={
                  editMode
                    ? selectedSchedule.arrivalTime
                    : newSchedule.arrivalTime
                }
                onChange={editMode ? handleEditChange : handleChange}
              />
              <input
                type="text"
                name="flightName"
                id="flightName"
                placeholder="Flight Name"
                value={
                  editMode
                    ? selectedSchedule.flightName
                    : newSchedule.flightName
                }
                onChange={editMode ? handleEditChange : handleChange}
              />
              <select
                type="text"
                name="status"
                id="status"
                value={editMode ? selectedSchedule.status : newSchedule.status}
                onChange={editMode ? handleEditChange : handleChange}
              >
                <option value="">---Status---</option>
                <option value="active">Active</option>
                <option value="inactive">Inactive</option>
              </select>
              <input
                type="text"
                name="availableSeats"
                id="availableSeats"
                placeholder="Available Seats"
                value={
                  editMode
                    ? selectedSchedule.availableSeats
                    : newSchedule.availableSeats
                }
                onChange={editMode ? handleEditChange : handleChange}
              />
              <button
                type="button"
                onClick={editMode ? handleEdit : handleNewSchudule}
              >
                {editMode ? "Update" : "Add"}
              </button>
              <button type="button" onClick={handleClose}>
                Close
              </button>
            </fieldset>
          </form>
        </div>
      )}
      <table className="flight-table">
        <thead>
          <tr>
            <th>Code</th>
            <th>Source</th>
            <th>Destination</th>
            <th>TravelDate</th>
            <th>PickupTime</th>
            <th>ArrivalTime</th>
            <th>FlightName</th>
            <th>Fare</th>
            <th>Status</th>
            <th>AvailableSeats</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {flightSchedule?.map((flight) => (
            <tr key={flight.flightName}>
              <td>{flight.scheduleCode}</td>
              <td>{flight.source}</td>
              <td>{flight.destination}</td>
              <td>{flight.travelDate}</td>
              <td>{flight.pickupTime}</td>
              <td>{flight.arrivalTime}</td>
              <td>{flight.flightName}</td>
              <td>{"Rs."}{flight.fare}</td>
              <td>{flight.status}</td>
              <td>{flight.availableSeat}</td>
              <td>
                <button onClick={() => handleEditClick(flight)}>Edit</button>
                <button onClick={() => handleDelete(flight.scheduleCode)}>
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      </div>
    </>
  );
};

export default FlighScheduletTable;
