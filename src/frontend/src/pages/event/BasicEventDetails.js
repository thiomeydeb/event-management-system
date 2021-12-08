import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableHead from '@mui/material/TableHead';
import TableBody from '@mui/material/TableBody';
import TableRow from '@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import * as React from 'react';
import { Chip } from '@mui/material';

export default function BasicEventDetails({
  eventData,
  greeningStatusMessage,
  statusMessage,
  greeningColor,
  statusColor
}) {
  return (
    <TableContainer component={Paper}>
      <Table aria-label="Providers">
        <TableHead />
        <TableBody>
          <TableRow>
            <TableCell component="th" scope="row">
              Name
            </TableCell>
            <TableCell component="th" scope="row">
              {eventData.title}
            </TableCell>
          </TableRow>
          <TableRow>
            <TableCell component="th" scope="row">
              Type
            </TableCell>
            <TableCell component="th" scope="row">
              {eventData.eventType}
            </TableCell>
          </TableRow>
          <TableRow>
            <TableCell component="th" scope="row">
              Attendees
            </TableCell>
            <TableCell component="th" scope="row">
              {eventData.attendees}
            </TableCell>
          </TableRow>
          <TableRow>
            <TableCell component="th" scope="row">
              Other Information
            </TableCell>
            <TableCell component="th" scope="row">
              {eventData.otherInformation}
            </TableCell>
          </TableRow>
          <TableRow>
            <TableCell component="th" scope="row">
              Management Amount
            </TableCell>
            <TableCell component="th" scope="row">
              {eventData.managementAmount}
            </TableCell>
          </TableRow>
          <TableRow>
            <TableCell component="th" scope="row">
              Total Amount
            </TableCell>
            <TableCell component="th" scope="row">
              {eventData.totalAmount}
            </TableCell>
          </TableRow>
          <TableRow>
            <TableCell component="th" scope="row">
              Status
            </TableCell>
            <TableCell component="th" scope="row">
              <Chip label={statusMessage} color={statusColor} />
            </TableCell>
          </TableRow>
          <TableRow>
            <TableCell component="th" scope="row">
              Greening Status
            </TableCell>
            <TableCell component="th" scope="row">
              <Chip label={greeningStatusMessage} color={greeningColor} />
            </TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </TableContainer>
  );
}
