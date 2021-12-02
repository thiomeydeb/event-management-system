import { useFormik, Form, FormikProvider } from 'formik';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
// material
import { Stack, TextField } from '@mui/material';
import { LoadingButton } from '@mui/lab';
import { addEventTypeSchema } from './validation/event-type';
import { basicAuthBase64Header } from '../../constants/defaultValues';

export default function AddEventTypeForm({ setViewMode, setAlertOptions, url, getEventTypes }) {
  const navigate = useNavigate();
  const formik = useFormik({
    initialValues: {
      name: ''
    },
    validationSchema: addEventTypeSchema,
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
            message: 'Event type added',
            severity: 'success'
          });
          getEventTypes();
          setViewMode('list');
        })
        .catch((error) => {
          setAlertOptions({
            open: true,
            message: 'failed to add event type',
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
            label="Event type name"
            margin="dense"
            {...getFieldProps('name')}
            error={Boolean(touched.name && errors.name)}
            helperText={touched.name && errors.name}
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
