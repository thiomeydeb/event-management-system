import { useState } from 'react';
import { Stack, Typography, Button, Container, Card } from '@mui/material';
import { Icon } from '@iconify/react';
import { Link as RouterLink } from 'react-router-dom';
import plusFill from '@iconify/icons-eva/plus-fill';
import editFill from '@iconify/icons-eva/edit-fill';
import Page from '../../components/Page';
import Scrollbar from '../../components/Scrollbar';
import ListVenues from './ListVenues';
import AddVenue from './AddVenue';
import EditVenue from './EditVenue';

export default function Venue() {
  const [viewMode, setViewMode] = useState('list');
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
            {viewMode === 'list' && <ListVenues />}
            {viewMode === 'add' && <AddVenue />}
            {viewMode === 'edit' && <EditVenue setViewMode={setViewMode} />}
          </Scrollbar>
        </Card>
      </Container>
    </Page>
  );
}
