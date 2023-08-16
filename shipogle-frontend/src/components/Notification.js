import React from "react";
import { Link } from "react-router-dom";

// notifType 0 -> You have a chat message
// notifType 1 -> You have a match (either with a sender or deliverer).
function getRoute(notifType) {
  if (notifType === 0) {
    return "/inbox";
  } else {
    return "/";
  }
}

export default function Notification(props) {
  const [notifType, setNotifType] = React.useState(0);

  const route = getRoute(notifType);

  return (
    <div className="notif">
      <h4> {props.notificationName}</h4>
      <Link to={route}>
        <p> {props.notificationAction}</p>
      </Link>
    </div>
  );
}
