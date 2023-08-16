import { createContext, useState, useEffect } from "react";

export const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const login = () => {
    setIsAuthenticated(true);
  };

  // useEffect(() => {
  //   const user_id = window.localStorage.getItem("user_id");
  //   const user_name = window.localStorage.getItem("user_name");
  //   const authToken = window.localStorage.getItem("authToken");

  //   if (user_id && user_name && authToken)
  //     setIsAuthenticated(true);
  // }, []);

  const logout = () => {
    window.localStorage.removeItem("user_id");
    window.localStorage.removeItem("user_name");
    window.localStorage.removeItem("authToken");
    setIsAuthenticated(false);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}
