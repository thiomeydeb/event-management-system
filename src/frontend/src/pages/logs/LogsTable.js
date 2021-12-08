import * as React from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableHead from '@mui/material/TableHead';
import TableBody from '@mui/material/TableBody';
import TableRow from '@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';

function Row(props) {
  const { row } = props;
  return (
    <>
      <TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
        <TableCell component="th" scope="row">
          {row.action}
        </TableCell>
        <TableCell component="th" scope="row">
          {row.ipAddress}
        </TableCell>
        <TableCell component="th" scope="row">
          {row.transactionTime}
        </TableCell>
        <TableCell component="th" scope="row">
          {row.reference}
        </TableCell>
      </TableRow>
    </>
  );
}

const LogsTable = ({ logs }) => (
  <TableContainer component={Paper}>
    <Table aria-label="Providers">
      <TableHead>
        <TableRow>
          <TableCell>Action</TableCell>
          <TableCell>Ip Address</TableCell>
          <TableCell>Transaction Time</TableCell>
          <TableCell>Reference</TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {logs.map((row) => (
          <Row key={row.id} row={row} />
        ))}
      </TableBody>
    </Table>
  </TableContainer>
);

export default LogsTable;
