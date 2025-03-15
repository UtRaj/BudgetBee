import React from "react";

import { useLocation, useNavigate } from "react-router";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import PersonIcon from "@mui/icons-material/Person";
import InboxIcon from "@mui/icons-material/Inbox";
import ListItemButton from "@mui/material/ListItemButton";
import cs from "classnames";

import { Box, Divider, Drawer, List, ListItem, ListItemIcon, ListItemText } from "@mui/material";
import { Link } from "react-router-dom";
import { useDispatch } from "react-redux";
import { logOutUser } from "store/actions/auth";
import TransactionIcon from "assets/icons/transaction-icon.svg";
import styles from "./index.module.css";

function PrivateLayout({ children }) {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const location = useLocation();

  const DashboardRoutes = [
    {
      path: "/dashboard",
      name: "Dashboard",
      icon: <InboxIcon />,
    },
    {
      path: "/account",
      name: "Account",
      icon: <PersonIcon />,
    },
    {
      path: "/transaction",
      name: "Transaction",
      icon: <img src={TransactionIcon} alt="icon" className={styles.icon} />,
    },
  ];
  const [state, setState] = React.useState({
    top: false,
    left: false,
    bottom: false,
    right: false,
  });

  const toggleDrawer = (anchor, open) => (event) => {
    if (event.type === "keydown" && (event.key === "Tab" || event.key === "Shift")) {
      return;
    }

    setState({ ...state, [anchor]: open });
  };
  const list = (anchor) => (
    <Box
      sx={{ width: anchor === "top" || anchor === "bottom" ? "auto" : 250 }}
      role="presentation"
      onClick={toggleDrawer(anchor, false)}
      onKeyDown={toggleDrawer(anchor, false)}>
      <List>
        {DashboardRoutes.map(({ path, name, icon }, index) => (
          <ListItem selected={location?.pathname === path ?? false} key={index} disablePadding>
            <ListItemButton components={Link} onClick={() => navigate(path)}>
              <ListItemIcon>{icon}</ListItemIcon>
              <ListItemText primary={name} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
      <Divider />
    </Box>
  );

  const doLogout = () => {
    dispatch(logOutUser());
    navigate("/");
  };

  return (
    <>
      <div className={cs("d-flex", styles.navbar)}>
        <AppBar position="static">
          <Toolbar className="d-flex justify-content-between">
            <IconButton
              onClick={toggleDrawer("left", true)}
              size="large"
              edge="start"
              color="inherit"
              aria-label="menu"
              sx={{ mr: 2 }}>
              <MenuIcon />
            </IconButton>
            <Typography
              variant="h6"
              component="div"
              className={styles.title}
              onClick={() => navigate("/dashboard")}>
              FinTrack
            </Typography>
            <Button color="inherit" onClick={doLogout}>
              Logout
            </Button>
          </Toolbar>
        </AppBar>
        <Drawer anchor={"left"} open={state["left"]} onClose={toggleDrawer("left", false)}>
          {list("left")}
        </Drawer>
      </div>
      {children}
    </>
  );
}

export default PrivateLayout;
