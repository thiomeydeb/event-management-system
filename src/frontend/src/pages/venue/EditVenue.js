import * as Yup from 'yup';
import { useFormik, Form, FormikProvider } from 'formik';
import { useNavigate } from 'react-router-dom';
// material
import { Stack, TextField } from '@mui/material';
import { LoadingButton } from '@mui/lab';

export default function EditVenueForm({ setViewMode }) {
  const navigate = useNavigate();

  const editVenueSchema = Yup.object().shape({
    eventTypeName: Yup.string()
      .min(2, 'Too Short!')
      .max(50, 'Too Long!')
      .required('Add Provider Category name required')
  });

  const formik = useFormik({
    initialValues: {
      firstName: '',
      lastName: '',
      email: '',
      password: ''
    },
    validationSchema: editVenueSchema,
    onSubmit: () => {
      navigate('/dashboard', { replace: true });
    }
  });

  const { errors, touched, isSubmitting, getFieldProps } = formik;

  return (
    <FormikProvider value={formik}>
      <Form autoComplete="off" noValidate onSubmit={() => setViewMode('edit')}>
        <Stack spacing={3}>
          <TextField
            fullWidth
            label="Venue"
            margin="dense"
            {...getFieldProps('venueName')}
            error={Boolean(touched.venueName && errors.venueName)}
            helperText={touched.venueName && errors.venueName}
          />
          <TextField
            fullWidth
            label="Location"
            margin="dense"
            {...getFieldProps('locationName')}
            error={Boolean(touched.locationName && errors.locationName)}
            helperText={touched.locationName && errors.locationName}
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
