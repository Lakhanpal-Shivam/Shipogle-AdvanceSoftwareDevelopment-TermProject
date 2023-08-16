import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../components/Header";
import RegRoleBox from "../components/RegRoleBox";
import senderImg from "../assets/sender.png";
import delivererImg from "../assets/deliverer.png";
import RegistrationSender from "./RegistrationForm";
export default function Registration() {
  //remove options, set single form;
  //dob verification
  let path = "/registration/form";

  let navigate = useNavigate();

  const navUser = (param) => {
    navigate(path, {
      state: { user_type: param },
    });
  };

  return (
    <div className="regPage">
      <Header title="Registration" info="Register with us!" />

      <div className="regRole">
        {/*
          <div className="one">
          <RegRoleBox
            role="Sender"
            roleimg={senderImg}
            roleinfo="I want to send my package from A to B before a due date!"
          />
          <center>
            <button className="btn reg" onClick={() => navUser("sender")}>
              Register
            </button>
          </center>
        </div>
          */}
        <RegistrationSender></RegistrationSender>
      </div>
    </div>
  );
}
