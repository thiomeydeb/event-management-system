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

function createData(category, provider, status) {
  return { category, provider, status };
}

const rows = [
  createData('Security', 'KK Security', 'Complete'),
  createData('Catering', 'Savannah Caterings', 'Incomplete'),
  createData('Entertainment', 'System Unit', 'Complete'),
  createData('Design', 'Glamar Designs', 'Complete'),
  createData('MC', 'Smile Network Ltd', 'Incomplete'),
  createData('Venue', 'The Hub Karen', 'Incomplete')
];

export default function EventProgressTable() {
  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <caption>
          Overall Planning Status: <strong>Incomplete</strong>
        </caption>
        <TableHead>
          <TableRow>
            <TableCell>Category</TableCell>
            <TableCell>Provider Name</TableCell>
            <TableCell>Status</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row) => (
            <TableRow key={row.category} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
              <TableCell component="th" scope="row">
                {row.category}
              </TableCell>
              <TableCell>{row.provider}</TableCell>
              <TableCell>
                <Label
                  variant="ghost"
                  color={(row.status === 'Incomplete' && 'error') || 'success'}
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
