import { useState } from 'react';
import { Box, TextField } from '@mui/material';
import axios from 'axios';
import { basicAuthBase64Header, apiBasePath } from '../../constants/defaultValues';
import PlannerUserTable from './PlannerUserTable';

const userUrl = apiBasePath.concat('user');

export default function AssignPlanner({ eventData, onLinkPlanner, setAlertOptions }) {
  const [users, setUsers] = useState([]);

  const getUsers = (query) => {
    axios(userUrl, {
      method: 'GET',
      headers: {
        authorization: basicAuthBase64Header
      },
      params: {
        query
      }
    })
      .then((res) => {
        setUsers(res.data.data);
      })
      .catch((error) => {
        setAlertOptions({
          open: true,
          message: 'failed to fetch users',
          severity: 'error'
        });
        console.log(error);
      });
  };
  return (
    <Box>
      <TextField
        fullWidth
        label="Search by email, name or phone number"
        onChange={(e) => getUsers(e.target.value)}
      />
      <PlannerUserTable onLinkPlanner={onLinkPlanner} users={users} eventData={eventData} />
    </Box>
  );
}
