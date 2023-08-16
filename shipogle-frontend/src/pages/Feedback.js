import React from "react";
import Header from "../components/Header";
import shipogleLogo from "../assets/shipogleLogo.png";
import { Link, useLocation, useNavigate } from "react-router-dom";
import StarRating from "./StarRating";
import customAxios from "../utils/MyAxios";
import Constants from "../Constants";
import CommonFunctions from "../services/CommonFunction";
import {
  Button,
  Card,
  CardContent,
  CardHeader,
  TextField,
} from "@mui/material";

export default function Feedback() {
  const { state } = useLocation();
  const navigate = useNavigate();
  const commFunc = new CommonFunctions();
  const orderDetails = state.orderDetails;
  const [showMsg, setShowMsg] = React.useState(0);
  const [rating, setRating] = React.useState(0);
  const [review, setReview] = React.useState("");

  const submit = (e) => {
    setShowMsg((prevShowMsg) => 1);
    const body = {
      driver_route_id: state.orderDetails.driverRoute.driverRouteId,
      star: rating,
      review: review,
    };
    customAxios.post(Constants.POSTRATING, body).then(
      (res) => {
        commFunc.showAlertMessage(
          "Rating submitted",
          "success",
          3000,
          "bottom"
        );
        navigate("/orders");
      },
      (error) => {
        console.error(error);
        commFunc.showAlertMessage(
          "Rating not submitted",
          "error",
          3000,
          "bottom"
        );
      }
    );
  };
  return (
    <div className="feedback-issue-page">
      <center>
        <Card style={{ maxWidth: "500px", width: "100%", marginTop: "2rem" }}>
          <img
            alt="logo"
            style={{ margin: "2rem auto -5rem auto" }}
            src={shipogleLogo}
            width="164px"
            height="164px"
          ></img>

          <Header title="S H I P O G L E" />
          <CardHeader title="We value your feedback"></CardHeader>
          <CardContent>
            <StarRating starRating={setRating} />
            <br></br>
            <TextField
              multiline
              onChange={(event) => {
                setReview(event.target.value);
              }}
              rows={3}
              sx={{ width: "100%" }}
              label="Review"
              placeholder="Please enter your comments"
            ></TextField>

            <Button
              sx={{ margin: "1rem auto 0rem", width: "120px" }}
              variant="contained"
              onClick={(event) => {
                submit(event);
              }}
            >
              Submit
            </Button>
          </CardContent>
        </Card>
      </center>
    </div>
  );
}
