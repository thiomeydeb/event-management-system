import { useEffect, useState } from 'react';
import { Stack, Typography, Button, Container, Card } from '@mui/material';
import { Icon } from '@iconify/react';
import { Link as RouterLink } from 'react-router-dom';
import plusFill from '@iconify/icons-eva/plus-fill';
import editFill from '@iconify/icons-eva/edit-fill';
import axios from 'axios';
import Page from '../../components/Page';
import Scrollbar from '../../components/Scrollbar';
import EventList from '../event/EventList';
import { apiBasePath, basicAuthBase64Header } from '../../constants/defaultValues';
import Notification from '../../components/custom/Notification';
import EventProgress from '../event/EventProgress';

const eventsUrl = apiBasePath.concat('event');
const plannedEventDetailsUrl = apiBasePath.concat('planned-event-details');

export default function PlanEvent() {
  const [viewMode, setViewMode] = useState('list');
  const [alertOptions, setAlertOptions] = useState({
    open: false,
    severity: 'success',
    message: 'Success'
  });
  const [editData, setEditData] = useState({});
  const [events, setEvents] = useState([{}]);
  const handleClose = () => {};
  const getEvents = (view) => {
    axios
      .get(eventsUrl, {
        headers: {
          authorization: basicAuthBase64Header
        }
      })
      .then((res) => {
        setEvents(res.data.data);
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
    getEvents('list');
  }, []);

  const updateProviderStatus = (plannedDetailsId, values) => {
    const url = plannedEventDetailsUrl.concat('/provider/update/').concat(plannedDetailsId);
    axios(url, {
      method: 'PUT',
      headers: {
        authorization: basicAuthBase64Header
      },
      data: values
    })
      .then((res) => {
        getEvents();
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
  const changeViewClick = (eventData, view) => {
    setEditData(eventData);
    setViewMode(view);
  };
  return (
    <Page title="Event | POSH Events">
      <Container>
        <Stack direction="row" alignItems="center" justifyContent="space-between" mb={7}>
          <Typography variant="h4" gutterBottom>
            {viewMode ? 'Plan Event' : 'Add Event'}
          </Typography>
        </Stack>
        <Card>
          <Scrollbar>
            {/* Display component based on view mode state */}
            {/* {viewMode ? <ListEventType /> : <AddEventType setViewMode={setViewMode} />} */}
            {viewMode === 'list' && (
              <EventList
                events={events}
                setViewMode={setViewMode}
                onChangeViewClick={changeViewClick}
              />
            )}
            {viewMode === 'view' && (
              <EventProgress
                setViewMode={setViewMode}
                eventData={editData}
                userType="planner"
                updateProviderStatus={updateProviderStatus}
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
