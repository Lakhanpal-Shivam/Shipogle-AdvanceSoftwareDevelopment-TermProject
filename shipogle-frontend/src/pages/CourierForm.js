import React from "react";
import { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";

import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import OutlinedInput from "@mui/material/OutlinedInput";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import ListItemText from "@mui/material/ListItemText";
import Select from "@mui/material/Select";
import Checkbox from "@mui/material/Checkbox";
import SearchIcon from "@mui/icons-material/Search";
import PostAddIcon from "@mui/icons-material/PostAdd";

import LocationAutoComplete from "../components/LocationAutoComplete";
import customAxios from "../utils/MyAxios";
import Constants from "../Constants";
import CommonFunctions from "../services/CommonFunction";
import "./courierForm.css";
import Listings from "../components/Listings";
import Data from "./data";
import axios from "axios";
import { CircularProgress } from "@mui/material";

const API_KEY = "AIzaSyBPtYm-CJPPW4yO9njM-e9YBWyp-DwIODM";
const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
  PaperProps: {
    style: {
      maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
      width: 250,
    },
  },
};

function CourierForm() {
  const date = new Date();
  const data = new Data();
  const commFunc = new CommonFunctions();
  const location = useLocation();
  const navigate = useNavigate();
  const [path, setLocationPath] = useState("");
  const presentDate = `${date.getFullYear()}-${
    date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1
  }-${date.getDate()}`;
  const [sourceCityName, setSourceCityName] = useState("");
  const [sourceCityReferenceId, setSourceCityReferenceId] = useState("");
  const [destinationsCityName, setDestinationsCityName] = useState("");
  const [destinationsCityReferenceId, setDestinationsCityReferenceId] =
    useState("");
  const [maxPackages, setMaxPackages] = useState(1);
  const [maxLength, setMaxLength] = useState(1);
  const [maxWidth, setMaxWidth] = useState(1);
  const [maxHeight, setMaxHeight] = useState(1);
  const [pickupDate, setPickupDate] = useState(presentDate);
  const [dropoffDate, setDropoffDate] = useState(presentDate);
  const [allowedCategory, setAllowedCategory] = useState([]);
  const [radius, setRadius] = useState(2);
  const [price, setPrice] = useState(1);
  const allowedCategoryLabels = ["Documents", "Fragile", "Liquids", "General"];
  const [pickupLocationCoords, setPickupLocationCoords] = useState([]);
  const [dropoffLocationCoords, setDropoffLocationCoords] = useState([]);
  const [listings, setListings] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [sourceAddress, setSourceAddress] = useState("");
  const [destinationAddress, setDestinationAddress] = useState("");

  useEffect(() => {
    if (!path) {
      if (location.pathname.includes("search")) {
        setLocationPath("search");
      } else {
        setLocationPath("post");
      }
    }
    //setListings(demoData.listings);
  }, []);

  useEffect(() => {
    if (location.pathname.includes("search")) {
      setLocationPath("search");
    } else {
      setLocationPath("post");
    }
  }, [location]);

  const onLocationChange = (key, value) => {
    if (!value || value === "") {
      return;
    }
    const data = { place: value.description, place_id: value.place_id };
    let latLng = [];
    axios
      .get(
        `https://maps.googleapis.com/maps/api/place/details/json?place_id=${data.place_id}&fields=geometry&key=${API_KEY}`
      )
      .then(
        (res) => {
          if (res.data) {
            latLng = [
              res.data?.result?.geometry?.location?.lat,
              res.data?.result?.geometry?.location?.lng,
            ];
          }
        },
        (error) => {
          commFunc.showAlertMessage(
            "Could not fetch entered location coordinates",
            "error",
            3000,
            "bottom"
          );
          console.error(error);
        }
      )
      .then(() => {
        axios
          .get(
            `https://maps.googleapis.com/maps/api/geocode/json?latlng=${latLng[0]},${latLng[1]}&key=${API_KEY}`
          )
          .then(
            (res) => {
              let cityName;
              const results = res.data.results[0].address_components;
              for (const result of results) {
                if (result.types.includes("locality")) {
                  cityName = result.long_name;
                }
              }
              if (key === "sourceCity") {
                setPickupLocationCoords(latLng);
                setSourceCityName(cityName);
              } else if (key === "destinations") {
                setDropoffLocationCoords(latLng);
                setDestinationsCityName(cityName);
              }
            },
            (error) => {
              commFunc.showAlertMessage(
                "error fetching place details",
                "error",
                2000,
                "bottom"
              );
            }
          );
      });

    if (key === "sourceCity") {
      setSourceAddress(data.place);
      setSourceCityReferenceId(data.place_id);
    } else if (key === "destinations") {
      setDestinationAddress(data.place);
      setDestinationsCityReferenceId(data.place_id);
    }
  };

  const onSubmit = (event) => {
    event.preventDefault();
    // submit form data
    //add days to deliver
    const data = {};

    data["maxPackages"] = maxPackages.toString();

    data["price"] = price.toString();

    if (path === "search") {
      setIsLoading(true);
      data["pickupDataTime"] = pickupDate + " 21:00:00";
      data["sourceCity"] = sourceCityName.split(",")[0];
      data["destination"] = destinationsCityName.split(",")[0];
      data["radius"] = "-1";

      const parms = new URLSearchParams(data).toString();
      customAxios.get(Constants.DRIVERROUTE + "?" + parms).then(
        (res) => {
          setListings(res.data);
          setIsLoading(false);
        },
        (error) => {
          commFunc.showAlertMessage(
            "Error while fetching posts",
            "error",
            3000,
            "bottom"
          );
          console.error(error);
        }
      );
    }

    if (path !== "search") {
      data["pickupDate"] = pickupDate;
      data["sourceCity"] = sourceCityName;
      data["sourceCityReferenceId"] = sourceCityReferenceId;
      data["destinationCity"] = destinationsCityName;
      data["destinationCityReferenceId"] = destinationsCityReferenceId;
      data["maxLength"] = maxLength.toString();
      data["maxWidth"] = maxWidth.toString();
      data["maxHeight"] = maxHeight.toString();
      data["dropoffDate"] = dropoffDate;
      data["daysToDeliver"] = "1";
      data["pickupLocationCoords"] = pickupLocationCoords;
      data["dropoffLocationCoords"] = dropoffLocationCoords;
      data["allowedCategory"] = allowedCategory;
      data["driverId"] = localStorage.getItem("user_id");
      const driverName = window.localStorage.getItem("user_name");
      data["driverName"] = driverName ? driverName : "Name not provided";
      data["sourceAddress"] = sourceAddress;
      data["destinationAddress"] = destinationAddress;
      customAxios.post(Constants.DRIVERROUTE, data).then(
        (res) => {
          commFunc.showAlertMessage(
            "Post created Successfully.",
            "success",
            3000,
            "bottom"
          );
          setTimeout(() => {
            navigate("/courier/search");
          }, 1000);
        },
        (error) => {
          console.error(error);
          commFunc.showAlertMessage(
            "There was an error creating post",
            "error",
            3000,
            "bottom"
          );
        }
      );
    }
  };

  return (
    <>
      <div className="form-container">
        <h3 style={{ margin: "0px 0px 16px 6px" }}>
          {path === "search" ? "Search Couriers" : "Post a delivery"}
        </h3>
        <form
          id="courierForm"
          onSubmit={(event) => {
            onSubmit(event);
          }}
        >
          <LocationAutoComplete
            className="courier-form-input"
            id={"sourceCity"}
            setSelectedLocation={onLocationChange}
            fieldType={"outlined"}
            label={"From"}
            placeholder={"e.g Halifax"}
            required={true}
          ></LocationAutoComplete>

          <LocationAutoComplete
            className="courier-form-input"
            id={"destinations"}
            setSelectedLocation={onLocationChange}
            fieldType={"outlined"}
            label={"To"}
            placeholder={"e.g Montreal"}
            required={true}
          ></LocationAutoComplete>

          <TextField
            id="pickup-date"
            className="courier-form-input"
            variant="filled"
            type="date"
            min={pickupDate}
            required
            pattern="\d{4}-\d{2}-\d{2}"
            defaultValue={pickupDate}
            onChange={(event) => {
              const value = event.target.value;
              setPickupDate(value);
            }}
            label="Pickup Date"
            placeholder="Pickup Date"
          ></TextField>

          <TextField
            id="dropoff-date"
            className="courier-form-input"
            variant="filled"
            type="date"
            required
            pattern="\d{4}-\d{2}-\d{2}"
            defaultValue={dropoffDate}
            onChange={(event) => {
              const value = event.target.value;
              setDropoffDate(value);
            }}
            label="Dropoff Date"
            placeholder="Dropoff Date"
          ></TextField>

          <TextField
            className="courier-form-input"
            id="number-of-packages"
            type="number"
            label="Packages"
            min="1"
            required
            placeholder="1"
            value={maxPackages}
            onChange={(event) => {
              const value = event.target.value;
              setMaxPackages(value);
            }}
          ></TextField>
          <div
            style={{
              display: "flex",
              minWidth: 200,
              maxWidth: 300,
              width: "100%",
            }}
          >
            <TextField
              id="package-length"
              variant="filled"
              type="number"
              label="Length (in cm)"
              placeholder="10"
              min="1"
              required
              sx={{ flexGrow: 1 }}
              value={maxLength}
              onChange={(event) => {
                const value = event.target.value;
                setMaxLength(value);
              }}
            ></TextField>
            &nbsp;
            <TextField
              id="package-width"
              variant="filled"
              type="number"
              min="1"
              required
              label="Width (in cm)"
              placeholder="10"
              sx={{ flexGrow: 1 }}
              value={maxWidth}
              onChange={(event) => {
                const value = event.target.value;
                setMaxWidth(value);
              }}
            ></TextField>
            &nbsp;
            <TextField
              id="package-height"
              variant="filled"
              min="1"
              required
              type="number"
              label="Height (in cm)"
              placeholder="10"
              sx={{ flexGrow: 1 }}
              value={maxHeight}
              onChange={(event) => {
                const value = event.target.value;
                setMaxHeight(value);
              }}
            ></TextField>
          </div>
          <div
            style={{
              display: "flex",
              maxWidth: "300px",
              width: "100%",
              padding: "0px 16px",
            }}
          >
            <TextField
              id="price"
              sx={{
                flexGrow: 1,
              }}
              label="Price (CAD)"
              required
              type="number"
              min="1"
              value={price}
              onChange={(event) => {
                const value = event.target.value;
                setPrice(value);
              }}
            ></TextField>
            &nbsp;
            {path === "search" ? (
              <TextField
                id="radius"
                type="number"
                min="1"
                label="Radius (in km)"
                value={radius}
                required
                sx={{
                  flexGrow: 1,
                }}
                onChange={(event) => {
                  setRadius(event.target.value);
                }}
              ></TextField>
            ) : (
              ""
            )}
          </div>
          <FormControl sx={{ m: 1, maxWidth: "300px", width: "100%" }}>
            <InputLabel id="allowedCategoryLabel">
              {path === "search" ? "Product Category" : "Allowed Category"}
            </InputLabel>
            <Select
              required
              labelId="allowedCategoryLabel"
              id="allowedCategory"
              multiple
              value={allowedCategory}
              onChange={(event) => {
                const {
                  target: { value },
                } = event;
                setAllowedCategory(
                  typeof value === "string" ? value.split(",") : value
                );
              }}
              input={<OutlinedInput required label="Category" />}
              renderValue={(selected) => selected.join(", ")}
              MenuProps={MenuProps}
            >
              {allowedCategoryLabels.map((cateogory) => (
                <MenuItem key={cateogory} value={cateogory}>
                  <Checkbox checked={allowedCategory.indexOf(cateogory) > -1} />
                  <ListItemText primary={cateogory} />
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <Button
            type="submit"
            variant="contained"
            style={{
              margin: "auto",
              marginTop: "0.5rem",
              gridColumnEnd: "span 2",
              minWidth: "180px",
              maxWidth: "300px",
              width: "100%",
            }}
            endIcon={path === "search" ? <SearchIcon /> : <PostAddIcon />}
          >
            {path === "search" ? "Search" : "Post"}
          </Button>
        </form>
      </div>
      {isLoading && (
        <>
          <div
            style={{
              width: "100%",
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              flexFlow: "column",
            }}
          >
            <CircularProgress sx={{ margin: "auto" }}></CircularProgress>
            <h4>Loading posts</h4>
          </div>
        </>
      )}
      {path === "search" && listings.length > 0 && !isLoading && (
        <div className="listing-container">
          <Listings data={listings}></Listings>
        </div>
      )}
    </>
  );
}
export default CourierForm;
