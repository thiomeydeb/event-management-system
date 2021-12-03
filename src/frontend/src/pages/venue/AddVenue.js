import { useFormik, Form, FormikProvider } from 'formik';
import { useNavigate } from 'react-router-dom';
// material
import { Stack, TextField } from '@mui/material';
import { LoadingButton } from '@mui/lab';
import axios from 'axios';
import { venueSchema } from './validation/venue';
import { basicAuthBase64Header } from '../../constants/defaultValues';

export default function AddVenueForm({ setViewMode, setAlertOptions, url, getVenues }) {
  const navigate = useNavigate();

  const formik = useFormik({
    initialValues: {
      name: '',
      location: '',
      amount: 0
    },
    validationSchema: venueSchema,
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
            message: 'Venue added',
            severity: 'success'
          });
          getVenues();
          setViewMode('list');
        })
        .catch((error) => {
          setAlertOptions({
            open: true,
            message: 'failed to add venue',
            severity: 'error'
          });
          console.log(error);
        });
    }
  });

  const { errors, touched, isSubmitting, getFieldProps } = formik;

  return (
    <FormikProvider value={formik}>
      <Form autoComplete="off" noValidate>
        <Stack spacing={3}>
          <TextField
            fullWidth
            label="Venue"
            margin="dense"
            {...getFieldProps('name')}
            error={Boolean(touched.venueName && errors.venueName)}
            helperText={touched.venueName && errors.venueName}
          />
          <TextField
            fullWidth
            label="Location"
            margin="dense"
            {...getFieldProps('location')}
            error={Boolean(touched.location && errors.location)}
            helperText={touched.location && errors.location}
          />
          <TextField
            fullWidth
            label="Price"
            margin="dense"
            {...getFieldProps('amount')}
            error={Boolean(touched.amount && errors.amount)}
            helperText={touched.amount && errors.amount}
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
