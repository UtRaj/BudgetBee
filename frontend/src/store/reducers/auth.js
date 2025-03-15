import Types from "../types/auth";
import Cookies from "js-cookie";

const initialState = {
  user: {
    data: null,
    loading: false,
    error: null,
  },
};

const AuthReducer = (state = initialState, action) => {
  const { type, payload } = action;

  switch (type) {
    case Types.SIGNIN_USER_STARTED:
      return {
        ...state,
        user: {
          ...state.user,
          loading: true,
        },
      };
    case Types.SIGNIN_USER_SUCCESS:
      Cookies.set("token", payload.data?.jwtToken);
      return {
        ...state,
        user: {
          ...state.user,
          data: payload.data,
          loading: false,
        },
      };
    case Types.SIGNIN_USER_FAILED:
      return {
        ...state,
        user: {
          ...state.user,
          error: payload.error,
          loading: false,
        },
      };
    case Types.LOG_OUT_USER: {
      Cookies.remove("token");
      return {
        ...state,
        user: {
          data: null,
          loading: false,
          error: null,
        },
      };
    }
    default:
      return state;
  }
};

export default AuthReducer;
