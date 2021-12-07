import * as React from 'react';
import Chip from '@mui/material/Chip';
import Stack from '@mui/material/Stack';
import { Typography } from '@mui/material';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableHead from '@mui/material/TableHead';
import TableBody from '@mui/material/TableBody';
import TableRow from '@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';

export default function ProgressDetails({ eventData }) {
  return (
    <TableContainer component={Paper}>
      <Table aria-label="Progress">
        <TableHead />
        <TableBody>
          <TableRow>
            <TableCell component="th" scope="row">
              Venue
            </TableCell>
            <TableCell component="th" scope="row">
              <Chip
                label={eventData.providers.venue.plannedStatus ? 'Complete' : 'Incomplete'}
                color={eventData.providers.venue.plannedStatus ? 'success' : 'warning'}
              />
            </TableCell>
          </TableRow>
          <TableRow>
            <TableCell component="th" scope="row">
              Entertainment
            </TableCell>
            <TableCell component="th" scope="row">
              <Chip
                label={eventData.providers.entertainment.plannedStatus ? 'Complete' : 'Incomplete'}
                color={eventData.providers.entertainment.plannedStatus ? 'success' : 'warning'}
              />
            </TableCell>
          </TableRow>
          <TableRow>
            <TableCell component="th" scope="row">
              Catering
            </TableCell>
            <TableCell component="th" scope="row">
              <Chip
                label={eventData.providers.catering.plannedStatus ? 'Complete' : 'Incomplete'}
                color={eventData.providers.catering.plannedStatus ? 'success' : 'warning'}
              />
            </TableCell>
          </TableRow>
          <TableRow>
            <TableCell component="th" scope="row">
              Security
            </TableCell>
            <TableCell component="th" scope="row">
              <Chip
                label={eventData.providers.security.plannedStatus ? 'Complete' : 'Incomplete'}
                color={eventData.providers.security.plannedStatus ? 'success' : 'warning'}
              />
            </TableCell>
          </TableRow>
          <TableRow>
            <TableCell component="th" scope="row">
              Master of Ceremony
            </TableCell>
            <TableCell component="th" scope="row">
              <Chip
                label={eventData.providers.mc.plannedStatus ? 'Complete' : 'Incomplete'}
                color={eventData.providers.mc.plannedStatus ? 'success' : 'warning'}
              />
            </TableCell>
          </TableRow>
          <TableRow>
            <TableCell component="th" scope="row">
              Design
            </TableCell>
            <TableCell component="th" scope="row">
              <Chip
                label={eventData.providers.design.plannedStatus ? 'Complete' : 'Incomplete'}
                color={eventData.providers.design.plannedStatus ? 'success' : 'warning'}
              />
            </TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </TableContainer>
  );
}
