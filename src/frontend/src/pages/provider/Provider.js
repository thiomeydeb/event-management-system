import { useState } from 'react';
import { Stack, Typography, Button, Container, Card } from '@mui/material';
import { Icon } from '@iconify/react';
import { Link as RouterLink } from 'react-router-dom';
import plusFill from '@iconify/icons-eva/plus-fill';
import editFill from '@iconify/icons-eva/edit-fill';
import Page from '../../components/Page';
import Scrollbar from '../../components/Scrollbar';
import AddProvider from './AddProvider';
import ListProviders from './ListProviders';
import EditProvider from './EditProvider';

export default function Provider() {
  const [viewMode, setViewMode] = useState('list');
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
            {viewMode === 'list' && <ListProviders />}
            {viewMode === 'add' && <AddProvider />}
            {viewMode === 'edit' && <EditProvider setViewMode={setViewMode} />}
          </Scrollbar>
        </Card>
      </Container>
    </Page>
  );
}
