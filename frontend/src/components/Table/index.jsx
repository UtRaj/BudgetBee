import React from "react";
import { Paper, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material";
import { Table } from "react-bootstrap";

import editIcon from "assets/icons/edit-icon.svg";
import deleteIcon from "assets/icons/delete-icon.svg";
import styles from "./index.module.css";

const TableComponent = ({
  tableHead,
  tableRowData,
  showEditButton = false,
  handleEditButton,
  showDeleteButton = false,
  handleDeleteButton,
}) => {
  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650, maxWidth: 900 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            {tableHead.map((title, i) => (
              <TableCell align="left" key={i}>
                {title.value}
              </TableCell>
            ))}
            {showEditButton && <TableCell align="left"></TableCell>}
            {showDeleteButton && <TableCell align="left"></TableCell>}
          </TableRow>
        </TableHead>
        {!tableRowData.length ? (
          <div className="d-flex justify-content-center">No Data Found</div>
        ) : (
          <TableBody>
            {tableRowData.map((data, i) => {
              const date = new Date(data.date);
              date.setDate(date.getDate() + 1);
              return (
                <TableRow key={i} sx={{ "&:last-child td, &:last-child th": { border: 0 } }}>
                  {tableHead.map((field, index) => (
                    <TableCell component="th" scope="row" key={index}>
                      {field.key === "date" ? date.toDateString() : data[field.key]}
                    </TableCell>
                  ))}
                  {showEditButton && (
                    <TableCell align="right">
                      <img
                        src={editIcon}
                        alt="Edit Icon"
                        className={styles.editIcon}
                        onClick={() => handleEditButton(data)}
                      />
                    </TableCell>
                  )}
                  {showDeleteButton && (
                    <TableCell align="right">
                      <img
                        src={deleteIcon}
                        alt="Delete Icon"
                        className={styles.deleteIcon}
                        onClick={() => handleDeleteButton(data)}
                      />
                    </TableCell>
                  )}
                </TableRow>
              );
            })}
          </TableBody>
        )}
      </Table>
    </TableContainer>
  );
};

export default TableComponent;
