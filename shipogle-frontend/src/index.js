import { BrowserRouter, Routes, Route } from "react-router-dom";
import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
import Login from "./pages/Login";
import Registration from "./pages/Registration";
import RegistrationForm from "./pages/RegistrationForm";
import RegSuccessful from "./pages/RegSuccessful";
import CourierForm from "./pages/CourierForm";
import EditProfile from "./pages/EditProfile";
import Inbox from "./pages/Inbox/Inbox";
import OrderDetails from "./components/OrderDetails";
import Orders from "./pages/Orders";
import Payment from "./pages/payment";
import CurrentDelivery from "./components/CurrentDelivery";
import CourierDetails from "./pages/CourierDetails";
import ForgotPwd from "./pages/ForgotPwd";
import PackageRequests from "./components/PackageRequests";
import StartEndDelivery from "./pages/StartEndDelivery";
import Feedback from "./pages/Feedback";
import Issue from "./pages/Issues";
import IssueLisitng from "./pages/IssueListing";

const root = ReactDOM.createRoot(document.getElementById("root"));

root.render(
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<App />}>
        <Route path="/login" element={<Login />} />
        <Route path="/registration" element={<Registration />} />
        <Route path="/courier/search" element={<CourierForm path={1} />} />
        <Route path="/courier/offer" element={<CourierForm key={2} />} />
        <Route path="/registration/form" element={<RegistrationForm />} />
        <Route path="/registration/success" element={<RegSuccessful />} />
        <Route path="/user/editprofile" element={<EditProfile />} />

        <Route path="/inbox" element={<Inbox></Inbox>}></Route>

        <Route path="/orders" element={<Orders></Orders>}></Route>
        <Route
          path="/forgotpwd"
          element={<ForgotPwd path={1}></ForgotPwd>}
        ></Route>
        <Route
          path="/forgotpwd/reset/:token"
          element={<ForgotPwd path={2}></ForgotPwd>}
        ></Route>
        <Route
          path="/orders/details/:status/:orderId"
          element={<OrderDetails></OrderDetails>}
        ></Route>
        <Route
          path="/courier/payment/:id"
          element={<Payment></Payment>}
        ></Route>
        <Route
          path="/myrequests"
          element={<PackageRequests></PackageRequests>}
        ></Route>
        <Route
          path="/deliveries"
          element={<CurrentDelivery></CurrentDelivery>}
        ></Route>

        <Route
          path="/courier/details/:id"
          element={<CourierDetails></CourierDetails>}
        />
        <Route
          path="/order/startend"
          element={<StartEndDelivery></StartEndDelivery>}
        ></Route>
        <Route path="/feedback" element={<Feedback></Feedback>}></Route>
        <Route path="/issue" element={<Issue></Issue>}></Route>
        <Route path="/issues" element={<IssueLisitng></IssueLisitng>}></Route>
      </Route>
    </Routes>
  </BrowserRouter>
);

// If you want to start measuring performance in your app, pass a function
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
