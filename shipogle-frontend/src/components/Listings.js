import React, { useCallback, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import LocationOnIcon from "@mui/icons-material/LocationOn";
import WhereToVoteIcon from "@mui/icons-material/WhereToVote";
import IconButton from "@mui/material/IconButton";
import FormatListBulletedIcon from "@mui/icons-material/FormatListBulleted";
import MapIcon from "@mui/icons-material/Map";

import "./Listings.css";
import MapView from "./MapView";
import { Avatar, Button, Typography } from "@mui/material";

function Listings({ data }) {
  const location = useLocation();
  const navigate = useNavigate();
  const path = location.pathname;
  const [listingCards, setListingCards] = useState([]);
  const [showMapView, setShowMapView] = useState(false);

  const routeTo = useCallback(
    (routeId, listing) => {
      navigate("/order/startend", { state: { id: routeId, order: listing } });
    },
    [navigate]
  );

  const isInDateRange = (startDate, endDate) => {
    const currentDate = new Date();
    const start = new Date(startDate);
    const end = new Date(endDate);

    // Reset hours, minutes, seconds, and milliseconds to compare dates only
    currentDate.setHours(0, 0, 0, 0);
    start.setHours(0, 0, 0, 0);
    end.setHours(0, 0, 0, 0);

    return start <= currentDate && currentDate <= end;
  };

  const getDriverInitials = (name) => {
    const nameArray = name.split(" ");
    const fInitial = nameArray[0].charAt(0);
    const sInitial = nameArray.length > 1 ? nameArray[1].charAt(0) : "";
    return fInitial + sInitial;
  };

  const nothing = () => {
    return;
  };

  const createListingCards = useCallback(() => {
    const cards = data.map((listing) => (
      <div
        className="listing-card"
        onClick={() =>
          path !== "/deliveries"
            ? navigate(`/courier/details/${listing?.driverRouteId}`, {
              state: { routeData: listing },
            })
            : nothing()
        }
        key={listing?.driverRouteId}
      >
        {listing.avatar && (
          <>
            <img
              className="listing-card-avatar"
              src={listing?.avatar}
              alt="avatar"
            ></img>
          </>
        )}
        {!listing.avatar && (
          <>
            <Avatar
              sx={{
                height: "56px",
                width: "56px",
                fontSize: "32px",
                marginLeft: "12px",
                marginRight: "4px",
              }}
            >
              {listing.driverName ? getDriverInitials(listing.driverName) : ""}
            </Avatar>
          </>
        )}

        <span className="listing-card-divider"></span>
        <div className="lisitng-card-header">
          <p className="listing-card-heading">
            {listing?.driverName ? listing.driverName : "Name not found"}
          </p>
          <hr style={{ marginBottom: "0px", flexGrow: 1 }} />
          <div className="listing-card-subheading">
            <LocationOnIcon /> &nbsp;
            <p className="listing-card-location">{listing?.sourceCity}</p>&nbsp;
            <ArrowForwardIcon />
            &nbsp;
            <WhereToVoteIcon />
            &nbsp;
            <p className="listing-card-location">{listing?.destinationCity}</p>
            <span style={{ flexGrow: 1 }}></span>
            <p>
              Pickup Date:{" "}
              {new Date(listing.pickupDate).toLocaleString().split(",")[0]}
            </p>
          </div>
        </div>
        {path === "/deliveries" && (
          <>
            <Button
              sx={{ margin: "0px 12px", height: "74px" }}
              disabled={!isInDateRange(listing.pickupDate, listing.dropoffDate)}
              onClick={() => {
                routeTo(listing.driverRouteId, listing);
              }}
            >
              <div>
                start /<p style={{ margin: "0px" }}>end</p> order
              </div>
            </Button>
          </>
        )}
      </div>
    ));
    if (path === "/deliveries") {
      let newArr = [];
      cards.forEach((element) => {
        newArr.unshift(element);
      });
      setListingCards(newArr);
    } else setListingCards(cards);
  }, [data, navigate, path, routeTo]);

  useEffect(() => {
    createListingCards();
  }, [createListingCards, data]);

  return (
    <>
      <div
        className="courier-listing-container"
        style={{
          marginBottom: "1rem",
          width: "90%",
        }}
      >
        {path === "/deliveries" && <h3>Your posts</h3>}
        {path !== "/deliveries" && (
          <div className="view-buttons-container">
            <p className="view-buttons-text">
              <Typography>Tap on a deliverer to book or know more</Typography>
            </p>
            <IconButton
              sx={{ height: "42px", width: "42px" }}
              aria-label="list-view"
              onClick={() => setShowMapView(false)}
            >
              <FormatListBulletedIcon />
            </IconButton>
            <IconButton
              sx={{ height: "42px", width: "42px" }}
              aria-label="map-view"
              onClick={() => setShowMapView(true)}
            >
              <MapIcon />
            </IconButton>
          </div>
        )}

        <br />
        {!showMapView && <>{listingCards}</>}
        {showMapView && <MapView locations={data} />}
      </div>
      {listingCards.length <= 0 && (
        <h4 style={{ marginTop: "3rem" }}>No Listings</h4>
      )}
    </>
  );
}

export default Listings;
