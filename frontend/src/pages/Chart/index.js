import Chart from "components/Chart";
import React, { useEffect, useState } from "react";
import Cookies from "js-cookie";
import axios from "axios";
import { ToggleButton, ToggleButtonGroup } from "@mui/material";

const baseURL = process.env.REACT_APP_BASE_URL;

const ChartPage = () => {
  const defaultData = {
    labels: [],
    datasets: [{ data: [], hoverOffset: 4 }],
  };

  const [allAccount, setAllAccount] = useState(defaultData);
  const [allIncome, setAllIncome] = useState([]);
  const [allExpense, setAllExpense] = useState([]);
  const [displayChart, setDisplayChart] = useState(false);
  const [filterData, setFilterData] = useState({
    allIncome: defaultData,
    allExpense: defaultData,
    allCategory: defaultData,
  });

  const handleToggleChange = (event, newDisplayChart) => {
    if (newDisplayChart === null) return;
    setDisplayChart(newDisplayChart);
    filterDataByDate(displayChart, allIncome, allExpense);
  };

  const filterDataByDate = (displayChart, incomeAll, expenseAll) => {
    const min = new Date().getTime() - 15 * 24 * 60 * 60 * 1000;
    const max = new Date().getTime();
    const filterIncome = displayChart
      ? incomeAll
      : incomeAll.filter((ele) => {
          return ele.date >= min && ele.date <= max;
        });
    const filterExpense = displayChart
      ? expenseAll
      : expenseAll.filter((ele) => {
          return ele.date >= min && ele.date <= max;
        });

    const incomeData = {};
    filterIncome.forEach((element) => {
      if (incomeData[element.accountNumber] === undefined) {
        incomeData[element.accountNumber] = 0.0;
      }
      incomeData[element.accountNumber] =
        parseFloat(element.amount) + parseFloat(incomeData[element.accountNumber]);
    });

    const filterCategory = {};
    const expenseData = {};
    filterExpense.forEach((element) => {
      if (expenseData[element.accountNumber] === undefined) {
        expenseData[element.accountNumber] = 0.0;
      }
      if (filterCategory[element.categoryName] === undefined) {
        filterCategory[element.categoryName] = 0.0;
      }
      expenseData[element.accountNumber] =
        parseFloat(element.expenseAmount) + parseFloat(expenseData[element.accountNumber]);
      filterCategory[element.categoryName] =
        parseFloat(element.expenseAmount) + parseFloat(filterCategory[element.categoryName]);
    });

    const categoryLabel = Object.keys(filterCategory).map((ele) =>
      ele === "null" ? "Uncategorized" : ele
    );

    setFilterData({
      ...filterData,
      allIncome: {
        labels: Object.keys(incomeData),
        datasets: [{ data: Object.values(incomeData) }],
      },
      allExpense: {
        labels: Object.keys(expenseData),
        datasets: [{ data: Object.values(expenseData) }],
      },
      allCategory: {
        labels: categoryLabel,
        datasets: [{ data: Object.values(filterCategory) }],
      },
    });
  };

  const fetchData = async () => {
    try {
      const token = Cookies.get("token");
      const { status, data: incomeData } = await axios.get(`${baseURL}/income/all`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const { status: expenseStatus, data: expenseData } = await axios.get(
        `${baseURL}/expense/all`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      if (status === 200 && expenseStatus === 200 && incomeData && expenseData) {
        incomeData.map((ele) => (ele.amount = ele.amount.toFixed(2)));
        expenseData.map((ele) => (ele.expenseAmount = ele.expenseAmount.toFixed(2)));
        setAllIncome([...incomeData]);
        setAllExpense([...expenseData]);
        filterDataByDate(true, [...incomeData], [...expenseData]);
      }
    } catch (err) {
      console.error(err.message);
    }
  };

  const fetchAccountData = async () => {
    try {
      const token = Cookies.get("token");
      const { status, data } = await axios.get(`${baseURL}/account`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (status === 200 && data) {
        data.map((ele) => (ele.balance = ele.balance.toFixed(2)));
        const accountData = {};
        data.forEach((element) => {
          accountData[element.accountNumber] = parseFloat(element.balance);
        });
        setAllAccount({
          labels: Object.keys(accountData),
          datasets: [{ data: Object.values(accountData) }],
        });
      }
    } catch (err) {
      console.error(err.message);
    }
  };

  useEffect(() => {
    fetchData();
    fetchAccountData();
  }, []);

  const checkData =
    allExpense.length !== 0 || allIncome.length !== 0 || allAccount.labels.length !== 0;

  console.log(checkData);

  return (
    <div className="mt-4">
      <div className="d-flex justify-content-around">
        <Chart data={allAccount} label="Account Balance" />
        <Chart data={filterData.allIncome} label="Income" />
      </div>
      <div className="d-flex justify-content-around mt-2">
        <Chart data={filterData.allExpense} label="Expense" />
        <Chart data={filterData.allCategory} label="Category" />
      </div>
      {checkData && (
        <ToggleButtonGroup
          value={displayChart}
          exclusive
          onChange={handleToggleChange}
          aria-label="text alignment">
          <ToggleButton value={false}>All</ToggleButton>
          <ToggleButton value={true}>Last 15 days</ToggleButton>
        </ToggleButtonGroup>
      )}
    </div>
  );
};

export default ChartPage;
