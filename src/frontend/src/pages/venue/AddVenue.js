import * as Yup from 'yup';
import { useFormik, Form, FormikProvider } from 'formik';
import { useNavigate } from 'react-router-dom';
// material
import { Stack, TextField } from '@mui/material';
import { LoadingButton } from '@mui/lab';

export default function AddVenueForm({ setViewMode }) {
  const navigate = useNavigate();

  const addVenueSchema = Yup.object().shape({
    eventTypeName: Yup.string()
      .min(2, 'Too Short!')
      .max(50, 'Too Long!')
      .required('Venue name required')
  });

  const formik = useFormik({
    initialValues: {
      firstName: '',
      lastName: '',
      email: '',
      password: ''
    },
    validationSchema: addVenueSchema,
    onSubmit: () => {
      navigate('/dashboard', { replace: true });
    }
  });

  const { errors, touched, isSubmitting, getFieldProps } = formik;

  return (
    <FormikProvider value={formik}>
      <Form autoComplete="off" noValidate onSubmit={() => setViewMode('add')}>
        <Stack spacing={3}>
          <TextField
            fullWidth
            label="Venue name"
            margin="dense"
            {...getFieldProps('venueName')}
            error={Boolean(touched.venueName && errors.venueName)}
            helperText={touched.venueName && errors.venueName}
          />
          <TextField
            fullWidth
            label="Location name"
            margin="dense"
            {...getFieldProps('LocationName')}
            error={Boolean(touched.LocationName && errors.LocationName)}
            helperText={touched.LocationName && errors.LocationName}
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
