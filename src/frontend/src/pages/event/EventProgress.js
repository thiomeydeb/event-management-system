import * as React from 'react';
import PropTypes from 'prop-types';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import { styled } from '@mui/material/styles';
import Paper from '@mui/material/Paper';
import BasicEventDetails from './BasicEventDetails';
import ProgressDetails from './ProgressDetails';
import ProviderDetails from './ProviderDetails';
import ManageProviders from './ManageProviders';
import AssignPlanner from './AssignPlanner';
import EventGreening from './EventGreening';

const Item = styled(Paper)(({ theme }) => ({
  ...theme.typography.body2,
  padding: theme.spacing(1),
  textAlign: 'center',
  color: theme.palette.text.secondary
}));

function TabPanel(props) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box sx={{ p: 3 }}>
          <Typography>{children}</Typography>
        </Box>
      )}
    </div>
  );
}

TabPanel.propTypes = {
  children: PropTypes.node,
  index: PropTypes.number.isRequired,
  value: PropTypes.number.isRequired
};

function a11yProps(index) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`
  };
}

export default function EventProgress({
  eventData,
  setViewMode,
  userType,
  updateProviderStatus,
  onLinkPlanner,
  setAlertOptions
}) {
  let statusColor = 'error';
  let statusMessage = 'undefined';
  let greeningStatusColor = 'error';
  let greeningStatusMessage = 'undefined';
  if (eventData.status === 0) {
    statusColor = 'warning';
    statusMessage = 'Pending';
  } else if (eventData.status === 1) {
    statusColor = 'info';
    statusMessage = 'Planner Assigned';
  } else if (eventData.status === 2) {
    statusColor = 'primary';
    statusMessage = 'In progress';
  } else if (eventData.status === 3) {
    statusColor = 'success';
    statusMessage = 'Complete';
  } else if (eventData.status === 4) {
    statusColor = 'error';
    statusMessage = 'Cancelled';
  }

  if (eventData.greeningStatus === 0) {
    greeningStatusColor = 'warning';
    greeningStatusMessage = 'Pending';
  } else if (eventData.greeningStatus === 1) {
    greeningStatusColor = 'info';
    greeningStatusMessage = 'Setting up';
  } else if (eventData.greeningStatus === 2) {
    greeningStatusColor = 'primary';
    greeningStatusMessage = 'In progress';
  } else if (eventData.greeningStatus === 3) {
    greeningStatusColor = 'success';
    greeningStatusMessage = 'Complete';
  } else if (eventData.greeningStatus === 4) {
    greeningStatusColor = 'error';
    statusMessage = 'Cancelled';
  }
  const [value, setValue] = React.useState(0);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };
  return (
    <Box sx={{ width: '100%' }}>
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs value={value} onChange={handleChange} aria-label="basic tabs example">
          <Tab label="Event Details" {...a11yProps(0)} />
          <Tab label="Provider Details" {...a11yProps(1)} />
          <Tab label="Progress Details" {...a11yProps(2)} />
          {(userType === 'planner' || userType === 'manager') && (
            <Tab label="Manage Selected Providers" {...a11yProps(3)} />
          )}
          {userType === 'manager' && <Tab label="Assign Planner" {...a11yProps(4)} />}
          {userType === 'manager' && <Tab label="Event Greening" {...a11yProps(5)} />}
        </Tabs>
      </Box>
      <TabPanel value={value} index={0}>
        <BasicEventDetails
          eventData={eventData}
          greeningStatusMessage={greeningStatusMessage}
          statusMessage={statusMessage}
          greeningColor={greeningStatusColor}
          statusColor={statusColor}
        />
      </TabPanel>
      <TabPanel value={value} index={1}>
        <ProviderDetails eventData={eventData} />
      </TabPanel>
      <TabPanel value={value} index={2}>
        <ProgressDetails eventData={eventData} />
      </TabPanel>
      {(userType === 'planner' || userType === 'manager') && (
        <TabPanel value={value} index={3}>
          <ManageProviders eventData={eventData} onUpdateProviderStatus={updateProviderStatus} />
        </TabPanel>
      )}
      {userType === 'manager' && (
        <div>
          <TabPanel value={value} index={4}>
            <AssignPlanner
              onLinkPlanner={onLinkPlanner}
              setAlertOptions={setAlertOptions}
              eventData={eventData}
            />
          </TabPanel>
          <TabPanel value={value} index={5}>
            <EventGreening />
          </TabPanel>
        </div>
      )}
    </Box>
  );
}
