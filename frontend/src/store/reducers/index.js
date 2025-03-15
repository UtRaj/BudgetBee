import { combineReducers } from "redux";

import AuthReducer from "./auth";

const reducers = {
  auth: AuthReducer,
};

const combinedReducer = combineReducers(reducers);

export default combinedReducer;
