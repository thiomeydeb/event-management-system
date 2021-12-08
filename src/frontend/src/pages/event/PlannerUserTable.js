import * as React from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableHead from '@mui/material/TableHead';
import TableBody from '@mui/material/TableBody';
import TableRow from '@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import { sentenceCase } from 'change-case';
import Label from '../../components/Label';
import AssignPlannerMoreMenu from './menu/AssignPlannerMoreMenu';

function Row(props) {
  const { row, onLinkPlanner, eventData } = props;
  return (
    <>
      <TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
        <TableCell component="th" scope="row">
          {row.firstName.concat(' ').concat(row.middleName).concat(' ').concat(row.lastName)}
        </TableCell>
        <TableCell component="th" scope="row">
          {row.email}
        </TableCell>
        <TableCell component="th" scope="row">
          {row.phoneNumber}
        </TableCell>
        <TableCell component="th" scope="row">
          {row.identificationType}
        </TableCell>
        <TableCell component="th" scope="row">
          {row.identificationNumber}
        </TableCell>
        <TableCell component="th" scope="row">
          {row.companyName}
        </TableCell>
        <TableCell align="left">
          <Label variant="ghost" color={row.isActive ? 'success' : 'error'}>
            {sentenceCase(row.isActive ? 'Active' : 'InActive')}
          </Label>
        </TableCell>
        <TableCell align="left">
          <Label variant="ghost" color={row.isAdmin ? 'warning' : 'info'}>
            {sentenceCase(row.isActive ? 'Admin' : 'Normal User')}
          </Label>
        </TableCell>
        <TableCell align="right">
          <AssignPlannerMoreMenu
            eventId={eventData.eventId}
            plannerId={row.id}
            onLinkPlanner={onLinkPlanner}
          />
        </TableCell>
      </TableRow>
    </>
  );
}

const PlannerUserTable = ({ eventData, users, onLinkPlanner }) => (
  <TableContainer component={Paper}>
    <Table aria-label="Providers">
      <TableHead>
        <TableRow>
          <TableCell>Name</TableCell>
          <TableCell>Email</TableCell>
          <TableCell>Phone Number</TableCell>
          <TableCell>Identification Type</TableCell>
          <TableCell>Identification Number</TableCell>
          <TableCell>Company</TableCell>
          <TableCell>Status</TableCell>
          <TableCell>User Type</TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {users.map((row) => (
          <Row key={row.id} row={row} onLinkPlanner={onLinkPlanner} eventData={eventData} />
        ))}
      </TableBody>
    </Table>
  </TableContainer>
);

export default PlannerUserTable;
