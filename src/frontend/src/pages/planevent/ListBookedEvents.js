import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { sentenceCase } from 'change-case';
import { UserMoreMenu } from '../../components/_dashboard/user';
import Label from '../../components/Label';

function createData(name, venue, total, status) {
  return { name, venue, total, status };
}

const rows = [
  createData('AFS fashion Event', 'The Hub Karen', 572000, 'Planning complete'),
  createData('D&C Exhibition', 'Carnivore', 478500, 'Planning incomplete'),
  createData('Danson Weds Philomena', 'Mamba-Village', 660000, 'Planning complete'),
  createData('AFS fashion Event', 'The Hub Karen', 572000, 'Event Planner Assigned'),
  createData('Adenkule Concert', 'Ngong Race cource', 802000, 'Planning incomplete')
];

export default function ListBookedEventsTable() {
  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Venue</TableCell>
            <TableCell>Total Amount</TableCell>
            <TableCell>Status</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row) => (
            <TableRow key={row.name} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
              <TableCell component="th" scope="row">
                {row.name}
              </TableCell>
              <TableCell>{row.venue}</TableCell>
              <TableCell>{row.total}</TableCell>
              <TableCell>
                <Label
                  variant="ghost"
                  color={(row.status === 'Planning incomplete' && 'error') || 'success'}
                >
                  {sentenceCase(row.status)}
                </Label>
              </TableCell>
              <TableCell align="right">
                <UserMoreMenu />
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
