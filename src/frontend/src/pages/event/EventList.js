import * as React from 'react';
import PropTypes from 'prop-types';
import Box from '@mui/material/Box';
import Collapse from '@mui/material/Collapse';
import IconButton from '@mui/material/IconButton';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { sentenceCase } from 'change-case';
import Label from '../../components/Label';
import EventMoreMenu from './menu/EventMoreMenu';
// import ExpandMoreIcon from '@mui/icons-material/ExpandMoreIcon';

function Row(props) {
  const { row, setViewMode, onChangeViewClick } = props;
  let statusColor = 'error';
  let statusMessage = 'undefined';
  let greeningStatusColor = 'error';
  let greeningStatusMessage = 'undefined';
  if (row.status === 0) {
    statusColor = 'warning';
    statusMessage = 'Pending';
  } else if (row.status === 1) {
    statusColor = 'info';
    statusMessage = 'Planner Assigned';
  } else if (row.status === 2) {
    statusColor = 'info';
    statusMessage = 'In progress';
  } else if (row.status === 3) {
    statusColor = 'success';
    statusMessage = 'Complete';
  } else if (row.status === 4) {
    statusColor = 'error';
    statusMessage = 'Cancelled';
  }

  if (row.status === 0) {
    greeningStatusColor = 'warning';
    greeningStatusMessage = 'Pending';
  } else if (row.status === 1) {
    greeningStatusColor = 'info';
    greeningStatusMessage = 'Planner Assigned';
  } else if (row.status === 2) {
    greeningStatusColor = 'info';
    greeningStatusMessage = 'In progress';
  } else if (row.status === 3) {
    greeningStatusColor = 'success';
    greeningStatusMessage = 'Complete';
  } else if (row.status === 4) {
    greeningStatusColor = 'error';
    statusMessage = 'Cancelled';
  }
  return (
    <>
      <TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
        {/* <TableCell>
           <ExpandMore
            expand={expanded}
            onClick={handleExpandClick}
            aria-expanded={expanded}
            aria-label="show more"
          >
            <ExpandMoreIcon />
          </ExpandMore>
        </TableCell> */}
        <TableCell component="th" scope="row">
          {row.title}
        </TableCell>
        <TableCell component="th" scope="row">
          {row.eventType}
        </TableCell>
        <TableCell component="th" scope="row">
          {row.attendees}
        </TableCell>
        <TableCell component="th" scope="row">
          {row.managementAmount}
        </TableCell>
        <TableCell component="th" scope="row">
          {row.totalAmount}
        </TableCell>
        <TableCell align="left">
          <Label variant="ghost" color={statusColor}>
            {sentenceCase(statusMessage)}
          </Label>
        </TableCell>
        <TableCell align="left">
          <Label variant="ghost" color={greeningStatusColor}>
            {sentenceCase(greeningStatusMessage)}
          </Label>
        </TableCell>
        <TableCell align="right">
          <EventMoreMenu
            row={row}
            setViewMode={setViewMode}
            onChangeViewClick={onChangeViewClick}
          />
        </TableCell>
      </TableRow>
    </>
  );
}

Row.propTypes = {
  row: PropTypes.shape({
    totalAmont: PropTypes.number.isRequired,
    managementAmount: PropTypes.number.isRequired,
    title: PropTypes.string.isRequired,
    category: PropTypes.number.isRequired
  }).isRequired
};

export default function EventList({ events, setViewMode, onChangeViewClick }) {
  return (
    <TableContainer component={Paper}>
      <Table aria-label="Providers">
        <TableHead>
          <TableRow>
            <TableCell>Title</TableCell>
            <TableCell>Type</TableCell>
            <TableCell>Attendees</TableCell>
            <TableCell>Management Cost</TableCell>
            <TableCell>Total Cost</TableCell>
            <TableCell>Status</TableCell>
            <TableCell>Greening Status</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {events.map((row) => (
            <Row
              key={row.id}
              row={row}
              setViewMode={setViewMode}
              onChangeViewClick={onChangeViewClick}
            />
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
