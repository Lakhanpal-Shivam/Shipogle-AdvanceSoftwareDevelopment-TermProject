import { Link, useNavigate } from "react-router-dom";
import React, { useContext, useEffect } from "react";

import { IconButton, Typography } from "@mui/material";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import styled from "@emotion/styled";

import AddRoadIcon from "@mui/icons-material/AddRoad";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import ForumIcon from "@mui/icons-material/Forum";
import SearchIcon from "@mui/icons-material/Search";
import NotificationsMenu from "./NotificationsMenu";
import "./navBar.css";
import { AuthContext } from "../utils/Auth";
import CommonFunctions from "../services/CommonFunction";
import customAxios from "../utils/MyAxios";
import Constants from "../Constants";
import shipogleLogo from "../assets/shipogleLogo.png";

const ExpandButton = styled(Button)({
  minWidth: "18px",
  minHeight: "32px",
  width: "18px",
  height: "32px",

  marginRight: "4px",
  marginTop: "4px",
  color: "black",
});

export default function NavBar({ authStatus, authStatusUpdater }) {
  const { isAuthenticated, login, logout } = useContext(AuthContext);
  const [anchorEl, setAnchorEl] = React.useState(null);
  const open = Boolean(anchorEl);
  const commFunc = new CommonFunctions();
  const navigate = useNavigate();

  const getCurrentLocation = (callback) => {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const userLocation = {
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
        };
        callback(userLocation);
      },
      (error) => {
        this.showAlertMessage(
          error.message ? error.message : "Please give Location Access",
          "error",
          3000,
          "bottom"
        );
      }
    );
  };

  useEffect(() => {
    if (authStatus && isAuthenticated) {
      login();
    } else if (!authStatus && isAuthenticated) {
      login();
    } else if (authStatus && !isAuthenticated) {
      login();
    } else if (!authStatus && !isAuthenticated) {
      logout();
    } else {
      logout();
    }
  }, [authStatus, login, logout, isAuthenticated]);

  const handleClickOnExpand = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleCloseOnExpand = () => {
    setAnchorEl(null);
  };

  const route = (url) => {
    navigate(url);
  };

  useEffect(() => {
    if (isAuthenticated) {
      getCurrentLocation((userLocation) => {
        customAxios.put(Constants.UPDATELOCATION, userLocation).then(
          (res) => { },
          (error) => {
            console.error(error);
          }
        );
      });
    } else {
      authStatusUpdater(false);
    }
  }, [authStatusUpdater, isAuthenticated]);

  return (
    <div className="navbar-container">
      <div className="navbar-logo">
        <img
          src={shipogleLogo}
          alt="Shipogle"
          className="navbar-logo-img"
        ></img>
        <Typography
          sx={{
            fontSize: "24px",
            textShadow:
              "-1px -1px 1px rgba(255,255,255,.1), 1px 1px 1px rgba(0,0,0,.2)",
            margin: "-4px 0px 0px 0px",
            fontWeight: "450",
          }}
        >
          SHIPOGLE
        </Typography>
      </div>
      <div className="navbar-secondary-menu"></div>
      <div className="navbar-menu">
        <button className="navbar-menu-item">
          <SearchIcon></SearchIcon> &nbsp;
          <Link style={{ textDecoration: "none" }} to="/courier/search">
            Search
          </Link>
        </button>
        <button
          className="navbar-menu-item"
          onClick={() => {
            if (!isAuthenticated)
              commFunc.showAlertMessage(
                "Please Login before posting an ad.",
                "info",
                3000,
                "bottom"
              );
          }}
        >
          <AddRoadIcon></AddRoadIcon> &nbsp;
          <Link
            to={isAuthenticated ? "/courier/offer" : "/login"}
            style={{ textDecoration: "none" }}
          >
            Deliver
          </Link>
        </button>
        &nbsp;
        {isAuthenticated && (
          <>
            {" "}
            <NotificationsMenu />
            <IconButton
              className="icon-buttons"
              onClick={() => {
                route("/inbox");
              }}
            >
              <ForumIcon></ForumIcon>
            </IconButton>
          </>
        )}
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            height: "64px",
            marginTop: "-8px",
          }}
        >
          <Avatar onClick={handleClickOnExpand}>
            {isAuthenticated
              ? commFunc.getDriverInitials(
                window.localStorage.getItem("user_name")
              )
              : ""}
          </Avatar>

          <div>
            <ExpandButton
              aria-label="expand"
              size="small"
              id="expandButton"
              aria-controls={open ? "basic-menu" : undefined}
              aria-haspopup="true"
              aria-expanded={open ? "true" : undefined}
              onClick={handleClickOnExpand}
            >
              <ExpandMoreIcon />
            </ExpandButton>
            {!isAuthenticated && (
              <>
                <Menu
                  id="basic-menu"
                  anchorEl={anchorEl}
                  open={open}
                  onClose={handleCloseOnExpand}
                  MenuListProps={{
                    "aria-labelledby": "basic-button",
                  }}
                >
                  <MenuItem>
                    <Link
                      style={{ textDecoration: "none", color: "black" }}
                      to="/login"
                    >
                      Login
                    </Link>
                  </MenuItem>
                  <MenuItem>
                    <Link style={{ textDecoration: "none" }} to="/registration">
                      Register
                    </Link>
                  </MenuItem>
                </Menu>
              </>
            )}
            {isAuthenticated && (
              <Menu
                id="basic-menu"
                anchorEl={anchorEl}
                open={open}
                onClose={handleCloseOnExpand}
                MenuListProps={{
                  "aria-labelledby": "basic-button",
                }}
              >
                <MenuItem>
                  <Link style={{ textDecoration: "none" }} to="/orders">
                    Orders
                  </Link>
                </MenuItem>
                <MenuItem>
                  <Link style={{ textDecoration: "none" }} to="/myrequests">
                    Delivery Requests
                  </Link>
                </MenuItem>
                <MenuItem>
                  <Link style={{ textDecoration: "none" }} to="/deliveries">
                    Current Delivery
                  </Link>
                </MenuItem>
                <MenuItem>
                  <Link
                    style={{ textDecoration: "none" }}
                    to="/user/editprofile"
                  >
                    Edit Profile
                  </Link>
                </MenuItem>
                <MenuItem>
                  <Link style={{ textDecoration: "none" }} to="/issues">
                    Reported Issues
                  </Link>
                </MenuItem>
                <MenuItem
                  onClick={() => {
                    logout();
                  }}
                >
                  <Link style={{ textDecoration: "none" }} to="/login">
                    Logout
                  </Link>
                </MenuItem>
              </Menu>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
