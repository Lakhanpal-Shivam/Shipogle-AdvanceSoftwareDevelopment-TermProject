import React from "react";
import { Link, useLocation } from "react-router-dom";
import Header from "../components/Header";
import shipogleLogo from "../assets/shipogleLogo.png";
export default function RegSuccessful() {
  const loc = useLocation();
  return (
    <div className="regSuccessPage">
      <Header title="S H I P O G L E" />
      <center>
        <img alt="logo" src={shipogleLogo} width="200px" height="200px"></img>
      </center>
      <div className="regSuccessBox">
        <h1>Registration successful!</h1>
        <p>
          A verification email has been sent. Please check your email and click
          on the link provided.
        </p>
        <Link to="/login">Once verified, click here to login.</Link>
      </div>
    </div>
  );
}
