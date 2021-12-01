import * as Yup from 'yup';
import { useFormik, Form, FormikProvider } from 'formik';
import { useNavigate } from 'react-router-dom';
// material
import { Stack, TextField } from '@mui/material';
import { LoadingButton } from '@mui/lab';

export default function AddProviderCategoryForm({ setViewMode }) {
  const navigate = useNavigate();

  const addProviderCategorySchema = Yup.object().shape({
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
    validationSchema: addProviderCategorySchema,
    onSubmit: () => {
      navigate('/dashboard', { replace: true });
    }
  });

  const { errors, touched, isSubmitting, getFieldProps } = formik;

  return (
    <FormikProvider value={formik}>
      <Form autoComplete="off" noValidate onSubmit={() => setViewMode('list')}>
        <Stack spacing={3}>
          <TextField
            fullWidth
            label="Provider Category name"
            margin="dense"
            {...getFieldProps('providerCategoryName')}
            error={Boolean(touched.providerCategoryName && errors.providerCategoryName)}
            helperText={touched.providerCategoryName && errors.providerCategoryName}
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
