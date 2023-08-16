import {
  Button,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  CircularProgress,
} from "@mui/material";
import * as React from "react";
import customAxios from "../utils/MyAxios";
import Constants from "../Constants";
import Data from "../pages/data";
import CommonFunctions from "../services/CommonFunction";

export default function MyRides() {
  const demoData = new Data();
  const [requests, setRequests] = React.useState([]);
  const [isLoading, setIsLoading] = React.useState(true);
  const commFunc = new CommonFunctions();
  const accept = (request) => {
    const body = { package_request_id: request.package_request_id };
    customAxios.post(Constants.ACCEPTREQUEST, body).then(
      (res) => {},
      (error) => {
        console.error(error);
      }
    );
  };

  const reject = (request) => {
    const body = { package_request_id: request.package_request_id };
    customAxios.post(Constants.REJECTREQUEST, body).then(
      (res) => {
        commFunc.showAlertMessage("Request rejected", "info", 3000, "bottom");
        alert("request rejected");
      },
      (error) => {
        console.error(error);
      }
    );
  };

  React.useEffect(() => {
    customAxios.get(Constants.GETREQUESTS).then(
      (res) => {
        const requests = [];
        demoData.requests.forEach((request) => {
          requests.push(
            <div>
              <Card
                sx={{ maxWidth: "500px", width: "100%", margin: "2rem auto" }}
              >
                <CardHeader
                  title="Courier Requests"
                  subheader={"RideID: " + request.order_id}
                ></CardHeader>
                <hr
                  style={{
                    marginTop: "-0.5rem",
                    borderTop: "1px solid rgba(0,0,0,0.4)",
                    width: "95%",
                    margin: "-0.5rem auto 0.5rem auto",
                  }}
                ></hr>
                <CardContent>
                  <h3 style={{ margin: "0rem auto" }}>Details</h3>
                  <ul style={{ color: "rgba(0,0,0,0.6)" }}>
                    <li>Source city: {request.source_city}</li>
                    <li>Destination City: {request.destination_city}</li>
                    <li>Pickup Date: {request.pickupDate}</li>
                  </ul>
                  <h4 style={{ margin: "0rem auto" }}>Request By</h4>
                  <ul style={{ color: "rgba(0,0,0,0.6)" }}>
                    <li>Username: {request.sender_name}</li>
                    <li>ID: {request.sender_id}</li>
                  </ul>
                </CardContent>
                <CardActions>
                  <Button
                    onClick={() => {
                      accept(request);
                    }}
                  >
                    Accept
                  </Button>
                  <Button
                    color="error"
                    onClick={() => {
                      reject(request);
                    }}
                  >
                    Reject
                  </Button>
                </CardActions>
              </Card>
            </div>
          );
        });
        setRequests(requests);
        setIsLoading(false);
      },
      (error) => {
        console.error(error);
      }
    );
  }, []);
  return (
    <>
      {isLoading && (
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            flexFlow: "column",
            height: "300px",
            width: "100%",
          }}
        >
          <CircularProgress></CircularProgress>
          <p>Loading Requests</p>
        </div>
      )}
      {!isLoading && <>{requests}</>}
    </>
  );
}
