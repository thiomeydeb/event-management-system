import * as React from 'react';
import Chip from '@mui/material/Chip';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableHead from '@mui/material/TableHead';
import TableBody from '@mui/material/TableBody';
import TableRow from '@mui/material/TableRow';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import ManageProviderMoreMenu from './menu/ManageProviderMoreMenu';

export default function ManageProviders({
  eventData,
  setViewMode,
  onChangeViewClick,
  onUpdateProviderStatus
}) {
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
            <TableCell align="right">
              <ManageProviderMoreMenu
                row={eventData}
                status={eventData.providers.venue.plannedStatus}
                eventId={eventData.eventId}
                plannedDetailsId={eventData.providers.venue.plannedDetailsId}
                setViewMode={setViewMode}
                onChangeViewClick={onChangeViewClick}
                onUpdateProviderStatus={onUpdateProviderStatus}
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
            <TableCell align="right">
              <ManageProviderMoreMenu
                status={eventData.providers.entertainment.plannedStatus}
                eventId={eventData.eventId}
                plannedDetailsId={eventData.providers.entertainment.plannedDetailsId}
                onUpdateProviderStatus={onUpdateProviderStatus}
                setViewMode={setViewMode}
                onChangeViewClick={onChangeViewClick}
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
            <TableCell align="right">
              <ManageProviderMoreMenu
                row={eventData}
                status={eventData.providers.catering.plannedStatus}
                eventId={eventData.eventId}
                plannedDetailsId={eventData.providers.catering.plannedDetailsId}
                onUpdateProviderStatus={onUpdateProviderStatus}
                setViewMode={setViewMode}
                onChangeViewClick={onChangeViewClick}
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
            <TableCell align="right">
              <ManageProviderMoreMenu
                row={eventData}
                status={eventData.providers.security.plannedStatus}
                eventId={eventData.eventId}
                plannedDetailsId={eventData.providers.security.plannedDetailsId}
                onUpdateProviderStatus={onUpdateProviderStatus}
                setViewMode={setViewMode}
                onChangeViewClick={onChangeViewClick}
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
            <TableCell align="right">
              <ManageProviderMoreMenu
                row={eventData}
                status={eventData.providers.mc.plannedStatus}
                eventId={eventData.eventId}
                plannedDetailsId={eventData.providers.mc.plannedDetailsId}
                onUpdateProviderStatus={onUpdateProviderStatus}
                setViewMode={setViewMode}
                onChangeViewClick={onChangeViewClick}
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
            <TableCell align="right">
              <ManageProviderMoreMenu
                row={eventData}
                status={eventData.providers.design.plannedStatus}
                eventId={eventData.eventId}
                plannedDetailsId={eventData.providers.design.plannedDetailsId}
                onUpdateProviderStatus={onUpdateProviderStatus}
                setViewMode={setViewMode}
                onChangeViewClick={onChangeViewClick}
              />
            </TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </TableContainer>
  );
}
