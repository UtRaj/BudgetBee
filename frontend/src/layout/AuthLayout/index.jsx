import React from "react";
import cs from "classnames";

import styles from "./index.module.css";
import { useLocation, useNavigate } from "react-router";

const AuthLayout = ({ children }) => {
  const { pathname } = useLocation();
  const navigate = useNavigate();

  return (
    <div className={cs("d-flex flex-column my-0 px-4", styles.background)}>
      <nav className="navbar navbar-light justify-content-between text-white">
        <p className={cs("h3", styles.pointer)} onClick={() => navigate("/")}>
          FinTrack
        </p>
        <div>
          {pathname === "/" ? (
            <button
              type="button"
              className="btn btn-primary text-white"
              onClick={() => navigate("/signup")}>
              Sign up
            </button>
          ) : (
            <button
              type="button"
              className="btn btn-primary text-white"
              onClick={() => navigate("/")}>
              Log in
            </button>
          )}
        </div>
      </nav>
      {children}
    </div>
  );
};

export default AuthLayout;
