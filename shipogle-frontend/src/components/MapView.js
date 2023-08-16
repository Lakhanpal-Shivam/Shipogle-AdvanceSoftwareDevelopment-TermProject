import React, { useCallback, useEffect, useRef, useState } from "react";
import CommonFunctions from "../services/CommonFunction.js";
import { CircularProgress } from "@mui/material";

//const apiKey = "AIzaSyBPtYm-CJPPW4yO9njM-e9YBWyp-DwIODM";

const MapView = ({ locations }) => {
  const commFunc = useCallback(() => new CommonFunctions(), []);
  const mapRef = useRef(null);
  const [googleMaps, setGoogleMaps] = useState(null);
  const [map, setMap] = useState(null);
  const [markers, setMarkers] = useState([]);
  const [center, setCenter] = useState({});
  const [currentLocationMarker, setCurrentLocationMarker] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const getCurrentLocation = () => {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const userLocation = {
          lat: position.coords.latitude,
          lng: position.coords.longitude,
        };
        setCenter(userLocation);
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
    getCurrentLocation();
    if (window.google.maps) {
      setGoogleMaps(window.google.maps);
    } else {
      commFunc.showAlertMessage(
        ("there was an error loading maps", "error", 3000, "bottom")
      );
    }
  }, [commFunc]);

  useEffect(() => {
    if (googleMaps && center) {
      const mapOptions = {
        zoom: 18,
        center: new googleMaps.LatLng(center?.lat, center?.lng),
      };

      const newMap = new googleMaps.Map(mapRef.current, mapOptions);
      setMap(newMap);

      if (currentLocationMarker) {
        currentLocationMarker.setMap(null);
      }

      const newCurrentLocationMarker = new googleMaps.Marker({
        position: new googleMaps.LatLng(
          center?.lat ? center?.lat : 44.637073,
          center?.lng ? center?.lng : -63.589928
        ),
        map: newMap,
        label: "You location",
        icon: {
          url: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png",
        },
      });

      setCurrentLocationMarker(newCurrentLocationMarker);
    }
  }, [googleMaps, center, currentLocationMarker]);

  const updateMarkers = useCallback(() => {
    markers.forEach((marker) => marker.setMap(null));

    const newMarkers = locations.map((location) => {
      const marker = new googleMaps.Marker({
        position: new googleMaps.LatLng(
          location.pickupLocationCoords[0],
          location.pickupLocationCoords[1]
        ),
        map,
        label: location.sourceCity,
      });

      return marker;
    });

    setMarkers(newMarkers);
  }, [googleMaps?.LatLng, googleMaps?.Marker, locations, map, markers]);

  useEffect(() => {
    if (map) {
      updateMarkers();
      setIsLoading(false);
    }
  }, [map, locations, updateMarkers]);

  return (
    <>
      <div ref={mapRef} style={{ width: "100%", height: "500px" }} />{" "}
      {isLoading && <CircularProgress></CircularProgress>}
    </>
  );
};

export default MapView;
