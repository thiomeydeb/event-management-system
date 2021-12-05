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
  const { row, setViewMode, onEditClick } = props;
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
          {row.providerCategory ? row.providerCategory.name : ''}
        </TableCell>
        <TableCell component="th" scope="row">
          {row.cost}
        </TableCell>
        <TableCell align="left">
          <Label variant="ghost" color={(!row.active && 'error') || 'success'}>
            {sentenceCase(row.active ? 'active' : 'inactive')}
          </Label>
        </TableCell>
        <TableCell align="right">
          <EventMoreMenu row={row} setViewMode={setViewMode} onEditClick={onEditClick} />
        </TableCell>
      </TableRow>
    </>
  );
}

Row.propTypes = {
  row: PropTypes.shape({
    calories: PropTypes.number.isRequired,
    carbs: PropTypes.number.isRequired,
    fat: PropTypes.number.isRequired,
    history: PropTypes.arrayOf(
      PropTypes.shape({
        amount: PropTypes.number.isRequired,
        customerId: PropTypes.string.isRequired,
        date: PropTypes.string.isRequired
      })
    ).isRequired,
    name: PropTypes.string.isRequired,
    price: PropTypes.number.isRequired,
    protein: PropTypes.number.isRequired
  }).isRequired
};

export default function EventList({ events, setViewMode, onEditClick }) {
  return (
    <TableContainer component={Paper}>
      <Table aria-label="Providers">
        <TableHead>
          <TableRow>
            <TableCell>Title</TableCell>
            <TableCell>Category</TableCell>
            <TableCell>Cost</TableCell>
            <TableCell>Status</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {events.map((row) => (
            <Row key={row.id} row={row} setViewMode={setViewMode} onEditClick={onEditClick} />
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
