import * as React from "react";

import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import Avatar from "@mui/material/Avatar";
import CardHeader from "@mui/material/CardHeader";
import CardActions from "@mui/material/CardActions";

import Constants from "../Constants";
import { useLocation } from "react-router-dom";
import customAxios from "../utils/MyAxios";
import StaticMap from "../components/StaticMap";
import CommonFunctions from "../services/CommonFunction";
import CreatePackage from "./CreatePackage";

function CourierDetails() {
  const { state } = useLocation();
  const [routeDetails] = React.useState(state.routeData);
  const [viewDetails, setViewDetails] = React.useState(true);
  const [confirmBooking, setConfirmBooking] = React.useState(false);
  const [requestSent, setRequestSent] = React.useState(false);
  const [createPackage, setCreatePackage] = React.useState(false);
  const [staticMapWidth, setMapWidth] = React.useState(0);
  const [packageID, setPackageID] = React.useState("");
  const [rating, setRating] = React.useState(3.5);
  const commFunc = new CommonFunctions();
  React.useEffect(() => {
    const el = document.getElementById("static-map-container");
    if (el) {
      setMapWidth(el.offsetWidth * 0.9);
    }
    customAxios.get(Constants.GETDELIVERERRATINGS).then(
      (res) => {},
      (error) => {
        console.error(error);
        commFunc.showAlertMessage(
          "Error while fetching ratings",
          "error",
          3000,
          "bottom"
        );
      }
    );
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const packageCreated = (package_id) => {
    setConfirmBooking(true);
    setCreatePackage(false);
    setViewDetails(false);
    setRequestSent(false);
    setPackageID(package_id);
  };

  const confirmBookingFunction = (bool) => {
    setViewDetails(false);
    setCreatePackage(true);
    setConfirmBooking(false);
    setRequestSent(false);
  };

  const requestDriver = (bool) => {
    setViewDetails(false);
    const data = {
      driver_route_id: routeDetails.driverRouteId + "",
      package_id: packageID + "",
      ask_price: parseInt(routeDetails.price).toFixed(2) + "",
    };

    customAxios.post(Constants.SENDPACKAGEREQUEST, data).then(
      (res) => {
        setConfirmBooking(false);
        setCreatePackage(false);
        setRequestSent(true);
        const body = {
          userId: state.routeData.driverId,
          title: "Request for delivery",
          message: `${window.localStorage.getItem(
            "user_name"
          )} has requested for a delivery`,
        };

        customAxios.post(Constants.API_NOTIFICATIONS, body).then(
          (res) => {
            commFunc.showAlertMessage(
              "Sent a notification to driver",
              "success",
              3000,
              "bottom"
            );
          },
          (error) => {
            console.error(error);
            commFunc.showAlertMessage(
              "Request sent but failed to send driver a Notification!",
              "error",
              3000,
              "bottom"
            );
          }
        );
      },
      (error) => {
        commFunc.showAlertMessage(
          "Failed to send request to driver",
          "error",
          3000,
          "top"
        );
        console.error(error);
      }
    );
  };

  const getDriverInitials = (name) => {
    const nameArray = name.split(" ");
    const fInitial = nameArray[0].charAt(0);
    const sInitial = nameArray.length > 1 ? nameArray[1].charAt(0) : "";
    return fInitial + " " + sInitial;
  };

  return (
    <>
      <Card
        id="route-details-card"
        sx={{ maxWidth: "768px", margin: " 2rem auto ", borderRadius: "4px" }}
      >
        {viewDetails && (
          <>
            <CardHeader
              sx={{
                boxShadow: "0px 0px 1px 1px rgba(0,0,0,0.2)",
                fontSize: "24px",
              }}
              avatar={
                routeDetails.avatar ? (
                  <Avatar sx={{ bgcolor: "blue" }}>
                    <img src={routeDetails.avatar} alt="no img"></img>
                  </Avatar>
                ) : (
                  <Avatar>{getDriverInitials(routeDetails.driverName)}</Avatar>
                )
              }
              title={routeDetails.driverName}
              subheader={`Rating: ${rating}`}
            />
            <div
              id="static-map-container"
              style={{
                width: "100%",
                margin: "auto",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                padding: "1rem 1rem 1rem 0rem",
                marginBottom: "-1rem",
              }}
            >
              <StaticMap
                pickup={[
                  routeDetails.pickupLocationCoords[0],
                  routeDetails.pickupLocationCoords[1],
                  routeDetails.sourceCity,
                ]}
                dropOff={[
                  routeDetails.dropoffLocationCoords[0],
                  routeDetails.dropoffLocationCoords[1],
                  routeDetails.destinationCity,
                ]}
                height={264}
                width={768}
                zoom={5}
              ></StaticMap>
            </div>

            <CardContent>
              <Typography gutterBottom variant="h6" component="div">
                Pickup Location: {routeDetails.sourceCity}
                <br></br>
                Dropoff Location: {routeDetails.destinationCity}
              </Typography>
              <Typography
                variant="body2"
                color="text.secondary"
                style={{ fontSize: "16px" }}
              >
                <li>
                  {"Pickup Date: " +
                    new Date(routeDetails.pickupDate).toLocaleDateString()}
                </li>
                <li>
                  {"Dropoff Date: " +
                    new Date(routeDetails.dropoffDate).toLocaleDateString()}
                </li>
                <li>{"Price: " + routeDetails.price + " CAD"}</li>
                <li>
                  {"Max package dimensions (l*b*h cm): " +
                    routeDetails.maxLength +
                    ", " +
                    routeDetails.maxWidth +
                    ", " +
                    routeDetails.maxHeight}
                </li>
                <li>{"Allowed Categories: " + routeDetails.allowedCategory}</li>
              </Typography>
            </CardContent>
            <CardActions sx={{ display: "flex", justifyContent: "center" }}>
              <Button
                sx={{ marginBottom: "1rem", width: "240px" }}
                variant="contained"
                onClick={() => {
                  confirmBookingFunction(true);
                }}
              >
                Book Now
              </Button>
            </CardActions>
          </>
        )}
        {createPackage && (
          <CreatePackage
            routeDetails={routeDetails}
            packageCreated={packageCreated}
          ></CreatePackage>
        )}
        {confirmBooking && (
          <div style={{ width: "100%", textAlign: "center", padding: "1rem" }}>
            <h4>
              Are you sure you want to confirm Booking with{" "}
              <b>{routeDetails.driverName}</b>
            </h4>
            <div style={{ display: "flex", justifyContent: "center" }}>
              <Button
                onClick={() => {
                  requestDriver(true);
                }}
              >
                Confirm
              </Button>
              &nbsp;&nbsp;
              <Button
                color="error"
                onClick={() => {
                  confirmBookingFunction(false);
                }}
              >
                Cancel
              </Button>
            </div>
          </div>
        )}

        {requestSent && (
          <div style={{ textAlign: "center" }}>
            <h5>Request Sent Successfully.</h5>
          </div>
        )}
      </Card>
    </>
  );
}

export default CourierDetails;
