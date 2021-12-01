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

function createData(name, location, status) {
  return { name, location, status };
}

const rows = [
  createData('Carnivore', 'Kenya,Nairobi,Langata', 'in-active'),
  createData('The Hub Karen', 'Kenya,Nairobi,Langata', 'active'),
  createData('Ngong Race cource', 'Kenya,Nairobi,Ngong', 'in-active'),
  createData('Social House', 'Kenya,Nairobi,James Gichuru', 'active')
];

export default function ListVenuesTable() {
  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Location</TableCell>
            <TableCell>Status</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row) => (
            <TableRow key={row.name} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
              <TableCell component="th" scope="row">
                {row.name}
              </TableCell>
              <TableCell>{row.location}</TableCell>
              <TableCell>
                <Label variant="ghost" color={(row.status === 'in-active' && 'error') || 'success'}>
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
