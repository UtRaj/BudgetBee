import SignIn from "pages/Auth/SignIn";
import Dashboard from "pages/Dashboard";
import SignUp from "pages/Auth/SignUp";
import Account from "pages/Account";
import Transaction from "pages/Transaction";

const routes = [
  {
    path: "/",
    name: "SignIn",
    component: SignIn,
    layout: "auth",
  },
  {
    path: "/signup",
    name: "SignUp",
    component: SignUp,
    layout: "auth",
  },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: Dashboard,
    layout: "private",
  },
  {
    path: "/account",
    name: "Account",
    component: Account,
    layout: "private",
  },
  {
    path: "/transaction",
    name: "Transaction",
    component: Transaction,
    layout: "private",
  },
];

export default routes;
