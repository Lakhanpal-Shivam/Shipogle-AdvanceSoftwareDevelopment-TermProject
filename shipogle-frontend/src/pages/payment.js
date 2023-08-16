import React, { useState } from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import ReactDOM from "react-dom";

import "./payment.css";
import Constants from "../Constants";
import customAxios from "../utils/MyAxios";
import CommonFunctions from "../services/CommonFunction";
import { useLocation, useNavigate } from "react-router-dom";
import Reciept from "./Reciept";

function Payment() {
  const navigate = useNavigate();
  const { state } = useLocation();
  const commFunc = new CommonFunctions();
  const [amount] = useState(state.price);
  const [Currency, setCurrency] = useState("CAD");
  const [CardNumber, setCardNumber] = useState("");
  const [CardExpiryMonth, setCardExpiryMonth] = useState("");
  const [CardExpiryYear, setCardExpiryYear] = useState("");
  const [CardCvv, setCardCvv] = useState("");
  const [CardHolderName, setCardHolderName] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const generateInvoice = (data) => {
    const receiptElement = document.createElement("div");
    ReactDOM.render(<Reciept RecieptData={data} />, receiptElement);

    const htmlContent = `
      <!DOCTYPE html>
      <html>
        <head>
          <title>Payment Reciept</title>
        </head>
        <body>
          ${receiptElement.innerHTML}
        </body>
      </html>
    `;

    const blob = new Blob([htmlContent], { type: "text/html;charset=utf-8" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");

    link.href = url;
    link.download = `Shipogle_Payment_Reciept_${Date.now()}`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();
    const paymentDetails = {
      amount: amount,
      currency: Currency,
      cardNumber: CardNumber,
      cardExpiryMonth: CardExpiryMonth,
      cardExpiryYear: CardExpiryYear,
      cardCvv: CardCvv,
      cardHolderName: CardHolderName,
    };

    setIsLoading(true);

    customAxios.post(Constants.PAYMENT_CHARGE, paymentDetails).then(
      (res) => {
        commFunc.showAlertMessage("payment success", "success", 2000, "bottom");
        customAxios
          .put(Constants.UPDATEPAYMENTSTAT, {
            package_order_id: state.order.id,
          })
          .then(
            (res) => {
              const RecieptData = Object.assign(state.order, paymentDetails);
              commFunc.showAlertMessage(
                "Generating and Downloading Recipt",
                "success",
                2000,
                "top"
              );
              setTimeout(() => {
                generateInvoice(RecieptData);
                setIsLoading(false);
                navigate("/orders");
              }, 500);
            },
            (error) => {
              console.error(error);
              commFunc.showAlertMessage(
                "error while processing payment",
                "error",
                2000,
                "bottom"
              );
            }
          );
      },
      (error) => {
        commFunc.showAlertMessage(
          "error while processing payment",
          "error",
          2000,
          "bottom"
        );
        console.error(error);
      }
    );
  };

  return (
    <>
      <form className="payment-form" onSubmit={handleFormSubmit}>
        <p
          style={{
            textAlign: "left",
            gridColumnEnd: "span 2",
          }}
        >
          Please enter payment Details
        </p>
        <TextField
          sx={{ minWidth: 200, maxWidth: 350, width: "100%" }}
          label="Amount (CAD)"
          type="text"
          className="input-field"
          value={amount}
          disabled
          required
        />

        <TextField
          sx={{ minWidth: 200, maxWidth: 350, width: "100%" }}
          label="Currency"
          className="input-field"
          type="text"
          required
          disabled
          value={Currency}
          onChange={(e) => setCurrency(e.target.value)}
        />

        <TextField
          sx={{ minWidth: 200, maxWidth: 350, width: "100%" }}
          label="Card Number (16 digit)"
          className="input-field"
          type="text"
          required
          placeholder="16 digit (1234567890123456)"
          inputProps={{
            maxLength: 16,
            minLength: 16,
            pattern: "\\d{16}",
          }}
          value={CardNumber}
          onChange={(e) => setCardNumber(e.target.value)}
        />

        <TextField
          sx={{ minWidth: 200, maxWidth: 350, width: "100%" }}
          label="Card Expiry Month"
          className="input-field"
          type="number"
          required
          value={CardExpiryMonth}
          inputProps={{ max: 12, min: 1 }}
          onChange={(e) => setCardExpiryMonth(e.target.value)}
        />

        <TextField
          sx={{ minWidth: 200, maxWidth: 350, width: "100%" }}
          label="Card Expiry Year"
          className="input-field"
          type="number"
          required
          value={CardExpiryYear}
          inputProps={{ min: new Date().getFullYear() }}
          onChange={(e) => setCardExpiryYear(e.target.value)}
        />

        <TextField
          sx={{ minWidth: 200, maxWidth: 350, width: "100%" }}
          label="Card CVV (3 Digit)"
          className="input-field"
          type="text"
          required
          placeholder="e.g 123"
          value={CardCvv}
          inputProps={{
            maxLength: 3,
            minLength: 3,
            pattern: "\\d{3}",
          }}
          onChange={(e) => setCardCvv(e.target.value)}
        />

        <TextField
          sx={{ minWidth: 200, maxWidth: 350, width: "100%" }}
          label="Card Holder Name"
          className="input-field"
          type="text"
          required
          value={CardHolderName}
          onChange={(e) => setCardHolderName(e.target.value)}
        />

        <Button
          style={{
            margin: "auto",
            marginTop: "0.5rem",
            gridColumnEnd: "span 2",
            minWidth: "180px",
            maxWidth: "300px",
            width: "100%",
          }}
          disabled={isLoading}
          type="submit"
          variant="contained"
          color="primary"
        >
          Pay
        </Button>
      </form>
      <br></br>
    </>
  );
}

export default Payment;
