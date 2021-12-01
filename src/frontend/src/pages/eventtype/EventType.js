import { useState, useEffect } from 'react';
import { Stack, Typography, Button, Container, Card } from '@mui/material';
import { Icon } from '@iconify/react';
import { Link as RouterLink } from 'react-router-dom';
import plusFill from '@iconify/icons-eva/plus-fill';
import axios from 'axios';
import Page from '../../components/Page';
import Scrollbar from '../../components/Scrollbar';
import AddEventType from './AddEventType';
import ListEventType from './ListEventType';

const eventTypeUrl = 'http://localhost:8080/api/v1/event-type';

export default function EventType() {
  const [viewMode, setViewMode] = useState(true);
  const [eventTypes, setEventTypes] = useState([]);
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
      })
      .catch((error) => {
        console.log(error.toJSON);
      });
  };
  useEffect(() => {
    getEventTypes();
  }, []);
  return (
    <Page title="Event Type | POSH Events">
      <Container>
        <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
          <Typography variant="h4" gutterBottom>
            {viewMode ? 'Event Type' : 'Add Event Type'}
          </Typography>
          <Button
            variant="contained"
            component={RouterLink}
            to="#"
            startIcon={<Icon icon={plusFill} />}
            onClick={() => setViewMode(false)}
          >
            New Event Type
          </Button>
        </Stack>
        <Card>
          <Scrollbar>
            {/* Display component based on view mode state */}
            {viewMode ? (
              <ListEventType eventTypes={eventTypes} />
            ) : (
              <AddEventType setViewMode={setViewMode} />
            )}
          </Scrollbar>
        </Card>
      </Container>
    </Page>
  );
}
