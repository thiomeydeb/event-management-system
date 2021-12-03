import { useEffect, useState } from 'react';
import { Stack, Typography, Button, Container, Card } from '@mui/material';
import { Icon } from '@iconify/react';
import { Link as RouterLink } from 'react-router-dom';
import plusFill from '@iconify/icons-eva/plus-fill';
import editFill from '@iconify/icons-eva/edit-fill';
import axios from 'axios';
import Page from '../../components/Page';
import Scrollbar from '../../components/Scrollbar';
import ListVenues from './ListVenues';
import AddVenue from './AddVenue';
import EditVenue from './EditVenue';
import { apiBasePath, basicAuthBase64Header } from '../../constants/defaultValues';
import Notification from '../../components/custom/Notification';

const venueUrl = apiBasePath.concat('venue');

export default function Venue() {
  const [viewMode, setViewMode] = useState('list');
  const [alertOptions, setAlertOptions] = useState({
    open: false,
    severity: 'success',
    message: 'Success'
  });
  const [editData, setEditData] = useState({});
  const [venues, setVenues] = useState([{}]);
  const handleClose = () => {};
  const getVenues = (view) => {
    axios
      .get(venueUrl, {
        headers: {
          authorization: basicAuthBase64Header
        }
      })
      .then((res) => {
        setVenues(res.data.data);
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
    getVenues('list');
  }, []);
  const updateVenueStatus = (url, values) => {
    axios(url, {
      method: 'PUT',
      headers: {
        authorization: basicAuthBase64Header
      },
      data: values
    })
      .then((res) => {
        console.log(res);
        getVenues();
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
    <Page title="Venue | POSH Events">
      <Container>
        <Stack direction="row" alignItems="center" justifyContent="space-between" mb={7}>
          <Typography variant="h4" gutterBottom>
            {viewMode ? 'Venue' : 'Add Venue'}
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
            Edit Venue
          </Button>
          &nbsp;&nbsp;&nbsp;
          <Button
            variant="contained"
            component={RouterLink}
            to="#"
            startIcon={<Icon icon={plusFill} />}
            onClick={() => setViewMode('add')}
          >
            New Venue
          </Button>
        </Stack>
        <Card>
          <Scrollbar>
            {/* Display component based on view mode state */}
            {/* {viewMode ? <ListEventType /> : <AddEventType setViewMode={setViewMode} />} */}
            {viewMode === 'list' && (
              <ListVenues
                venues={venues}
                updateVenueStatus={updateVenueStatus}
                setViewMode={setViewMode}
                url={venueUrl}
                onEditClick={onEditClick}
              />
            )}
            {viewMode === 'add' && (
              <AddVenue
                setViewMode={setViewMode}
                setAlertOptions={setAlertOptions}
                url={venueUrl}
                getVenues={getVenues}
              />
            )}
            {viewMode === 'edit' && (
              <EditVenue
                setViewMode={setViewMode}
                setAlertOptions={setAlertOptions}
                url={venueUrl}
                getVenues={getVenues}
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
