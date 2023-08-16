import React from "react";
import Constants from "../Constants";

const apiKey = Constants.API_KEY;
function StaticMap({ pickup, dropOff, width, height, zoom, trackedLocation }) {
  const buildMarkers = () => {
    const markerA = `markers=color:0xFF0000|label:${
      pickup[2] ? pickup[2] : "No label"
    }|${pickup[0]},${pickup[1]}`;
    const markerB = `markers=color:0xFF0000|label:${
      dropOff[2] ? dropOff[2] : "No label"
    }|${dropOff[0]},${dropOff[1]}`;
    if (trackedLocation) {
      const markerC = `markers=color:0x00FF00|label:P|${trackedLocation.lat},${trackedLocation.lng}`;
      return `${markerC}`;
    }
    return `${markerA}&${markerB}`;
  };

  const buildStaticMapUrl = () => {
    const baseApiUrl = "https://maps.googleapis.com/maps/api/staticmap?";
    const path = `path=color:0x0000ff|weight:5|${pickup[0]},${pickup[1]}|${dropOff[0]},${dropOff[1]}`;
    const markers = buildMarkers();
    const size = `size=${width}x${height}`;
    const mapZoom = `zoom=${zoom}`;
    const apiKeyParam = `key=${apiKey}`;
    let url;
    if (trackedLocation) {
      url = `${baseApiUrl}${size}&${markers}&2&${apiKeyParam}`;
    } else
      url = `${baseApiUrl}${size}&${path}&${markers}&${mapZoom}&${apiKeyParam}`;
    return url;
  };

  return (
    <div>
      <img src={buildStaticMapUrl()} alt="Static map" />
    </div>
  );
}

export default StaticMap;
