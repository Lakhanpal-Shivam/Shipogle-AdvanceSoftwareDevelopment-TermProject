import {
  Button,
  Card,
  CardContent,
  CardHeader,
  CircularProgress,
  TextField,
} from "@mui/material";
import * as React from "react";

import { Form } from "react-router-dom";
import customAxios from "../utils/MyAxios";
import Constants from "../Constants";
import Listings from "./Listings";

export default function CurrentDelivery(props) {
  const [order_id, setOrderID] = React.useState("qeyv9c3q78r62389qryfqhi");
  const [order_code, setOrderCode] = React.useState("");
  const [MyRides, setMyRides] = React.useState([]);
  const [isLoading, setIsLoading] = React.useState(true);
  React.useEffect(() => {
    const driverId = window.localStorage.getItem("user_id");
    customAxios.get(Constants.GETDRIVERROUTES + "?driverId=" + driverId).then(
      (res) => {
        setMyRides(res.data);
        setIsLoading(false);
      },
      (error) => {
        console.error(error);
      }
    );
  }, []);

  return (
    <>
      <div
        className="listing-container"
        style={{
          marginTop: "2rem",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        {isLoading && <CircularProgress></CircularProgress>}
        {!isLoading && <Listings data={MyRides}></Listings>}
      </div>
    </>
  );
}
