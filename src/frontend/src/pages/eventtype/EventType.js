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
import { apiBasePath, basicAuthBase64Header } from '../../constants/defaultValues';
import Notification from '../../components/custom/Notification';

const eventTypeUrl = apiBasePath.concat('event-type');

export default function EventType() {
  const [viewMode, setViewMode] = useState('list');
  const [alertOptions, setAlertOptions] = useState({
    open: false,
    severity: 'success',
    message: 'Success'
  });
  const [editData, setEditData] = useState({});
  const [eventTypes, setEventTypes] = useState([{}]);
  const handleClose = () => {
    setAlertOptions({
      open: false,
      severity: 'success',
      message: 'Values fetched successfully'
    });
  };
  const getEventTypes = (view) => {
    axios
      .get(eventTypeUrl, {
        headers: {
          authorization: basicAuthBase64Header
        }
      })
      .then((res) => {
        setEventTypes(res.data.data);
        if (view === 'list') {
          setAlertOptions({
            open: true,
            severity: 'success',
            message: 'Values fetched successfully'
          });
        }
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
    getEventTypes('list');
  }, []);
  const updateEventTypeStatus = (url, values) => {
    axios(url, {
      method: 'PUT',
      headers: {
        authorization: basicAuthBase64Header
      },
      data: values
    })
      .then((res) => {
        getEventTypes();
        setAlertOptions({
          open: true,
          message: 'status update successful',
          severity: 'success'
        });
      })
      .catch((error) => {
        console.log(error.toJSON);
        setAlertOptions({
          open: true,
          message: 'status update failed',
          severity: 'error'
        });
      });
  };
  const onEditClick = (eventTypeData) => {
    setEditData(eventTypeData);
    setViewMode('edit');
  };
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
            {viewMode === 'list' && (
              <ListEventType
                eventTypes={eventTypes}
                updateEventTypeStatus={updateEventTypeStatus}
                setViewMode={setViewMode}
                url={eventTypeUrl}
                onEditClick={onEditClick}
              />
            )}
            {viewMode === 'add' && (
              <AddEventType
                setViewMode={setViewMode}
                setAlertOptions={setAlertOptions}
                url={eventTypeUrl}
                getEventTypes={getEventTypes}
              />
            )}
            {viewMode === 'edit' && (
              <EditEventType
                setViewMode={setViewMode}
                setAlertOptions={setAlertOptions}
                url={eventTypeUrl}
                getEventTypes={getEventTypes}
                updateData={editData}
              />
            )}
          </Scrollbar>
        </Card>
        <Notification
          open={alertOptions.open}
          handleCLose={handleClose}
          severity={alertOptions.severity}
          message={alertOptions.message}
        />
      </Container>
    </Page>
  );
}
