import React, { useEffect, useState } from "react";
import {
  Alert,
  Button,
  MenuItem,
  Pagination,
  Select,
  Snackbar,
  ToggleButton,
  ToggleButtonGroup,
} from "@mui/material";
import Cookies from "js-cookie";
import axios from "axios";
import TableComponent from "components/Table";
import TransactionModal from "components/TransactionModal";

const baseURL = process.env.REACT_APP_BASE_URL;

const Transaction = () => {
  const [open, setOpen] = useState(false);
  const [transactionType, setTransactionType] = useState("expense");
  const [displayExpense, setDisplayExpense] = useState(true);
  const [isEdit, setIsEdit] = useState(-1);
  const [pageNumber, setPageNumber] = useState(1);
  const [allExpense, setAllExpense] = useState([]);
  const [allIncome, setAllIncome] = useState([]);
  const [filterTransaction, setFilterTransaction] = useState([]);
  const [allAccounts, setAllAccounts] = useState([]);
  const [allCategories, setAllCategories] = useState([]);
  const [toasterData, setToasterData] = useState({ open: false, status: "", message: "" });
  const [openCategories, setOpenCategories] = useState(false);
  const [categoryName, setCategoryName] = useState("");
  const [transactionDetails, setTransactionDetails] = useState({
    accountNumber: "",
    expenseAmount: "",
    expenseDescription: "",
    date: "",
    categoryName: "",
  });
  const [transactionValidation, setTransactionValidation] = useState({
    accountNumber: "",
    expenseAmount: "",
  });

  useEffect(() => {
    fetchData();
    fetchAccounts();
    fetchCategories();
  }, []);

  const fetchAccounts = async () => {
    try {
      const token = Cookies.get("token");
      const { status, data } = await axios.get(`${baseURL}/account`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (status === 200 && data) {
        const allAcc = data.map((ele) => ele.accountNumber);
        setAllAccounts(allAcc ?? []);
      }
    } catch (err) {
      console.error(err.message);
    }
  };

  const fetchCategories = async () => {
    try {
      const token = Cookies.get("token");
      const { status, data } = await axios.get(`${baseURL}/category/all`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (status === 200 && data) {
        const allCat = data.map((ele) => ele.categoryName);
        setAllCategories(allCat ?? []);
      }
    } catch (err) {
      console.error(err.message);
    }
  };

  const fetchData = async () => {
    try {
      const token = Cookies.get("token");
      setPageNumber(1);
      const { status, data } = await axios.get(`${baseURL}/expense/all`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (status === 200 && data) {
        data.map((ele) => (ele.expenseAmount = ele.expenseAmount.toFixed(2)));
        setAllExpense(data ?? []);
      }
      const { status: incomeStatus, data: incomeData } = await axios.get(`${baseURL}/income/all`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (incomeStatus === 200 && incomeData) {
        incomeData.map((ele) => (ele.amount = ele.amount.toFixed(2)));
        setAllIncome(incomeData ?? []);
      }
      const filterData = displayExpense ? data : incomeData;
      const filter = [...filterData].slice(0, 5);
      setFilterTransaction(filter ?? []);
    } catch (err) {
      setToasterData({ open: true, status: "error", message: err.message });
    }
  };

  const handleClose = () => {
    setOpen(false);
    setIsEdit(-1);
    setTransactionDetails({
      accountNumber: "",
      expenseAmount: "",
      expenseDescription: "",
      date: "",
      categoryName: "",
    });
    setTransactionValidation({
      accountNumber: "",
      expenseAmount: "",
    });
    setTransactionType("expense");
  };

  const disabledCondition =
    Object.values({
      ...transactionDetails,
      expenseDescription: "description",
      categoryName: "category",
    }).some((ele) => ele.length === 0) || Object.values(transactionValidation).some((ele) => ele);

  const checkValidation = (name, value) => {
    if (value === "" && name !== "expenseDescription") {
      setTransactionValidation({
        ...transactionValidation,
        [name]: `Please enter ${name} properly`,
      });
    } else {
      setTransactionValidation({
        ...transactionValidation,
        [name]: "",
      });
    }
  };

  const handleValueChange = (e) => {
    const { name, value } = e.target;
    if (name === "expenseAmount" && !value.match(/^[0-9]*(\.)?[0-9]*$/) && value.length) {
      return;
    }
    setTransactionDetails({ ...transactionDetails, [name]: value });
    checkValidation(name, value);
  };

  const handleTransactionDetails = async () => {
    try {
      const token = Cookies.get("token");
      const date = transactionDetails.date ? new Date(transactionDetails.date) : new Date();
      const data =
        transactionType === "expense"
          ? {
              ...transactionDetails,
              expenseDescription: transactionDetails.expenseDescription || null,
              categoryName: transactionDetails.categoryName || null,
              date: date.getTime(),
            }
          : {
              accountNumber: transactionDetails.accountNumber,
              amount: transactionDetails.expenseAmount,
              description: transactionDetails.expenseDescription || null,
              date: date.getTime(),
            };
      const { status } = await axios.post(`${baseURL}/${transactionType}/add`, data, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (status === 201) {
        setToasterData({
          open: true,
          status: "success",
          message: `${transactionType} added Successfully`,
        });
        handleClose();
        fetchData();
      }
    } catch (err) {
      setToasterData({ open: true, status: "error", message: err.message });
    }
  };
  const handleEditTransaction = async () => {
    try {
      const date = new Date(transactionDetails.date);
      const token = Cookies.get("token");
      const data = displayExpense
        ? {
            ...transactionDetails,
            expenseDescription: transactionDetails.expenseDescription || null,
            categoryName: transactionDetails.categoryName || null,
            date: date.getTime(),
          }
        : {
            accountNumber: transactionDetails.accountNumber,
            amount: transactionDetails.expenseAmount,
            description: transactionDetails.expenseDescription,
            date: date.getTime(),
          };
      const url = displayExpense
        ? `${baseURL}/expense/update/${isEdit}`
        : `${baseURL}/income/${isEdit}`;
      const { status } = await axios.put(url, data, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (status === 204 || status === 201) {
        setToasterData({
          open: true,
          status: "success",
          message: `${displayExpense ? "Expense" : "Income"} edited Successfully`,
        });
        handleClose();
        fetchData();
      }
    } catch (err) {
      setToasterData({ open: true, status: "error", message: err.message });
    }
  };

  const handleTableEdit = (data) => {
    const date = new Date(data.date);
    setIsEdit(displayExpense ? data.expenseId : data.incomeId);
    setTransactionType(displayExpense ? "expense" : "income");
    setTransactionDetails({
      accountNumber: data.accountNumber,
      expenseAmount: displayExpense ? data.expenseAmount : data.amount,
      expenseDescription: displayExpense ? data.expenseDescription : data.description,
      date: date.toISOString().split("T")[0],
      categoryName: data.categoryName || "",
    });
    setOpen(true);
  };

  const handleTableDelete = async (data) => {
    try {
      const token = Cookies.get("token");
      const id = displayExpense ? data.expenseId : data.incomeId;
      const url = displayExpense ? `${baseURL}/expense/delete/${id}` : `${baseURL}/income/${id}`;
      const { status } = await axios.delete(url, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (status === 204) {
        setToasterData({
          open: true,
          status: "success",
          message: `${displayExpense ? "Expense" : "Income"} deleted Successfully`,
        });
        fetchData();
      }
    } catch (err) {
      setToasterData({ open: true, status: "error", message: err.message });
    }
  };

  const handleAccountFilter = (e) => {
    const { value } = e.target;
    const requiredData = displayExpense ? allExpense : allIncome;
    if (value === "All") {
      const filter = [...requiredData].slice(0, 5);
      setFilterTransaction(filter);
      return;
    }
    const data = requiredData.filter((ex) => ex.accountNumber === value);
    setFilterTransaction(data);
    setPageNumber(1);
  };

  const handleTransactionType = (e) => {
    const { value } = e.target;
    setTransactionType(value);
  };

  const handleChangePage = (pg) => {
    const exp = displayExpense ? [...allExpense] : [...allIncome];
    const offset = 5 * (pg - 1);
    const filter = exp.slice(offset, 5 * pg);
    setFilterTransaction(filter);
    setPageNumber(pg);
  };

  const handleToggleChange = (e, value) => {
    if (value === null) return;
    setDisplayExpense(value);
    setPageNumber(1);
    if (value) {
      const filter = [...allExpense].slice(0, 5);
      setFilterTransaction(filter);
    } else {
      const filter = [...allIncome].slice(0, 5);
      setFilterTransaction(filter);
    }
  };

  const handleOpen = () => {
    const currentDate = new Date();
    setOpen(true);
    setTransactionDetails({ ...transactionDetails, date: currentDate.toISOString().split("T")[0] });
  };

  const handleCategories = (value) => {
    setOpenCategories(value);
    if (value === false) {
      setCategoryName("");
    }
  };
  const CategoryChange = (e) => {
    const { value } = e.target;
    setCategoryName(value);
  };

  const handleCategory = async () => {
    try {
      const token = Cookies.get("token");
      const { status } = await axios.post(
        `${baseURL}/category/add`,
        { categoryName },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      if (status === 201) {
        setTransactionDetails({ ...transactionDetails, categoryName });
        await fetchCategories();
        handleCategories(false);
      }
    } catch (err) {
      console.error(err.message);
    }
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
      <TransactionModal
        open={open}
        handleChange={handleValueChange}
        handleClose={handleClose}
        values={transactionDetails}
        validation={transactionValidation}
        onSubmit={isEdit !== -1 ? handleEditTransaction : handleTransactionDetails}
        disableBtn={disabledCondition}
        submitBtn={isEdit !== -1 ? "Save" : "Add"}
        isEdit={isEdit !== -1}
        allAccounts={allAccounts}
        transactionType={transactionType}
        changeTransactionType={handleTransactionType}
        allCategories={allCategories}
        openCategories={openCategories}
        categoryName={categoryName}
        categoryChange={CategoryChange}
        handleCategory={handleCategory}
        handleOpenCategories={handleCategories}
      />
      <div className="d-flex justify-content-end">
        <Button sx={{ mr: 2 }} className="mr-2" onClick={handleOpen} variant="outlined">
          Add Transaction
        </Button>
      </div>
      <div className="pt-5">
        <div>
          <ToggleButtonGroup
            value={displayExpense}
            exclusive
            onChange={handleToggleChange}
            aria-label="text alignment">
            <ToggleButton value={true}>Expense</ToggleButton>
            <ToggleButton value={false}>Income</ToggleButton>
          </ToggleButtonGroup>
        </div>
        <div className="d-flex justify-content-between">
          <Select
            onChange={handleAccountFilter}
            defaultValue={"All"}
            label="accountNumber"
            inputProps={{
              name: "accountNumber",
              id: "accountNumber",
            }}>
            {["All", ...allAccounts].map((acc, i) => (
              <MenuItem key={i} value={acc}>
                {acc}
              </MenuItem>
            ))}
          </Select>
        </div>
        <TableComponent
          tableHead={[
            { value: "Account Number", key: "accountNumber" },
            { value: "Amount", key: displayExpense ? "expenseAmount" : "amount" },
            { value: "Description", key: displayExpense ? "expenseDescription" : "description" },
            displayExpense && { value: "Category", key: "categoryName" },
            { value: "Date", key: "date" },
          ]}
          className="pt-0"
          tableRowData={filterTransaction}
          showEditButton
          handleEditButton={handleTableEdit}
          showDeleteButton
          handleDeleteButton={handleTableDelete}
        />
        <Pagination
          component="div"
          count={Math.ceil(displayExpense ? allExpense.length / 5 : allIncome.length / 5)}
          className="d-flex justify-content-center"
          rowsPerPage={5}
          page={pageNumber}
          onChange={(event, value) => handleChangePage(value)}
        />
      </div>
    </div>
  );
};

export default Transaction;
