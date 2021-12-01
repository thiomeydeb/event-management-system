import { useState, useEffect } from 'react';
import { Stack, Typography, Button, Container, Card, Snackbar, Alert } from '@mui/material';
import { Icon } from '@iconify/react';
import { Link as RouterLink } from 'react-router-dom';
import plusFill from '@iconify/icons-eva/plus-fill';
import editFill from '@iconify/icons-eva/edit-fill';
import axios from 'axios';
import Page from '../../components/Page';
import Scrollbar from '../../components/Scrollbar';
import AddEventType from './AddEventType';
import ListEventType from './ListEventType';
import EditEventType from './EditEventType';

const eventTypeUrl = 'http://localhost:8080/api/v1/event-type';

export default function EventType() {
  const [viewMode, setViewMode] = useState('list');
  const [alertOptions, setAlertOptions] = useState({
    open: false,
    severity: 'success',
    message: 'Success'
  });
  const [eventTypes, setEventTypes] = useState([{}]);
  const handleClose = () => {
    setAlertOptions({
      open: false,
      severity: 'success',
      message: 'Values fetched successfully'
    });
  };
  const getEventTypes = () => {
    axios
      .get(eventTypeUrl, {
        auth: {
          username: 'user1',
          password: 'user1Pass'
        }
      })
      .then((res) => {
        console.log(res);
        setEventTypes(res.data.data);
        setAlertOptions({
          open: true,
          severity: 'success',
          message: 'Values fetched successfully'
        });
      })
      .catch((error) => {
        console.log(error.toJSON);
        setAlertOptions({
          open: true,
          severity: 'error',
          message: 'failed to fetch data'
        });
      });
  };
  useEffect(() => {
    getEventTypes();
  }, []);
  const Notification = () => (
    <Snackbar
      open={alertOptions.open}
      autoHideDuration={6000}
      onClose={handleClose}
      anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
    >
      <Alert onClose={handleClose} severity={alertOptions.severity} sx={{ width: '100%' }}>
        {alertOptions.message}
      </Alert>
    </Snackbar>
  );
  return (
    <Page title="Event Type | POSH Events">
      <Container>
        <Stack direction="row" alignItems="center" justifyContent="space-between" mb={7}>
          <Typography variant="h4" gutterBottom>
            {viewMode ? 'Event Types' : 'Add Event Type'}
          </Typography>
          <Button
            variant="contained"
            color="warning"
            style={{ marginLeft: 'auto' }}
            component={RouterLink}
            to="#"
            startIcon={<Icon icon={editFill} />}
            onClick={() => setViewMode('edit')}
          >
            Edit Event Type
          </Button>
          &nbsp;&nbsp;&nbsp;
          <Button
            variant="contained"
            component={RouterLink}
            to="#"
            startIcon={<Icon icon={plusFill} />}
            onClick={() => setViewMode('add')}
          >
            New Event Type
          </Button>
        </Stack>
        <Card>
          <Scrollbar>
            {/* Display component based on view mode state */}
            {viewMode === 'list' && <ListEventType eventTypes={eventTypes} />}
            {viewMode === 'add' && (
              <AddEventType
                setViewMode={setViewMode}
                setAlertOptions={setAlertOptions}
                url={eventTypeUrl}
              />
            )}
            {viewMode === 'edit' && (
              <EditEventType
                setViewMode={setViewMode}
                setAlertOptions={setAlertOptions}
                url={eventTypeUrl}
              />
            )}
          </Scrollbar>
        </Card>
        <Notification />
      </Container>
    </Page>
  );
}
