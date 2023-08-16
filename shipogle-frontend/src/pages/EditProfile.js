import React, { useState } from "react";
import Header from "../components/Header";
import { useForm, Controller } from "react-hook-form";
import TextField from "@mui/material/TextField";
import axios from "../utils/MyAxios";
import Constants from "../Constants";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";

export default function EditProfile() {
  const [dobvalue, setDOBValue] = React.useState(null);
  const [status, setStatus] = useState(false);
  //govt ID
  const govIDValidation = () => {
    //add condition for verification here
    setStatus(true);
  };

  // State to hold user profile information
  const [profileInfo, setProfileInfo] = useState({
    first_name: "",
    last_name: "",
    email: "",
    phone: "",
    address: "",
    city: "",
    province: "",
    postal_code: "",
    country: "",
  });

  React.useEffect(() => {
    // Get user info from token
    axios.get(Constants.API_USER_INFO_FROM_TOKEN).then((response) => {
      const user = response.data;
      setProfileInfo(user);
    });
  }, []);

  // Function to handle changes to the input fields
  // Function to handle changes to the input fields
  const handleInputChange = (event, fieldName) => {
    const value = event.target.value;

    setProfileInfo((prevState) => ({
      ...prevState,
      [fieldName]: value,
    }));

    window.localStorage.setItem(
      "user_name",
      profileInfo.first_name + " " + profileInfo.last_name
    );
  };

  // Function to handle form submission
  const onFormSubmit = (event) => {
    event.preventDefault(); // prevent default form submission behavior

    axios
      .put(Constants.API_USER, profileInfo) // send updated user profile data to the API endpoint
      .then((response) => {})
      .catch((error) => {
        console.error(error);
      });
  };

  return (
    <div className="editprofile">
      <Header title="Edit profile details" />
      <form className="form" onSubmit={onFormSubmit}>
        <div className="subheading">Name</div>
        <div className="name">
          <TextField
            label="First Name"
            value={profileInfo.first_name}
            size="small"
            onChange={(event) => handleInputChange(event, "first_name")}
          />
          <TextField
            label="Last Name"
            value={profileInfo.last_name}
            size="small"
            onChange={(event) => handleInputChange(event, "last_name")}
          />
        </div>
        <div className="subheading">Contact Information</div>
        <div className="contact">
          <TextField
            label="Email"
            type="email"
            value={profileInfo.email}
            size="small"
            onChange={handleInputChange}
            disabled={true}
          />
          <TextField
            label="Phone Number"
            value={profileInfo.phone}
            size="small"
            onChange={(event) => handleInputChange(event, "phone")}
          />
        </div>
        <div className="subheading">Address Information</div>
        <div className="address">
          <TextField
            label="Address"
            type="text"
            value={profileInfo.address}
            size="small"
            onChange={(event) => handleInputChange(event, "address")}
          />
          <TextField
            label="City"
            type="text"
            value={profileInfo.city}
            size="small"
            onChange={(event) => handleInputChange(event, "city")}
          />
          <TextField
            label="Province"
            type="text"
            value={profileInfo.province}
            size="small"
            onChange={(event) => handleInputChange(event, "province")}
          />
          <TextField
            label="Postal Code"
            type="text"
            value={profileInfo.postal_code}
            size="small"
            onChange={(event) => handleInputChange(event, "postal_code")}
          />
          <TextField
            label="Country"
            type="text"
            value={profileInfo.country}
            size="small"
            onChange={(event) => handleInputChange(event, "country")}
          />
        </div>
        <div>
          <label>Upload profile picture: </label>
          <input type="file" />
        </div>
        <input className="btn" type="submit" />
      </form>
    </div>
  );
}

// FormSubmit:   https://blog.logrocket.com/react-hook-form-complete-guide/
