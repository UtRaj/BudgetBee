import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router";

import { loginUser } from "store/actions/auth";
import showPasswordIcon from "assets/icons/show-password-icon.svg";
import hidePasswordIcon from "assets/icons/hide-password-icon.svg";
import style from "./index.module.css";
import cs from "classnames";
import { Alert, Button, FormControl, Snackbar, TextField } from "@mui/material";

const SignIn = () => {
  const navigate = useNavigate();
  const [userDetails, setUserDetails] = useState({
    email: "",
    password: "",
  });
  const [validation, setValidation] = useState({
    email: "",
    password: "",
  });
  const [showPassword, setShowPassword] = useState(false);
  const [toasterData, setToasterData] = useState({ open: false, status: "", message: "" });
  const dispatch = useDispatch();
  const {
    user: { loading },
  } = useSelector((state) => state.auth);

  const handleSignIn = async (e) => {
    e.stopPropagation();
    e.preventDefault();
    try {
      const { status } = await dispatch(loginUser(userDetails));
      if (status) {
        setUserDetails({
          email: "",
          password: "",
        });
        navigate("/dashboard");
      }
    } catch (e) {
      setToasterData({ open: true, status: "error", message: e.response.data });
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUserDetails({ ...userDetails, [name]: value });
    checkValidation(name, value);
  };

  const checkValidation = (name, value) => {
    const regex = /^(?=.*\d)(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
    if (value === "") {
      setValidation({
        ...validation,
        [name]: `Please enter ${name} properly`,
      });
    } else {
      setValidation({
        ...validation,
        [name]: "",
      });
    }
    switch (name) {
      case "password":
        if (!regex.test(value) && value.length > 0)
          setValidation({
            ...validation,
            [name]: "Enter Password Properly",
          });
        else
          setValidation({
            ...validation,
            [name]: "",
          });
        break;
      case "name":
      default:
        if (value.length < 0)
          setValidation({ ...validation, [name]: "Email should be appropriate" });
        else
          setValidation({
            ...validation,
            [name]: "",
          });
        break;
    }
  };

  const disabledCondition =
    Object.values(userDetails).some((ele) => ele.length === 0) ||
    Object.values(validation).some((ele) => ele);

  return (
    <div className="d-flex justify-content-center align-items-center h-100">
      <Snackbar
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
        open={toasterData.open}
        onClose={() => {
          setToasterData({ open: false, status: "", message: "" });
        }}
        autoHideDuration={3000}>
        {toasterData.status ? (
          <Alert severity={toasterData.status}>{toasterData.message}</Alert>
        ) : (
          toasterData.message
        )}
      </Snackbar>
      <div className={cs("card", style.cardStyle)}>
        <div className="card-header fw-semibold">Login Page</div>
        <div className="card-body d-flex flex-column">
          <FormControl sx={{ mt: 2, minWidth: 120 }}>
            <TextField
              error={validation.email !== ""}
              helperText={validation.email}
              autoFocus
              margin="dense"
              id="email"
              name="email"
              label="Email"
              type="email"
              fullWidth
              value={userDetails.email}
              onChange={handleChange}
            />
          </FormControl>
          <FormControl sx={{ mt: 2, minWidth: 120 }}>
            <div className="position-relative">
              <TextField
                error={validation.password !== ""}
                helperText={validation.password}
                margin="dense"
                id="password"
                name="password"
                label="Password"
                type={showPassword ? "text" : "password"}
                fullWidth
                value={userDetails.password}
                onChange={handleChange}
              />
              <div className={cs("position-absolute", style.passwordIcon)}>
                {showPassword ? (
                  <img src={hidePasswordIcon} onClick={() => setShowPassword(false)} />
                ) : (
                  <img src={showPasswordIcon} onClick={() => setShowPassword(true)} />
                )}
              </div>
            </div>
          </FormControl>
          <Button className="mt-2" disabled={loading || disabledCondition} onClick={handleSignIn}>
            Log in
          </Button>
        </div>
      </div>
    </div>
  );
};

export default SignIn;
