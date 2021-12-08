import { useFormik, Form, FormikProvider } from 'formik';
import { useNavigate } from 'react-router-dom';
// material
import { Stack, TextField } from '@mui/material';
import { LoadingButton } from '@mui/lab';
import axios from 'axios';
import { addProviderCategorySchema } from './validation/provider-category';
import { basicAuthBase64Header } from '../../constants/defaultValues';

export default function AddProviderCategoryForm({
  setViewMode,
  setAlertOptions,
  url,
  getProviderCategories
}) {
  const formik = useFormik({
    initialValues: {
      name: '',
      code: ''
    },
    validationSchema: addProviderCategorySchema,
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
            message: 'Category added',
            severity: 'success'
          });
          getProviderCategories();
          setViewMode('list');
        })
        .catch((error) => {
          setAlertOptions({
            open: true,
            message: 'failed to add category',
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
            label="Event Category Name"
            margin="dense"
            {...getFieldProps('name')}
            error={Boolean(touched.name && errors.name)}
            helperText={touched.name && errors.name}
          />
          <TextField
            fullWidth
            label="Event Category Code"
            margin="dense"
            {...getFieldProps('code')}
            error={Boolean(touched.code && errors.code)}
            helperText={touched.code && errors.code}
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
