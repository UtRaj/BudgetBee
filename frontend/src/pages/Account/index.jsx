import { Alert, Button, Snackbar } from "@mui/material";
import axios from "axios";
import React, { useEffect, useState } from "react";
import Cookies from "js-cookie";

import TableComponent from "components/Table";
import AccountModal from "components/AccountModal";

const baseURL = process.env.REACT_APP_BASE_URL;

function Account() {
  const [open, setOpen] = React.useState(false);
  const [isEdit, setIsEdit] = useState(false);
  const [rowData, setRowData] = useState([]);
  const [toasterData, setToasterData] = useState({ open: false, status: "", message: "" });
  const [modalFormValue, setModalFormValue] = useState({
    accountType: "",
    accountNumber: "",
    balance: "",
    bankName: "",
  });
  const [modalValidation, setModalValidation] = useState({
    accountType: "",
    accountNumber: "",
    balance: "",
    bankName: "",
  });

  useEffect(() => {
    fetchData();
  }, []);

  const disabledCondition = isEdit
    ? Object.values(modalFormValue).some((ele) => ele.length === 0) ||
      Object.values(modalValidation).some((ele) => ele)
    : modalFormValue.accountNumber.length === 0 ||
      modalFormValue.bankName.length === 0 ||
      modalValidation.accountNumber ||
      modalValidation.bankName;

  const checkValidation = (name, value) => {
    if (value === "") {
      setModalValidation({
        ...modalValidation,
        [name]: `Please enter ${name} properly`,
      });
    } else {
      setModalValidation({
        ...modalValidation,
        [name]: "",
      });
    }
    switch (name) {
      case "accountNumber":
        if (value.length < 10) {
          setModalValidation({
            ...modalValidation,
            [name]: "Index number should be 10 digits",
          });
        }
        break;
      case "bankName":
        if (!value.match(/^[a-zA-Z]+$/)) {
          setModalValidation({
            ...modalValidation,
            [name]: "Bank name should be only alphabets",
          });
        }
        break;
      default:
        break;
    }
  };
  const handleValueChange = (event) => {
    if (
      modalFormValue.accountType === "Debit" &&
      event.target.name === "balance" &&
      !event.target.value.match(/^[0-9]+$/)
    ) {
      return;
    }
    setModalFormValue({ ...modalFormValue, [event.target.name]: event.target.value });
    checkValidation(event.target.name, event.target.value);
  };
  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setIsEdit(false);
    setModalFormValue({
      accountType: "",
      accountNumber: "",
      balance: "",
      bankName: "",
    });
    setModalValidation({
      accountType: "",
      accountNumber: "",
      balance: "",
      bankName: "",
    });
  };

  const handleAccountDetails = async () => {
    try {
      const token = Cookies.get("token");
      const data = {
        accountNumber: modalFormValue.accountNumber,
        bankName: modalFormValue.bankName,
      };
      const { data: resp } = await axios.post(`${baseURL}/mock-account`, data, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const { status } = await axios.post(`${baseURL}/account`, resp, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      axios.get(`${baseURL}/transactions/expenses`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      axios.get(`${baseURL}/transactions/incomes`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (status === 201) {
        setToasterData({ open: true, status: "success", message: "Account added Successfully" });
        handleClose();
        fetchData();
      }
    } catch (err) {
      setToasterData({ open: true, status: "error", message: err.message });
    }
  };

  const fetchData = async () => {
    try {
      const token = Cookies.get("token");
      const { status, data } = await axios.get(`${baseURL}/account`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (status === 200 && data) {
        data.map((ele) => (ele.balance = ele.balance.toFixed(2)));
        setRowData(data ?? []);
      }
    } catch (err) {
      setToasterData({ open: true, status: "error", message: err.message });
    }
  };

  const handleEditAccounts = async () => {
    try {
      const token = Cookies.get("token");
      const { status } = await axios.put(`${baseURL}/account`, modalFormValue, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (status === 204) {
        setToasterData({ open: true, status: "success", message: "Account edited Successfully" });
        handleClose();
        fetchData();
      }
    } catch (err) {
      setToasterData({ open: true, status: "error", message: err.message });
    }
  };

  const handleEdit = (data) => {
    setIsEdit(true);
    setModalFormValue({
      accountType: data.accountType,
      accountNumber: data.accountNumber,
      balance: data.balance,
      bankName: data.bankName,
    });
    setOpen(true);
  };

  return (
    <div className="mt-2">
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
      <AccountModal
        open={open}
        handleChange={handleValueChange}
        handleClose={handleClose}
        values={modalFormValue}
        validation={modalValidation}
        onAdd={isEdit ? handleEditAccounts : handleAccountDetails}
        disableBtn={disabledCondition}
        submitBtn={isEdit ? "Save" : "Add"}
        isEdit={isEdit}
      />
      <div style={{ justifyContent: "flex-end" }} className="d-flex mr-2">
        <Button sx={{ mr: 2 }} className="mr-2" onClick={handleClickOpen} variant="outlined">
          Add Account
        </Button>
      </div>
      <div className="pt-5">
        <TableComponent
          tableHead={[
            { value: "Account Number", key: "accountNumber" },
            { value: "Bank Name", key: "bankName" },
            { value: "Account Type", key: "accountType" },
            { value: "Balance", key: "balance" },
          ]}
          tableRowData={rowData}
          showEditButton
          handleEditButton={handleEdit}
        />
      </div>
    </div>
  );
}

export default Account;
