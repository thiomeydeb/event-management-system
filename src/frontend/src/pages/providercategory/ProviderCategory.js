import { useEffect, useState } from 'react';
import { Stack, Typography, Button, Container, Card } from '@mui/material';
import { Icon } from '@iconify/react';
import { Link as RouterLink } from 'react-router-dom';
import plusFill from '@iconify/icons-eva/plus-fill';
import editFill from '@iconify/icons-eva/edit-fill';
import axios from 'axios';
import Page from '../../components/Page';
import Scrollbar from '../../components/Scrollbar';
import AddProviderCategory from './AddProviderCategory';
import ListProviderCategory from './ListProviderCategory';
import EditProviderCategory from './EditProviderCategory';
import { basicAuthBase64Header, apiBasePath } from '../../constants/defaultValues';
import Notification from '../../components/custom/Notification';

const providerCategoryUrl = apiBasePath.concat('provider-category');

export default function ProviderCategory() {
  const [viewMode, setViewMode] = useState('list');
  const [alertOptions, setAlertOptions] = useState({
    open: false,
    severity: 'success',
    message: 'Success'
  });
  const [editData, setEditData] = useState({});
  const [providerCategories, setProviderCategories] = useState([{}]);
  const handleClose = () => {
    setAlertOptions({
      open: false,
      severity: 'success',
      message: 'Values fetched successfully'
    });
  };
  const getProviderCategories = (view) => {
    axios
      .get(providerCategoryUrl, {
        headers: {
          authorization: basicAuthBase64Header
        }
      })
      .then((res) => {
        setProviderCategories(res.data.data);
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
    getProviderCategories('list');
  }, []);
  const updateProviderCategoryStatus = (url, values) => {
    axios(url, {
      method: 'PUT',
      headers: {
        authorization: basicAuthBase64Header
      },
      data: values
    })
      .then((res) => {
        getProviderCategories();
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
  const onEditClick = (categoryData) => {
    setEditData(categoryData);
    setViewMode('edit');
  };
  return (
    <Page title="Provider Category | POSH Events">
      <Container>
        <Stack direction="row" alignItems="center" justifyContent="space-between" mb={7}>
          <Typography variant="h4" gutterBottom>
            {viewMode ? 'Provider Category' : 'Add Provider Category'}
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
            Edit Provider Category
          </Button>
          &nbsp;&nbsp;&nbsp;
          <Button
            variant="contained"
            component={RouterLink}
            to="#"
            startIcon={<Icon icon={plusFill} />}
            onClick={() => setViewMode('add')}
          >
            New Provider Category
          </Button>
        </Stack>
        <Card>
          <Scrollbar>
            {/* Display component based on view mode state */}
            {/* {viewMode ? <ListEventType /> : <AddEventType setViewMode={setViewMode} />} */}
            {viewMode === 'list' && (
              <ListProviderCategory
                providerCategories={providerCategories}
                updateProviderCategoryStatus={updateProviderCategoryStatus}
                setViewMode={setViewMode}
                url={providerCategoryUrl}
                onEditClick={onEditClick}
              />
            )}
            {viewMode === 'add' && (
              <AddProviderCategory
                setViewMode={setViewMode}
                setAlertOptions={setAlertOptions}
                url={providerCategoryUrl}
                getProviderCategories={getProviderCategories}
              />
            )}
            {viewMode === 'edit' && (
              <EditProviderCategory
                setViewMode={setViewMode}
                setAlertOptions={setAlertOptions}
                url={providerCategoryUrl}
                getProviderCategories={getProviderCategories}
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
