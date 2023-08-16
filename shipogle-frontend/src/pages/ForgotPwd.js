import React, { useState } from "react";
import Header from "../components/Header";
import shipogleLogo from "../assets/shipogleLogo.png";
import Constants from "../Constants";
import axios from "axios";
import Cookies from "js-cookie";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import Button from "@mui/material/Button";
import Card from "@mui/material/Card";
import TextField from "@mui/material/TextField";
import { Typography } from "@mui/material";
import CommonFunctions from "../services/CommonFunction";
import customAxios from "../utils/MyAxios";

export default function ForgotPwd() {
  //forgot password rest
  const location = useLocation();
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const [path, setPath] = useState(location.pathname);
  const [containerStyle, setContainerStyle] = useState({});
  const [newpassword, setNewPass] = useState("");
  const [confirmNewPass, setConfirmNewPass] = useState("");
  const commFunc = new CommonFunctions();
  const { token } = useParams();

  React.useEffect(() => {
    setBackgroundImage(window.localStorage.getItem("backgroundUrlLogin"));
  }, []);
  const setBackgroundImage = (url) => {
    if (url === "") {
      const containerStyle = {
        background:
          "linear-gradient(180deg, rgba(245,245,245,1) 0%, rgba(255,255,255,0.7805716036414566) 100%)",
        height: "calc(100vh - 58px)",
        width: "100%",
        margin: "0px",
      };
      setIsLoading(false);
      setContainerStyle(containerStyle);
    } else {
      const containerStyle = {
        backgroundImage: "url(" + url + ")",
        height: "calc(100vh - 58px)",
        width: "100%",
        margin: "0px",
        backgroundPosition: "center center",
        backgroundRepeat: "no-repeat",
        backgroundSize: "cover",
      };
      setIsLoading(false);
      setContainerStyle(containerStyle);
    }
  };

  const submit = (e) => {
    e.preventDefault();
    if (path === "/forgotpwd") {
      axios
        .post(Constants.API_FORGOT_PWD, {
          email: email,
        })
        .then(
          (response) => {
            commFunc.showAlertMessage(
              "Reset Link sent successfully to your email id. Please use it to reset password",
              "success",
              4000,
              "top"
            );
          },
          (error) => {
            console.error(error);
            commFunc.showAlertMessage(
              "Error while sending link, please check email or try again later!",
              "error",
              4000,
              "top"
            );
          }
        );
    } else {
      const password = newpassword;
      const body = {
        token: token,
        password: password,
      };
      axios.post(Constants.API_RESET_PWD, body).then(
        (res) => {
          commFunc.showAlertMessage(
            "Password changes successfully",
            "success",
            3000,
            "bottom"
          );
          navigate("/login");
        },
        (error) => {
          console.error(error);
          commFunc.showAlertMessage(
            "Error while setting password or try again later!",
            "error",
            4000,
            "top"
          );
        }
      );
    }
  };

  return (
    <div style={containerStyle} className="container">
      <div
        style={{
          height: "100%",
          width: "100%",
          backdropFilter: "blur(1px)",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        <Card
          sx={{
            maxWidth: "420px",
            width: "100%",
            height: "430px",
            padding: "0rem 1rem 0rem 1rem",
            display: "flex",
            flexFlow: "column",
            justifyContent: "center",
            background:
              "linear-gradient(180deg, rgba(255,255,255,0.9514399509803921) 0%, rgba(242,242,242,0.8954175420168067) 50%, rgba(230,230,230,0.6041010154061625) 100%);",
          }}
        >
          <div style={{ marginTop: "-rem", marginBottom: "2rem" }}>
            <Header title="S H I P O G L E" bgColor="transparent"></Header>
          </div>

          <h2 style={{ marginTop: "-2rem" }}>Reset Password</h2>
          <form onSubmit={submit}>
            {path === "/forgotpwd" ? (
              <TextField
                sx={{ width: "100%", marginBottom: "1rem" }}
                required
                id="username"
                label="User Email"
                variant="outlined"
                value={email}
                type="email"
                placeholder="Please enter your email id"
                onChange={(event) => {
                  setEmail(event.target.value);
                }}
              />
            ) : (
              <>
                {" "}
                <TextField
                  sx={{ width: "100%", marginBottom: "1.5rem" }}
                  required
                  id="password"
                  value={newpassword}
                  type="password"
                  label="New Password"
                  variant="outlined"
                  placeholder="password***"
                  onChange={(event) => {
                    setNewPass(event.target.value);
                  }}
                />
                <br></br>
                <TextField
                  sx={{ width: "100%", marginBottom: "0rem" }}
                  required
                  id="new-password"
                  value={confirmNewPass}
                  type="password"
                  label="Confirm New Password"
                  variant="outlined"
                  placeholder="password***"
                  onChange={(event) => {
                    setConfirmNewPass(event.target.value);
                  }}
                />
              </>
            )}

            <div style={{ textAlign: "center", marginTop: "2rem" }}>
              <Button
                disabled={isLoading}
                sx={{
                  minWidth: "94px",
                  maxWidth: "240px",
                  width: "100%",
                }}
                variant="contained"
                type="submit"
              >
                {path === "/forgotpwd" ? (
                  <>
                    <Typography>Send verification Link</Typography>
                  </>
                ) : (
                  <>
                    <Typography>Reset Password</Typography>
                  </>
                )}
              </Button>
            </div>
          </form>
        </Card>
      </div>
    </div>
  );
}
