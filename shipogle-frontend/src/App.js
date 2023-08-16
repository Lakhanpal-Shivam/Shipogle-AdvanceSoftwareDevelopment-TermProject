import React, { useState, useEffect } from "react";
import { Outlet, useLocation, useNavigate } from "react-router-dom";

import "./App.css";
import AlertMessage from "./components/AlertMessage";
import CommonFunctions from "../src/services/CommonFunction";
import NavBar from "./components/NavBar";
import { AuthProvider } from "./utils/Auth";
import "./Constants";
import Header from "./components/Header";
import shipogleLogo from "./assets/shipogleLogo.png";
import Constants from "./Constants";
import customAxios from "./utils/MyAxios";

const API_KEY = "AIzaSyBPtYm-CJPPW4yO9njM-e9YBWyp-DwIODM";
let userLocation = { latitude: "", longitude: "" };

window.initMap = function () {
  const comfunc = new CommonFunctions();
  comfunc.fetchUrl();
  comfunc.googleObjectDefinedStatus(true);
};

const App = (props) => {
  const location = useLocation();
  const navigate = useNavigate();

  const [showAlert, setShowAlert] = useState(false);
  const [pathname, setPathname] = useState(location.pathname);
  const [tokenChecked, setTokenChecked] = useState(false);

  const alertSettings = {
    alertMessage: "",
    alertType: "",
    alertDuration: 0,
    position: "bottom",
  };

  useEffect(() => {
    const paid_orders = window.localStorage.getItem("paid_orders");
    if (!paid_orders) window.localStorage.setItem("paid_orders", []);

    const head = document.querySelector("head");
    if (!document.getElementById("font-style-link")) {
      const link = document.createElement("link");
      link.rel = "stylesheet";
      link.setAttribute("id", "font-style-link");
      link.href =
        "https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap";
      head.append(link);
    }
    if (!document.getElementById("google-maps-script")) {
      const script = document.createElement("script");
      script.setAttribute("async", "");
      script.setAttribute("defer", "");
      script.setAttribute("id", "google-maps-script");
      script.src = `https://maps.googleapis.com/maps/api/js?key=${API_KEY}&libraries=places,maps,marker&callback=initMap`;
      head.append(script);
    }
    navigator.geolocation.getCurrentPosition(
      (position) => {
        userLocation.latitude = position.coords.latitude;
        userLocation.longitude = position.coords.longitude;
      },
      (error) => {
        setAlert(
          error.message ? error.message : "Please give Location Access",
          "error",
          3000,
          "bottom"
        );
      }
    );
  }, []);

  useEffect(() => {
    const commFunc = new CommonFunctions();
    setPathname(location.pathname);
    const token = window.localStorage.getItem("authToken");
    const excludedPaths = [
      "/",
      "/courier/search",
      "/login",
      "/registration",
      "/registration/success",
    ];
    if (!token) {
      if (excludedPaths.includes(pathname) || pathname.includes("/forgotpwd")) {
        if (pathname === "/courier/search" || pathname === "/") {
          commFunc.showAlertMessage(
            "Session Expired, Please Login in for better exprience",
            "info",
            3000,
            "bottom"
          );
        }
      } else {
        commFunc.showAlertMessage(
          "Please Login in before continuing",
          "info",
          3000,
          "bottom"
        );
        navigate("/login");
      }
    } else {
      if (
        !tokenChecked &&
        (!excludedPaths.includes(pathname) || pathname.includes("/forgotpwd"))
      ) {
        customAxios.get(Constants.API_USER_INFO_FROM_TOKEN).then((res) => {
          const user_id = res.data.user_id;
          const user_name = res.data.first_name + " " + res.data.last_name;
          window.localStorage.setItem("user_id", user_id);
          window.localStorage.setItem("user_name", user_name);
          setTokenChecked(true);
        });
      } else {
        setTokenChecked(true);
      }
    }
  }, [location, navigate, pathname, tokenChecked]);

  const setAlert = (message, type, duration, position) => {
    setShowAlert(true);
    alertSettings.alertMessage = message;
    alertSettings.alertType = type;
    alertSettings.alertDuration = duration;
    alertSettings.position = position ? position : "bottom";
    setTimeout(() => {
      setShowAlert(false);
    }, duration + 50);
  };

  return (
    <>
      <AuthProvider>
        <NavBar
          authStatus={tokenChecked}
          authStatusUpdater={setTokenChecked}
        ></NavBar>
        {pathname === "/" && (
          <>
            <div
              style={{
                display: "flex",
                flexFlow: "column",
                justifyContent: "center",
                alignItems: "center",
              }}
            >
              <img
                src={shipogleLogo}
                height={164}
                width={164}
                style={{ margin: "2rem auto -2rem auto" }}
                alt="Shipogle"
              ></img>
              <Header
                title="SHIPOGLE"
                info="Your go to place for Sending a courier / Delivering a courier"
              ></Header>
            </div>
          </>
        )}
        <Outlet></Outlet>
        {showAlert && (
          <AlertMessage
            message={alertSettings.alertMessage}
            messageType={alertSettings.alertType}
            duration={alertSettings.alertDuration}
            position={alertSettings.position}
          ></AlertMessage>
        )}
      </AuthProvider>
    </>
  );
};

export default App;
