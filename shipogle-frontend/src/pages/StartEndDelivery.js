import React, { useState, useEffect, useCallback } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Constants from "../Constants";
import customAxios from "../utils/MyAxios";
import { Button, Card, CircularProgress, TextField } from "@mui/material";
import CommonFunctions from "../services/CommonFunction";

const StartEndDelivery = () => {
  const commFunc = useCallback(() => new CommonFunctions(), []);
  const [isLoading, setIsLoading] = useState(true);
  const { state } = useLocation();
  const [orders, setOrders] = useState([]);
  const [canceledOrders, setCanceledOrders] = useState([]);
  const [codes, setCodes] = useState("");
  const routeId = state.id;
  const navigate = useNavigate();

  const sendNotification = useCallback(
    (body) => {
      customAxios.post(Constants.API_NOTIFICATIONS, body).then(
        (res) => {},
        (error) => {
          console.error(error);
          commFunc.showAlertMessage(
            "Request sent but failed to send driver a Notification!",
            "error",
            3000,
            "bottom"
          );
        }
      );
    },
    [commFunc]
  );

  const createNotificationBodyForNonCancelledOrders = useCallback(
    (orders) => {
      orders.forEach((order) => {
        let body = {
          userId: order.sender.user_id,
        };
        if (!order.started) {
          body["title"] = "Pickup Code";
          body[
            "message"
          ] = `Please provide pickup code ${order.pickup_code} to ${order.driverRoute.driverName} to start delivery`;
        } else if (order.started && !order.delivered) {
          body["title"] = "Drop Code";
          body[
            "message"
          ] = `Please provide pickup code ${order.drop_code} to ${order.driverRoute.driverName} to end delivery`;
        }
        sendNotification(body);
      });
    },
    [sendNotification]
  );

  const fetchOrders = useCallback(() => {
    customAxios(
      Constants.GETALLORDERSFROMDRIVERROUTEID + "?driver_route_id=" + routeId
    ).then(
      (res) => {
        const nonCanceledOrders = res.data.filter((order) => !order.canceled);
        const canceledOrders = res.data.filter((order) => order.canceled);
        setOrders(nonCanceledOrders);
        setCanceledOrders(canceledOrders);
        createNotificationBodyForNonCancelledOrders(nonCanceledOrders);
        setIsLoading(false);
      },
      (error) => {
        commFunc.showAlertMessage(
          "error while fetching details",
          "error",
          3000,
          "bottom"
        );
        console.error(error);
      }
    );
  }, [commFunc, createNotificationBodyForNonCancelledOrders, routeId]);

  const submit = (e) => {
    e.preventDefault();
    const code = codes.split(",");

    const api =
      orders[0].started && !orders[0].canceled
        ? Constants.ENDORDER
        : Constants.STARTORDER;
    const requests = [];
    for (let a = 0; a < code.length; a++) {
      let body = {
        order_id: orders[a].id,
      };
      const codeString =
        orders[0].started && !orders[0].canceled ? "drop_code" : "pickup_code";
      body[codeString] = parseInt(code[a]);
      const request = customAxios.put(api, body).then(
        (res) => {
          commFunc.showAlertMessage("code success", "success", 1000, "bottom");
          createBodyNotificationBodyForActionOnOrder(orders[a]);
          createNotificationBodyForNonCancelledOrders(orders[a]);
          sendNotification(body);
        },
        (error) => {
          console.error(error);
          commFunc.showAlertMessage(
            `${body.pickup_code} code failed`,
            "error",
            2000,
            "bottom"
          );
        }
      );
      requests.push(request);
    }
    waitAndRouteToCurrentDeliveries(requests);
  };

  const waitAndRouteToCurrentDeliveries = async (promises) => {
    try {
      await Promise.all(promises);
    } catch (error) {
      commFunc.showAlertMessage(
        "an error occured while verifying codes",
        "error",
        3000,
        "bottom"
      );
    } finally {
      navigate("/deliveries");
    }
  };

  const createBodyNotificationBodyForActionOnOrder = (order) => {
    let codeString;
    const body = {
      userId: order.sender.user_id,
      title: codeString === "drop_code" ? "delivery ended" : "delivery started",
      message:
        codeString === "drop_code"
          ? `${order.driverRoute.driverName} has delivered your courier`
          : `${order.driverRoute.driverName} has picked up your courier`,
    };
    return body;
  };

  useEffect(() => {
    fetchOrders();
  }, [fetchOrders]);

  return (
    <Card style={{ maxWidth: "400px", margin: "2rem auto", padding: "1rem" }}>
      {isLoading ? (
        <CircularProgress sx={{ margin: "1rem auto" }} />
      ) : (
        <form
          onSubmit={(event) => {
            submit(event);
          }}
          style={{
            display: "flex",
            flexFlow: "column",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          <h4>Start/End Order Form</h4>
          <TextField
            required
            sx={{ width: "100%", margin: "0.5rem auto", flexGrow: 1 }}
            label="Current Delivery ID"
            value={routeId}
            disabled
          />
          <TextField
            required
            type="text"
            onChange={(event) => {
              setCodes(event.target.value);
            }}
            sx={{
              minWidth: "400px",
              width: "100%",
              margin: "0.5rem auto",
              flexGrow: 1,
            }}
            placeholder="e.g 1234,5678,7890"
            label={
              orders[0].started && !orders[0].canceled
                ? `Enter Drop codes`
                : `Enter Pickup Codes `
            }
          />
          <Button type="submit" variant="contained" sx={{ margin: "auto" }}>
            {orders[0].started && !orders[0].canceled
              ? "End Delivery"
              : "Start Delivery"}
          </Button>
        </form>
      )}
    </Card>
  );
};

export default StartEndDelivery;
