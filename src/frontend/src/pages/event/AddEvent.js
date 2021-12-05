import { useFormik, Form, FormikProvider } from 'formik';
// material
import { Select, Stack, TextField } from '@mui/material';
import { LoadingButton } from '@mui/lab';
import axios from 'axios';
import { useEffect, useState } from 'react';
import MenuItem from '@mui/material/MenuItem';
import { apiBasePath, basicAuthBase64Header } from '../../constants/defaultValues';
import { eventSchema } from './validation/event';

const eventTypeUrl = apiBasePath.concat('event-type');

export default function AddEvent({ setViewMode, setAlertOptions, url, getProviders }) {
  const [category, setCategory] = useState({ id: 0, name: '' });
  const [eventTypes, setEventTypes] = useState([]);

  const getEventTypes = () => {
    axios(eventTypeUrl, {
      method: 'GET',
      headers: {
        authorization: basicAuthBase64Header
      }
    })
      .then((res) => {
        setEventTypes(res.data.data);
      })
      .catch((error) => {
        setAlertOptions({
          open: true,
          message: 'failed to fetch event types',
          severity: 'error'
        });
        console.log(error);
      });
  };

  useEffect(() => {
    getEventTypes();
  }, []);

  const getEventIndex = (id) => {
    for (let i = 0; i < eventTypes.length; i += 1) {
      if (eventTypes[i].id === id) {
        return i;
      }
    }
    return '';
  };

  const formik = useFormik({
    initialValues: {
      title: '',
      categoryId: 0,
      cost: 0
    },
    validationSchema: eventSchema,
    onSubmit: (values, formikActions) => {
      axios(url, {
        method: 'POST',
        data: values,
        headers: {
          authorization: basicAuthBase64Header
        }
      })
        .then((res) => {
          formikActions.resetForm();
          formikActions.setSubmitting(false);
          setAlertOptions({
            open: true,
            message: 'Event added',
            severity: 'success'
          });
          getProviders();
          setViewMode('list');
        })
        .catch((error) => {
          setAlertOptions({
            open: true,
            message: 'failed to add event',
            severity: 'error'
          });
          console.log(error);
        });
    }
  });

  const { errors, touched, isSubmitting, getFieldProps, setFieldValue, setFieldTouched } = formik;

  const handleChange = (event) => {
    setCategory(event.target.value);
    setFieldValue('categoryId', event.target.value.id);
    setFieldTouched('categoryId', true);
  };

  return (
    <FormikProvider value={formik}>
      <Form autoComplete="off" noValidate>
        <Stack spacing={3}>
          <TextField
            fullWidth
            label="Event title"
            margin="dense"
            {...getFieldProps('title')}
            error={Boolean(touched.title && errors.title)}
            helperText={touched.title && errors.title}
          />
          <Select
            id="outlined-select-category"
            label="Category"
            value={eventTypes[getEventIndex(category.id)]}
            onChange={handleChange}
            error={Boolean(touched.categoryId && errors.categoryId)}
          >
            {eventTypes.map((option, index) => (
              <MenuItem key={option.id} value={option}>
                {option.name}
              </MenuItem>
            ))}
          </Select>
          <TextField
            fullWidth
            label="Cost"
            margin="dense"
            {...getFieldProps('cost')}
            error={Boolean(touched.cost && errors.cost)}
            helperText={touched.cost && errors.cost}
          />
          <LoadingButton
            fullWidth
            size="large"
            type="submit"
            variant="contained"
            loading={isSubmitting}
          >
            Save
          </LoadingButton>
        </Stack>
      </Form>
    </FormikProvider>
  );
}
