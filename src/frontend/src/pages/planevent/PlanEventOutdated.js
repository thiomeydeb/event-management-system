import { useState } from 'react';
import { Stack, Typography, Button, Container, Card } from '@mui/material';
import { Icon } from '@iconify/react';
import { Link as RouterLink } from 'react-router-dom';
import plusFill from '@iconify/icons-eva/plus-fill';
import eyeFill from '@iconify/icons-eva/eye-fill';
import Page from '../../components/Page';
import Scrollbar from '../../components/Scrollbar';
import ListBookedEvents from './ListBookedEvents';
import EventProgress from './EventProgress';
import PlanEventNow from './PlanEventNow';

export default function PlanEventOutdated() {
  const [viewMode, setViewMode] = useState('list');
  return (
    <Page title="Plan Event | POSH Events">
      <Container>
        <Stack direction="row" alignItems="center" justifyContent="space-between" mb={7}>
          <Typography variant="h4" gutterBottom>
            {viewMode ? 'Plan Event' : 'Event Progress'}
          </Typography>
          <Button
            variant="contained"
            color="warning"
            style={{ marginLeft: 'auto' }}
            component={RouterLink}
            to="#"
            startIcon={<Icon icon={eyeFill} />}
            onClick={() => setViewMode('details')}
          >
            View Event Details
          </Button>
          &nbsp;&nbsp;&nbsp;
          <Button
            variant="contained"
            component={RouterLink}
            to="#"
            startIcon={<Icon icon={plusFill} />}
            onClick={() => setViewMode('plan')}
          >
            Plan Event
          </Button>
          &nbsp;&nbsp;&nbsp;
          <Button
            variant="contained"
            color="warning"
            style={{ marginLeft: 'right' }}
            component={RouterLink}
            to="#"
            startIcon={<Icon icon={eyeFill} />}
            onClick={() => setViewMode('progress')}
          >
            View Event Progress
          </Button>
        </Stack>
        <Card>
          <Scrollbar>
            {/* Display component based on view mode state */}
            {/* {viewMode ? <ListEventType /> : <AddEventType setViewMode={setViewMode} />} */}
            {viewMode === 'list' && <ListBookedEvents />}
            {viewMode === 'progress' && <EventProgress />}
            {viewMode === 'plan' && <PlanEventNow setViewMode={setViewMode} />}
          </Scrollbar>
        </Card>
      </Container>
    </Page>
  );
}
