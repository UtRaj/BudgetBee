import React, { useState } from "react";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  FormControlLabel,
  InputLabel,
  MenuItem,
  Select,
  Radio,
  TextField,
  RadioGroup,
  Input,
} from "@mui/material";
import { Box } from "@mui/system";
import CategoryModal from "components/CategoryModal";
import styles from "./index.module.css";
const TransactionModal = ({
  open,
  handleClose,
  values,
  validation,
  handleChange,
  onSubmit,
  disableBtn,
  submitBtn,
  isEdit = false,
  allAccounts,
  transactionType = "expense",
  changeTransactionType,
  allCategories = [],
  openCategories,
  categoryName,
  handleOpenCategories,
  categoryChange,
  handleCategory,
}) => {
  const [showSelect, setShowSelect] = useState(false);
  const closeFunction = () => {
    setShowSelect(false);
    handleClose();
  };
  return (
    <>
      <CategoryModal
        open={openCategories}
        handleClose={() => handleOpenCategories(false)}
        handleChange={categoryChange}
        value={categoryName}
        onSubmit={handleCategory}
      />
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
        <DialogTitle>{isEdit ? "Edit Transaction" : "Add Transaction"}</DialogTitle>
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
            <FormControl sx={{ mt: 2, minWidth: 120 }}>
              <RadioGroup
                value={transactionType}
                onChange={changeTransactionType}
                name="transactionType"
                className="d-flex flex-row">
                <FormControlLabel
                  disabled={isEdit}
                  value="expense"
                  control={<Radio />}
                  label="Expense"
                />
                <FormControlLabel
                  disabled={isEdit}
                  value="income"
                  control={<Radio />}
                  label="Income"
                />
              </RadioGroup>
            </FormControl>
            <FormControl sx={{ mt: 2, minWidth: 120 }}>
              <InputLabel htmlFor="accountNumber">Select Acc. No.</InputLabel>
              <Select
                error={validation.accountNumber !== ""}
                helperText={validation.accountNumber}
                value={values.accountNumber}
                onChange={handleChange}
                disabled={isEdit}
                label="accountNumber"
                inputProps={{
                  name: "accountNumber",
                  id: "accountNumber",
                }}>
                {allAccounts.map((acc, i) => (
                  <MenuItem key={i} value={acc}>
                    {acc}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <FormControl sx={{ mt: 2, minWidth: 120 }}>
              <TextField
                error={validation.expenseAmount !== ""}
                helperText={validation.expenseAmount}
                margin="dense"
                id="expenseAmount"
                name="expenseAmount"
                label="Amount"
                fullWidth
                value={values.expenseAmount}
                onChange={handleChange}
              />
            </FormControl>
            {transactionType === "expense" && (
              <div className="d-flex justify-content-between">
                <FormControl sx={{ mt: 2, minWidth: 120 }}>
                  {values.categoryName && !showSelect ? (
                    <div className={styles.categoryName} onClick={() => setShowSelect(true)}>
                      {values.categoryName}
                    </div>
                  ) : (
                    <>
                      <InputLabel htmlFor="category">Category</InputLabel>
                      <Select
                        value={values.categoryName}
                        onChange={handleChange}
                        label="Category"
                        inputProps={{
                          name: "categoryName",
                          id: "category",
                        }}>
                        {allCategories.map((cat, i) => (
                          <MenuItem key={i} value={cat}>
                            {cat}
                          </MenuItem>
                        ))}
                      </Select>
                    </>
                  )}
                </FormControl>
                <Button
                  className="mt-3"
                  onClick={() => handleOpenCategories(true)}
                  variant="outlined">
                  +
                </Button>
              </div>
            )}
            <FormControl sx={{ mt: 2, minWidth: 120 }}>
              <TextField
                margin="dense"
                id="expenseDescription"
                name="expenseDescription"
                label="Description"
                type="textArea"
                fullWidth
                value={values.expenseDescription}
                onChange={handleChange}
              />
            </FormControl>
            <FormControl sx={{ mt: 2, minWidth: 120 }}>
              <Input type="date" name="date" value={values.date} onChange={handleChange} />
            </FormControl>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button disabled={disableBtn} onClick={onSubmit}>
            {submitBtn}
          </Button>
          <Button onClick={closeFunction}>Close</Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default TransactionModal;
