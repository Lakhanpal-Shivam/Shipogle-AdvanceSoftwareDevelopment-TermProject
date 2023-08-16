import React from "react";
import Header from "../components/Header";
import "./Reciept.css";
export default function Reciept({ RecieptData }) {
  const date = new Date().toLocaleDateString();
  return (
    <div style={{ border: "1px solid black", margin: "1rem" }}>
      <center>
        <Header title="S H I P O G L E" info="Invoice"></Header>
        <table className="payment-details-table">
          <tbody>
            <tr className="table-row">
              <td className="payment-td type">Sender Name</td>
              <td>&nbsp;&nbsp;</td>
              <td className="payment-td info">{`${RecieptData?.sender?.first_name} ${RecieptData?.sender?.last_name}`}</td>
            </tr>
            <tr className="table-row">
              <td className="payment-td type">Deliverer Name</td>
              <td>&nbsp;&nbsp;</td>
              <td className="payment-td info">{`${RecieptData?.driverRoute?.driverName}`}</td>
            </tr>
            <tr className="table-row">
              <td className="payment-td type">Deliverer Email</td>
              <td>&nbsp;&nbsp;</td>
              <td className="payment-td info">
                {RecieptData?.deliverer?.email}
              </td>
            </tr>
            <tr className="table-row">
              <td className="payment-td type">Delivery from</td>
              <td>&nbsp;&nbsp;</td>
              <td className="payment-td info">{`${RecieptData?.driverRoute?.sourceAddress}`}</td>
            </tr>
            <tr className="table-row">
              <td className="payment-td type">Delivery To</td>
              <td>&nbsp;&nbsp;</td>
              <td className="payment-td info">{`${RecieptData?.driverRoute?.destinationAddress}`}</td>
            </tr>
            <tr className="table-row">
              <td className="payment-td type">Total Payment</td>
              <td>&nbsp;&nbsp;</td>
              <td className="payment-td info">${RecieptData?.amount} CAD</td>
            </tr>
            <tr className="table-row">
              <td className="payment-td type">Payment Status</td>
              <td>&nbsp;&nbsp;</td>
              <td className="payment-td info">Success</td>
            </tr>
            <tr className="table-row">
              <td className="payment-td type">Payment Date</td>
              <td>&nbsp;&nbsp;</td>
              <td className="payment-td info">{date}</td>
            </tr>
          </tbody>
        </table>
      </center>
    </div>
  );
}
