package com.shipogle.app.utility;

public class Const {
    public static final int UNAUTHORIZED_ERROR_CODE = 401;
    public static final String SECRET_KEY = "2A462D4A614E645267556B58703273357638792F423F4528472B4B6250655368";
    public static final String URL_FRONTEND = "http://csci5308vm9.research.cs.dal.ca:3000";
    public static final String URL_BACKEND = "http://csci5308vm9.research.cs.dal.ca:8080";
    public static final int TOKEN_EXPIRATION_TIME = 1440; // (60*24) minutes = 1 day
    public static final int RANDOM_LOWER_BOUND = 1000; // Lower bound to generate 4 digit random number
    public static final int RANDOM_UPPER_BOUND = 10000; // Upper bound to generate 4 digit random number
    public static final String[] AUTH_EXCEPT_PATHS = {"/" ,"/register", "/verification", "/changepassword", "/forgotpassword", "/login", "/driverRoutes", "/ShipoglePay", "/chatSocket/*", "notificationSocket/*"};
}
