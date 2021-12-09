import { useFormik, Form, FormikProvider } from 'formik';
import { useNavigate } from 'react-router-dom';
// material
import { Stack, TextField } from '@mui/material';
import { LoadingButton } from '@mui/lab';
import axios from 'axios';
import { addProviderCategorySchema } from './validation/provider-category';
import { basicAuthBase64Header } from '../../constants/defaultValues';

export default function EditProviderCategoryForm({
  setViewMode,
  setAlertOptions,
  url,
  getProviderCategories,
  updateData
}) {
  const data = updateData === undefined ? {} : updateData;
  const updateUrl = url.concat('/').concat(data.id);

  const formik = useFormik({
    initialValues: {
      name: data.name,
      code: data.code
    },
    validationSchema: addProviderCategorySchema,
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
          getProviderCategories();
          setViewMode('list');
        })
        .catch((error) => {
          setAlertOptions({
            open: true,
            message: 'failed to update provider category',
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
            label="Provider Category Name"
            margin="dense"
            {...getFieldProps('name')}
            error={Boolean(touched.name && errors.name)}
            helperText={touched.name && errors.name}
          />
          <TextField
            fullWidth
            label="Provider Category Code"
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
