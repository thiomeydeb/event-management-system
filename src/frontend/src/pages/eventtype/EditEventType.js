import { useFormik, Form, FormikProvider } from 'formik';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
// material
import { Stack, TextField } from '@mui/material';
import { LoadingButton } from '@mui/lab';
import { addEventTypeSchema } from './validation/event-type';
import { basicAuthBase64Header } from '../../constants/defaultValues';

export default function EditEventTypeForm({
  setViewMode,
  setAlertOptions,
  url,
  getEventTypes,
  updateData
}) {
  const navigate = useNavigate();
  const data = updateData === undefined ? {} : updateData;
  const updateUrl = url.concat('/').concat(data.id);

  const formik = useFormik({
    initialValues: {
      name: data.name
    },
    validationSchema: addEventTypeSchema,
    onSubmit: (values, formikActions) => {
      console.log(values);
      axios(updateUrl, {
        method: 'PUT',
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
            message: 'update successful',
            severity: 'success'
          });
          getEventTypes();
          setViewMode('list');
        })
        .catch((error) => {
          setAlertOptions({
            open: true,
            message: 'failed to update event type',
            severity: 'error'
          });
          formikActions.setSubmitting(false);
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
            label="Event type "
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
