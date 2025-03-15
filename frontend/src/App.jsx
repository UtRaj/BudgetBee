import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Navigate, Route, Routes } from "react-router";
import Cookies from "js-cookie";

import "./App.css";
import AuthLayout from "layout/AuthLayout";
import PrivateLayout from "layout/PrivateLayout";
import RoutesList from "./routes";

const App = () => {
  const [isLogin, setIsLogin] = useState(Cookies.get("token"));
  const { user } = useSelector((store) => store.auth);

  useEffect(() => {
    const loginStatus = Cookies.get("token");
    setIsLogin(!!loginStatus);
  }, [user]);

  const renderRoutes = () => {
    const renderRoute = (Component, layout) => {
      if (Component) {
        switch (layout) {
          case "private":
            return isLogin ? (
              <PrivateLayout>
                <Component />
              </PrivateLayout>
            ) : (
              <Navigate to="/" />
            );
          case "auth":
          default:
            return isLogin ? (
              <Navigate to="/dashboard" />
            ) : (
              <AuthLayout>
                <Component />
              </AuthLayout>
            );
        }
      }
      return null;
    };

    return RoutesList.map((route) => {
      return (
        <Route
          key={route.name}
          path={route.path}
          element={renderRoute(route.component, route.layout)}
        />
      );
    });
  };

  return (
    <div className="App">
      <Routes>{renderRoutes()}</Routes>
    </div>
  );
};

export default App;
