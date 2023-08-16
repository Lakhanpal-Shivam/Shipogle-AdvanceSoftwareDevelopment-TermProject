import * as React from "react";
import { useLocation, useNavigate } from "react-router-dom";

import Card from "@mui/material/Card";
import CardHeader from "@mui/material/CardHeader";
import CardContent from "@mui/material/CardContent";
import CardActions from "@mui/material/CardActions";

import { Button } from "@mui/material";
import Typography from "@mui/material/Typography";

import customAxios from "../utils/MyAxios";
import Constants from "../Constants";
import StaticMap from "./StaticMap";
import CommonFunctions from "../services/CommonFunction";

export default function OrderDetails() {
  const commFunc = new CommonFunctions();
  const location = useLocation().pathname.split("/");
  const orderId = location[4];
  const pathname = location[3];
  const [driverLocation, setDriverLocation] = React.useState({});
  const { state } = useLocation();
  const navigate = useNavigate();
  const statusList = {
    inprogress: "In Progress",
    canceled: "Canceled Order",
    completed: "Order Delivered",
    pending: "Order is still Pending",
  };
  React.useEffect(() => {
    if (pathname === "inprogress") {
      setDriverLocation({
        lat: state.order.deliverer.latitude,
        lng: state.order.deliverer.longitude,
      });
    }
  }, [
    pathname,
    state.order.deliverer.latitude,
    state.order.deliverer.longitude,
  ]);

  const cancelOrder = () => {
    const body = {
      package_order_id: orderId,
    };
    customAxios.put(Constants.CANCELORDER, body).then(
      (res) => {
        commFunc.showAlertMessage(
          "Order Canceled! Initiating a refund if paid.",
          "info",
          3000,
          "bottom"
        );
        navigate("/orders");
      },
      (error) => {
        commFunc.showAlertMessage(
          "Error while canceling order",
          "error",
          3000,
          "bottom"
        );
        console.error(error);
      }
    );
  };

  const routeTo = (order, route) => {
    navigate(route, {
      state: {
        orderDetails: order,
      },
    });
  };

  return (
    <div className="order-details-container">
      <Card sx={{ maxWidth: "600px", margin: "2rem auto" }}>
        <CardHeader
          title={`Deliverer: ${state.order?.driverRoute?.driverName}`}
          subheader={`status: ${statusList[pathname]}`}
        />
        {(pathname === "completed" || pathname === "inprogress") && (
          <div style={{ textAlign: "center" }}>
            <StaticMap
              pickup={[
                state?.order?.driverRoute?.pickupLocationCoords[0],
                state?.order?.driverRoute?.pickupLocationCoords[1],
                state?.order?.driverRoute?.sourceCity,
              ]}
              dropOff={[
                state?.order?.driverRoute?.dropoffLocationCoords[0],
                state?.order?.driverRoute?.dropoffLocationCoords[1],
                state?.order?.driverRoute?.destinationCity,
              ]}
              height={264}
              width={560}
              zoom={5}
              trackedLocation={
                pathname === "inprogress" ? driverLocation : null
              }
            ></StaticMap>
          </div>
        )}
        {pathname === "pending" && (
          <h4 style={{ margin: "auto 2rem" }}>
            Tracking will be available once Deliverer Pickups your package
          </h4>
        )}
        <CardContent>
          <ul>
            <li>
              <Typography>
                {" "}
                Order Date:{" "}
                {`${state?.order?.created_at[1]}/${state?.order?.created_at[2]}/${state?.order?.created_at[0]}`}
              </Typography>
            </li>
            <li>
              <Typography>
                From: {state?.order?.driverRoute?.sourceCity}
              </Typography>
            </li>
            <li>
              <Typography>
                To: {state?.order?.driverRoute?.destinationCity}
              </Typography>
            </li>
            <li>
              <Typography>{`Pickup Date: ${
                new Date(state?.order?.driverRoute?.pickupDate)
                  .toLocaleDateString()
                  .split(",")[0]
              }`}</Typography>
            </li>
          </ul>
        </CardContent>
        <CardActions
          sx={{
            display: "flex",
            justifyContent: "center",
            marginTop: "1rem",
            marginBottom: "1rem",
          }}
        >
          {pathname === "completed" && (
            <>
              <Button
                variant="contained"
                onClick={() => {
                  routeTo(state.order, "/issue");
                }}
              >
                Raise an Issue
              </Button>
              <Button
                variant="contained"
                onClick={() => {
                  routeTo(state.order, "/feedback");
                }}
              >
                FeedBack
              </Button>
            </>
          )}
          {pathname === "pending" && (
            <Button
              variant="contained"
              color="error"
              onClick={() => {
                cancelOrder();
              }}
            >
              Confirm Cancel
            </Button>
          )}
        </CardActions>
      </Card>
    </div>
  );
}
