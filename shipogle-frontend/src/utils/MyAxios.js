import axios from "axios";
import Cookies from "js-cookie";

const customAxios = axios.create({
  timeout: 10000,
});

const requestHandler = (request) => {
  const token = Cookies.get("authToken");

  if (token) {
    request.headers.Authorization = `Bearer ${token}`;
  }
  return request;
};

const responseHandler = (response) => {
  if (response?.status === 401 || response?.status === 403) {
    window.location = "/login";
  }

  return response;
};

const errorHandler = (error) => {
  if (error?.response?.status === 401 || error?.response?.status === 403) {
    window.location = "/login";
  }
  return Promise.reject(error);
};

customAxios.interceptors.request.use(
  (request) => {
    return requestHandler(request);
  },
  (error) => errorHandler(error)
);

customAxios.interceptors.response.use(
  (response) => responseHandler(response),
  (error) => errorHandler(error)
);

export default customAxios;

// Reference: https://blog.clairvoyantsoft.com/intercepting-requests-responses-using-axios-df498b6cab62
