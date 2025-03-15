import React from "react";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  TextField,
} from "@mui/material";
import { Box } from "@mui/system";

const CategoryModal = ({ open, handleClose, handleChange, value, onSubmit }) => {
  return (
    <Dialog fullWidth={true} maxWidth="sm" open={open} onClose={handleClose}>
      <DialogTitle>Add Category</DialogTitle>
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
            <TextField
              margin="dense"
              id="category"
              name="category"
              label="Category"
              fullWidth
              value={value}
              onChange={handleChange}
            />
          </FormControl>
        </Box>
      </DialogContent>
      <DialogActions>
        <Button disabled={value === ""} onClick={onSubmit}>
          ADD
        </Button>
        <Button onClick={handleClose}>Close</Button>
      </DialogActions>
    </Dialog>
  );
};

export default CategoryModal;
