import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { sentenceCase } from 'change-case';
import Label from '../../components/Label';
import VenueMoreMenu from './menu/VenueMoreMenu';

export default function ListVenuesTable({
  venues,
  updateVenueStatus,
  setViewMode,
  url,
  onEditClick
}) {
  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Location</TableCell>
            <TableCell>Price</TableCell>
            <TableCell>Status</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {venues.map((row) => (
            <TableRow key={row.id} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
              <TableCell component="th" scope="row">
                {row.name}
              </TableCell>
              <TableCell>{row.location}</TableCell>
              <TableCell>{row.amount}</TableCell>
              <TableCell align="left">
                <Label variant="ghost" color={(!row.active && 'error') || 'success'}>
                  {sentenceCase(row.active ? 'active' : 'inactive')}
                </Label>
              </TableCell>
              <TableCell align="right">
                <VenueMoreMenu
                  row={row}
                  updateVenueStatus={updateVenueStatus}
                  setViewMode={setViewMode}
                  url={url}
                  onEditClick={onEditClick}
                />
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
