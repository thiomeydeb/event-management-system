import * as Yup from 'yup';
import { useFormik, Form, FormikProvider } from 'formik';
import { useNavigate } from 'react-router-dom';
// material
import { Stack, TextField } from '@mui/material';
import { LoadingButton } from '@mui/lab';

export default function AddEventTypeForm({ setViewMode }) {
  const navigate = useNavigate();

  const addEventTypeSchema = Yup.object().shape({
    eventTypeName: Yup.string()
      .min(2, 'Too Short!')
      .max(50, 'Too Long!')
      .required('Event type name required')
  });

  const formik = useFormik({
    initialValues: {
      firstName: '',
      lastName: '',
      email: '',
      password: ''
    },
    validationSchema: addEventTypeSchema,
    onSubmit: () => {
      navigate('/dashboard', { replace: true });
    }
  });

  const { errors, touched, isSubmitting, getFieldProps } = formik;

  return (
    <FormikProvider value={formik}>
      <Form autoComplete="off" noValidate onSubmit={() => setViewMode(true)}>
        <Stack spacing={3}>
          <TextField
            fullWidth
            label="Event type name"
            margin="dense"
            {...getFieldProps('eventTypeName')}
            error={Boolean(touched.eventTypeName && errors.eventTypeName)}
            helperText={touched.eventTypeName && errors.eventTypeName}
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
