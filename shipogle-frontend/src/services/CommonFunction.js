import React, { Component } from "react";
import ReactDOM from "react-dom";
import AlertMessage from "../components/AlertMessage";

class CommonFunctions extends Component {
  static backgroundUrl = "";
  static googleMapObjectStatus = false;

  getDriverInitials(name) {
    if (!name) {
      return "NN";
    }

    const nameArray = name.split(" ");
    const fInitial = nameArray[0].charAt(0);
    const sInitial = nameArray.length > 1 ? nameArray[1].charAt(0) : "";
    return fInitial + sInitial;
  }

  showAlertMessage(message, type, duration, position) {
    const alertMessage = (
      <AlertMessage
        message={message}
        messageType={type}
        duration={duration}
        position={position}
      />
    );
    const bodyElement = document.querySelector("body");

    if (!document.getElementById("alert-container")) {
      const alertContainer = document.createElement("div");
      alertContainer.id = "alert-container";
      bodyElement.appendChild(alertContainer);
    }

    const alertContainer = document.getElementById("alert-container");

    ReactDOM.render(alertMessage, alertContainer);

    setTimeout(() => {
      ReactDOM.unmountComponentAtNode(alertContainer);
    }, duration + 50);
  }

  async fetchUrl() {
    const randomNumber = Math.floor(Math.random() * 1300);
    try {
      const response = await fetch(
        `https://source.unsplash.com/collection/11649432/800x600/?sig=${randomNumber}`
      );
      CommonFunctions.backgroundUrl = response.url;
      window.localStorage.setItem(
        "backgroundUrlLogin",
        CommonFunctions.backgroundUrl
      );
    } catch (error) {
      CommonFunctions.backgroundUrl = "";
      window.localStorage.setItem(
        "backgroundUrlLogin",
        CommonFunctions.backgroundUrl
      );
      console.error(error?.message);
    }
  }

  getUrl() {
    return CommonFunctions.backgroundUrl;
  }

  googleObjectDefinedStatus(status) {
    CommonFunctions.googleMapObjectStatus = status;
  }
}

export default CommonFunctions;
