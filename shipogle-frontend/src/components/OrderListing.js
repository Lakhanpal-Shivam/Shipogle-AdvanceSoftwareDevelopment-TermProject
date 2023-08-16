import { Avatar, Button } from "@mui/material";
import * as React from "react";
import { Link, useNavigate } from "react-router-dom";

import Data from "../pages/data";

import "./OrderListing.css";
import CommonFunctions from "../services/CommonFunction";
export default function OrderListing(props) {
  const commFunc = new CommonFunctions();
  const demoData = new Data();
  const [listings, setListing] = React.useState([]);
  const navigate = useNavigate();
  //delete later and all instances
  const paid_orders = window.localStorage.getItem("paid_orders");
  const routeToPayment = (order) => {
    navigate(`/courier/payment/${order?.id}`, {
      state: { price: order.driverRoute.price, order: order },
    });
  };
  const routeTo = (order) => {
    navigate(`/orders/details/${props.status}/${order?.id}`, {
      state: { order: order },
    });
  };
  React.useEffect(() => {
    const cards = [];
    props.data.forEach((order) => {
      cards.push(
        <div className="order-listing-container" key={order?.id}>
          <Avatar sx={{ width: 46, height: 46, margin: "0.5rem 0.6rem" }}>
            {commFunc.getDriverInitials(
              order?.deliverer?.first_name + " " + order?.deliverer?.last_name
            )}
          </Avatar>
          <div
            style={{
              borderRight: "1px solid rgba(0,0,0,0.2)",
              height: "64px",
              marginRight: "8px",
            }}
          ></div>
          <div className="order-details">
            <div className="order-listing-heading">
              &nbsp;
              <span style={{ flexGrow: 1 }}>
                {order?.driverRoute.driverName}
              </span>
              <span>
                Created at:{" "}
                {order?.created_at
                  ? `${order?.created_at[1]}/${order?.created_at[2]}/${order?.created_at[0]}`
                  : "Nodate"}
              </span>
            </div>
            <hr></hr>
            <div className="order-summary">
              <p style={{ flexGrow: 1, margin: "8px" }}>
                {`From: ${order?._package?.pickup_address} , To: ${order?._package?.drop_address}`}
              </p>
              <p style={{ margin: "8px" }}>{`Delivery Date: ${new Date(
                order?.driverRoute?.dropoffDate
              ).toLocaleDateString()}`}</p>
            </div>
          </div>
          <div className="action-button" style={{ marginLeft: "8px" }}>
            {props.status === "pending" &&
              order.paymentStatus === 0 &&
              !paid_orders.includes(order.id) && (
                <Button
                  onClick={() => {
                    routeToPayment(order);
                  }}
                >
                  Pay
                </Button>
              )}
            {props.status === "canceled" &&
              order.paymentStatus === -1 &&
              !paid_orders.includes(order.id) && <Button>Refunded</Button>}
            <Button
              color={props.color}
              onClick={() => {
                routeTo(order);
              }}
            >
              {props.actionButtonText ? props.actionButtonText : "View"}
            </Button>
          </div>
        </div>
      );
    });
    setListing(cards);
  }, []);
  return (
    <>
      {listings.length > 0 ? (
        listings
      ) : (
        <>
          <h4 style={{ textAlign: "center" }}>No orders for this category</h4>
        </>
      )}
    </>
  );
}
