import React from "react";
import Header from "../components/Header";
import shipogleLogo from "../assets/shipogleLogo.png";
import Constants from "../Constants";
import axios from "axios";
import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";

export default function ResetPwd() {
  const [password, setPassword] = React.useState("");

  return (
    <div className="regSuccessPage">
      <Header title="S H I P O G L E" />
      <center>
        <img alt="logo" src={shipogleLogo} width="200px" height="200px"></img>
      </center>
      <div className="regSuccessBox">
        <div>
          <h1>Forgot Password</h1>

          <p>Please enter your Password.</p>
          <input
            className="forgotPwdMailField"
            id="email"
            name="email"
            placeholder="john@example.com"
            type="email"
            onChange={(e) => setPassword(e.target.value)}
          />

          <br></br>
          <p>Confirm Password.</p>
          <input
            className="forgotPwdMailField"
            id="email"
            name="email"
            placeholder="john@example.com"
            type="email"
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <br></br>
        <div>
          <button className="btn" type="submit">
            Reset password
          </button>
        </div>
      </div>
    </div>
  );
}
