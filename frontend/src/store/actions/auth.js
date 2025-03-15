import Types from "../types/auth";
import axios from "axios";

const baseURL = process.env.REACT_APP_BASE_URL;

const handleMockAccounts = async (token) => {
  try {
    await axios.get(`${baseURL}/mock-account/balance`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (err) {
    console.error(err.message);
  }
};

const handleMockExpenses = async (token) => {
  try {
    await axios.get(`${baseURL}/transactions/expenses`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (err) {
    console.error(err.message);
  }
};

const handleMockIncomes = async (token) => {
  try {
    await axios.get(`${baseURL}/transactions/incomes`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (err) {
    console.error(err.message);
  }
};

export const loginUser = (user) => {
  return async function (dispatch) {
    dispatch({
      type: Types.SIGNIN_USER_STARTED,
    });
    try {
      const { data } = await axios.post(`${baseURL}/authenticate`, user);
      const token = data.jwtToken;
      handleMockAccounts(token);
      handleMockExpenses(token);
      handleMockIncomes(token);
      dispatch({
        type: Types.SIGNIN_USER_SUCCESS,
        payload: {
          data,
        },
      });
      return {
        status: true,
      };
    } catch (e) {
      dispatch({
        type: Types.SIGNIN_USER_FAILED,
        payload: {
          error: e.message,
        },
      });
      return {
        status: false,
      };
    }
  };
};

export const logOutUser = () => {
  return async function (dispatch) {
    dispatch({
      type: Types.LOG_OUT_USER,
    });
  };
};
