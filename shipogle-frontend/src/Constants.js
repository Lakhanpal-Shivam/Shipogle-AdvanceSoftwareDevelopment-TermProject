const BASE_URL = "http://csci5308vm9.research.cs.dal.ca:8080";
const SOCKET_BASE_URL = "ws://csci5308vm9.research.cs.dal.ca:8080";

const APIS = {
  API_REGISTER: `${BASE_URL}/register`,
  API_LOGIN: `${BASE_URL}/login`,
  API_CHAT: `${BASE_URL}/chat`,
  API_USER_INFO_FROM_TOKEN: `${BASE_URL}/user_info`,
  API_NOTIFICATIONS: `${BASE_URL}/notifications`,
  API_FORGOT_PWD: `${BASE_URL}/forgotpassword`,
  API_RESET_PWD: `${BASE_URL}/changepassword`,
  API_USER: `${BASE_URL}/user`,
  SOCKET_CHAT: `${SOCKET_BASE_URL}/chatSocket`,
  SOCKET_NOTIFICATIONS: `${SOCKET_BASE_URL}/notificationSocket`,
  PAYMENT_CHARGE: `${BASE_URL}/payment/charge`,
  ORDERS: `${BASE_URL}/package/order/getall`,
  CANCELORDER: `${BASE_URL}/package/order/cancel`,
  STARTORDER: `${BASE_URL}/package/order/start`,
  ENDORDER: `${BASE_URL}/package/order/end`,
  ACCEPTREQUEST: `${BASE_URL}/package/request/accept`,
  REJECTREQUEST: `${BASE_URL}/package/request/reject`,
  GETREQUESTS: `${BASE_URL}/package/request/getall`,
  DRIVERROUTE: `${BASE_URL}/driverRoutes`,
  SENDPACKAGEREQUEST: `${BASE_URL}/package/request/send`,
  CREATEPACKAGE: `${BASE_URL}/package/create`,
  GETDRIVERROUTES: `${BASE_URL}/driverRoutesByDriverId`,
  UPDATELOCATION: `${BASE_URL}/user/location/put`,
  GETALLORDERSFROMDRIVERROUTEID: `${BASE_URL}/package/order/getAllDelivererRouteOrders`,
  GETUSERNOTIFICATION: `${BASE_URL}/notifications/get`,
  POSTRATING: `${BASE_URL}/rating/post`,
  POSTISSUE: `${BASE_URL}/issue/post`,
  GETDELIVERERRATINGS: `${BASE_URL}/rating/deliverer/getall`,
  GETRATINGS: `${BASE_URL}/rating/posted/getall`,
  GETISSUES: `${BASE_URL}/issue/getall`,
  UPDATEPAYMENTSTAT: `${BASE_URL}/package/order/recordPayment`,
  API_KEY: "AIzaSyDOeSvsNto_ch6nczKaNY9Rxo9pJHNcB9s",
};

export default APIS;
export { BASE_URL };
