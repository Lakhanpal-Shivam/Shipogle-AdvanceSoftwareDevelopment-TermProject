import React from "react";
import Header from "../components/Header";
import shipogleLogo from "../assets/shipogleLogo.png";
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
import { useLocation, useNavigate } from "react-router-dom";

export default function Issue() {
  const commFunc = new CommonFunctions();
  const { state } = useLocation();
  const [description, setDescription] = React.useState("");
  const navigate = useNavigate();
  const submit = (e) => {
    const body = {
      package_order_id: state.orderDetails._package.id,
      description: description,
    };
    customAxios.post(Constants.POSTISSUE, body).then(
      (res) => {
        commFunc.showAlertMessage("Issue submitted", "success", 3000, "bottom");
        navigate("/orders");
      },
      (error) => {
        console.error(error);
        commFunc.showAlertMessage(
          "Issue not submitted",
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
          <CardHeader title="Please provide issue details"></CardHeader>
          <CardContent>
            <br></br>
            <TextField
              multiline
              onChange={(event) => {
                setDescription(event.target.value);
              }}
              rows={3}
              sx={{ width: "100%" }}
              label="Issue Description"
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
