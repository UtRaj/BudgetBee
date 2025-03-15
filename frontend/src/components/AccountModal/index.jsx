import React from "react";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import { Box } from "@mui/system";

const AccountModal = ({
  open,
  handleClose,
  values,
  validation,
  handleChange,
  onAdd,
  disableBtn,
  submitBtn,
  isEdit,
}) => {
  return (
    <Dialog
      PaperProps={{
        sx: {
          minHeight: "50vh",
        },
      }}
      fullWidth={true}
      maxWidth="sm"
      open={open}
      onClose={handleClose}>
      <DialogTitle>{isEdit ? "Edit Account" : "Add Account"}</DialogTitle>
      <DialogContent>
        <Box
          noValidate
          component="form"
          sx={{
            display: "flex",
            flexDirection: "column",
            m: "auto",
            width: "50%",
          }}>
          {isEdit && (
            <FormControl sx={{ mt: 2, minWidth: 120 }}>
              <InputLabel htmlFor="accountType">Account Type</InputLabel>
              <Select
                error={validation.accountType !== ""}
                helperText={validation.accountType}
                autoFocus
                value={values.accountType}
                onChange={handleChange}
                label="Account Type"
                inputProps={{
                  name: "accountType",
                  id: "accountType",
                }}>
                <MenuItem value="Credit">Credit</MenuItem>
                <MenuItem value="Debit">Debit</MenuItem>
              </Select>
            </FormControl>
          )}
          <FormControl sx={{ mt: 2, minWidth: 120 }}>
            <TextField
              error={validation.accountNumber !== ""}
              helperText={validation.accountNumber}
              autoFocus
              margin="dense"
              id="accountNumber"
              name="accountNumber"
              label="Account Number"
              type="number"
              fullWidth
              value={values.accountNumber}
              onChange={handleChange}
              disabled={isEdit}
            />
          </FormControl>
          {isEdit && (
            <FormControl sx={{ mt: 2, minWidth: 120 }}>
              <TextField
                error={validation.balance !== ""}
                helperText={validation.balance}
                autoFocus
                margin="dense"
                id="balance"
                name="balance"
                label="Account Balance"
                type="number"
                fullWidth
                value={values.balance}
                onChange={handleChange}
              />
            </FormControl>
          )}
          <FormControl sx={{ mt: 2, minWidth: 120 }}>
            <TextField
              error={validation.bankName !== ""}
              helperText={validation.bankName}
              autoFocus
              margin="dense"
              id="bankName"
              name="bankName"
              label="Bank Name"
              type="text"
              fullWidth
              value={values.bankName}
              onChange={handleChange}
            />
          </FormControl>
        </Box>
      </DialogContent>
      <DialogActions>
        <Button disabled={disableBtn} onClick={onAdd}>
          {submitBtn}
        </Button>
        <Button onClick={handleClose}>Close</Button>
      </DialogActions>
    </Dialog>
  );
};

export default AccountModal;
