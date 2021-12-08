import { useState, useEffect } from 'react';
import { Button, Card, Container, Stack, Typography } from '@mui/material';
import axios from 'axios';
import LogsTable from './LogsTable';
import { apiBasePath, basicAuthBase64Header } from '../../constants/defaultValues';
import Scrollbar from '../../components/Scrollbar';
import Notification from '../../components/custom/Notification';
import Page from '../../components/Page';

const logsUrl = apiBasePath.concat('audit/logs');

export default function EventLogs() {
  const [logs, setLogs] = useState([]);
  const [alertOptions, setAlertOptions] = useState({
    open: false,
    severity: 'success',
    message: 'Success'
  });

  const getLogs = (view) => {
    axios
      .get(logsUrl, {
        headers: {
          authorization: basicAuthBase64Header
        }
      })
      .then((res) => {
        console.log(res);
        setLogs(res.data.data);
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
    getLogs();
  }, []);
  return (
    <Page title="Event | POSH Events">
      <Container>
        <Stack direction="row" alignItems="center" justifyContent="space-between" mb={7}>
          <Typography variant="h4" gutterBottom>
            Event Logs
          </Typography>
        </Stack>
        <Card>
          <Scrollbar>
            <LogsTable logs={logs} />
          </Scrollbar>
        </Card>
        <Notification
          open={alertOptions.open}
          severity={alertOptions.severity}
          message={alertOptions.message}
        />
      </Container>
    </Page>
  );
}
