import React, { useState } from "react";
import { useNavigate } from "react-router";
import axios from "axios";
import cs from "classnames";
import { Alert, Button, FormControl, Snackbar, TextField } from "@mui/material";

import showPasswordIcon from "assets/icons/show-password-icon.svg";
import hidePasswordIcon from "assets/icons/hide-password-icon.svg";
import style from "./index.module.css";
import { encryptPassword } from "utils";

const baseURL = process.env.REACT_APP_BASE_URL;

const SignUp = () => {
  const navigate = useNavigate();
  const [userDetails, setUserDetails] = useState({
    fname: "",
    lname: "",
    email: "",
    password: "",
    cpassword: "",
  });
  const [validation, setValidation] = useState({
    fname: "",
    lname: "",
    email: "",
    password: "",
    cpassword: "",
  });
  const [showPassword, setShowPassword] = useState({ password: false, cpassword: false });
  const [loading, setLoading] = useState(false);
  const [toasterData, setToasterData] = useState({ open: false, status: "", message: "" });

  const handleSignUp = async (e) => {
    e.stopPropagation();
    e.preventDefault();
    const data = {
      firstName: userDetails.fname,
      lastName: userDetails.lname,
      email: userDetails.email,
      password: encryptPassword(userDetails.password),
    };
    setLoading(true);
    try {
      const { status } = await axios.post(`${baseURL}/user/signup`, data);
      if (status === 201) {
        setUserDetails({
          fname: "",
          lname: "",
          email: "",
          password: "",
          cpassword: "",
        });
        setLoading(false);
        navigate("/");
      }
    } catch (e) {
      setLoading(false);
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
        else if (userDetails.cpassword.length > 0 && value !== userDetails.cpassword)
          setValidation({
            ...validation,
            cpassword: "Password doesn't match",
          });
        else
          setValidation({
            ...validation,
            [name]: "",
            cpassword: "",
          });
        break;
      case "cpassword":
        if (value !== userDetails.password)
          setValidation({
            ...validation,
            [name]: "Password doesn't match",
          });
        else
          setValidation({
            ...validation,
            [name]: "",
          });
        break;
      default:
        if (value.length < 0)
          setValidation({ ...validation, [name]: `${name} should be appropriate` });
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
        <div className="card-header fw-semibold">SignUp Page</div>
        <div className="card-body d-flex flex-column">
          <FormControl sx={{ mt: 2, minWidth: 120 }}>
            <TextField
              error={validation.fname !== ""}
              helperText={validation.fname}
              autoFocus
              margin="dense"
              id="fname"
              name="fname"
              label="First Name"
              type="text"
              fullWidth
              value={userDetails.fname}
              onChange={handleChange}
            />
          </FormControl>
          <FormControl sx={{ mt: 2, minWidth: 120 }}>
            <TextField
              error={validation.lname !== ""}
              helperText={validation.lname}
              margin="dense"
              id="lname"
              name="lname"
              label="Last Name"
              type="text"
              fullWidth
              value={userDetails.lname}
              onChange={handleChange}
            />
          </FormControl>
          <FormControl sx={{ mt: 2, minWidth: 120 }}>
            <TextField
              error={validation.email !== ""}
              helperText={validation.email}
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
                type={showPassword.password ? "text" : "password"}
                fullWidth
                value={userDetails.password}
                onChange={handleChange}
              />
              <div className={cs("position-absolute", style.passwordIcon)}>
                {showPassword.password ? (
                  <img
                    src={hidePasswordIcon}
                    onClick={() => setShowPassword({ ...showPassword, password: false })}
                  />
                ) : (
                  <img
                    src={showPasswordIcon}
                    onClick={() => setShowPassword({ ...showPassword, password: true })}
                  />
                )}
              </div>
            </div>
          </FormControl>
          <FormControl sx={{ mt: 2, minWidth: 120 }}>
            <div className="position-relative">
              <TextField
                error={validation.cpassword !== ""}
                helperText={validation.cpassword}
                margin="dense"
                id="cpassword"
                name="cpassword"
                label="Confirm Password"
                type={showPassword.cpassword ? "text" : "password"}
                fullWidth
                value={userDetails.cpassword}
                onChange={handleChange}
              />
              <div className={cs("position-absolute", style.passwordIcon)}>
                {showPassword.cpassword ? (
                  <img
                    src={hidePasswordIcon}
                    onClick={() => setShowPassword({ ...showPassword, cpassword: false })}
                  />
                ) : (
                  <img
                    src={showPasswordIcon}
                    onClick={() => setShowPassword({ ...showPassword, cpassword: true })}
                  />
                )}
              </div>
            </div>
          </FormControl>
          <div className="d-flex flex-column align-items-baseline my-2">
            <p className="m-0">Password must contain:</p>
            <ul className="d-flex flex-column align-items-baseline mb-0">
              <li>a lower case character</li>
              <li>a upper case character</li>
              <li>a number</li>
              <li>a special character</li>
            </ul>
          </div>
          <Button className="mt-2" disabled={loading || disabledCondition} onClick={handleSignUp}>
            Sign Up
          </Button>
        </div>
      </div>
    </div>
  );
};

export default SignUp;
