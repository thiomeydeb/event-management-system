import { useEffect, useState } from 'react';
import { Stack, Typography, Button, Container, Card } from '@mui/material';
import { Icon } from '@iconify/react';
import { Link as RouterLink } from 'react-router-dom';
import plusFill from '@iconify/icons-eva/plus-fill';
import editFill from '@iconify/icons-eva/edit-fill';
import axios from 'axios';
import Page from '../../components/Page';
import Scrollbar from '../../components/Scrollbar';
import AddProvider from './AddProvider';
import ListProviders from './ListProviders';
import EditProvider from './EditProvider';
import { apiBasePath, basicAuthBase64Header } from '../../constants/defaultValues';
import Notification from '../../components/custom/Notification';

const providerUrl = apiBasePath.concat('provider');

export default function Provider() {
  const [viewMode, setViewMode] = useState('list');
  const [alertOptions, setAlertOptions] = useState({
    open: false,
    severity: 'success',
    message: 'Success'
  });
  const [editData, setEditData] = useState({});
  const [providers, setProviders] = useState([{}]);
  const handleClose = () => {};
  const getProviders = (view) => {
    axios
      .get(providerUrl, {
        headers: {
          authorization: basicAuthBase64Header
        }
      })
      .then((res) => {
        setProviders(res.data.data);
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
    getProviders('list');
  }, []);
  const updateProviderStatus = (url, values) => {
    axios(url, {
      method: 'PUT',
      headers: {
        authorization: basicAuthBase64Header
      },
      data: values
    })
      .then((res) => {
        getProviders();
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
  const onEditClick = (providerData) => {
    setEditData(providerData);
    setViewMode('edit');
  };
  return (
    <Page title="Provider | POSH Events">
      <Container>
        <Stack direction="row" alignItems="center" justifyContent="space-between" mb={7}>
          <Typography variant="h4" gutterBottom>
            {viewMode ? 'Provider' : 'Add Provider'}
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
            Edit Provider
          </Button>
          &nbsp;&nbsp;&nbsp;
          <Button
            variant="contained"
            component={RouterLink}
            to="#"
            startIcon={<Icon icon={plusFill} />}
            onClick={() => setViewMode('add')}
          >
            New Provider
          </Button>
        </Stack>
        <Card>
          <Scrollbar>
            {/* Display component based on view mode state */}
            {/* {viewMode ? <ListEventType /> : <AddEventType setViewMode={setViewMode} />} */}
            {viewMode === 'list' && (
              <ListProviders
                providers={providers}
                updateProviderStatus={updateProviderStatus}
                setViewMode={setViewMode}
                url={providerUrl}
                onEditClick={onEditClick}
              />
            )}
            {viewMode === 'add' && (
              <AddProvider
                setViewMode={setViewMode}
                setAlertOptions={setAlertOptions}
                url={providerUrl}
                getProviders={getProviders}
              />
            )}
            {viewMode === 'edit' && (
              <EditProvider
                setViewMode={setViewMode}
                setAlertOptions={setAlertOptions}
                url={providerUrl}
                getProviders={getProviders}
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
