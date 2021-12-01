import { useState } from 'react';
import * as Yup from 'yup';
import { useFormik, Form, FormikProvider } from 'formik';
import { useNavigate } from 'react-router-dom';
// material
import { Stack, TextField } from '@mui/material';
import { LoadingButton } from '@mui/lab';
import MenuItem from '@mui/material/MenuItem';

export default function EditProviderForm({ setViewMode }) {
  const navigate = useNavigate();
  const [category, setCategory] = useState('Security');

  const providerCategories = [
    {
      value: 'Security',
      label: 'Security'
    },
    {
      value: 'Catering',
      label: 'Catering'
    },
    {
      value: 'Entertainment',
      label: 'Entertainment'
    },
    {
      value: 'Design',
      label: 'Design'
    },
    {
      value: 'MC',
      label: 'MC'
    }
  ];

  const handleChange = (event) => {
    setCategory(event.target.value);
  };

  const editProviderSchema = Yup.object().shape({
    eventTypeName: Yup.string()
      .min(2, 'Too Short!')
      .max(50, 'Too Long!')
      .required('Provider name required')
  });

  const formik = useFormik({
    initialValues: {
      firstName: '',
      lastName: '',
      email: '',
      password: ''
    },
    validationSchema: editProviderSchema,
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
            label="Provider name"
            margin="dense"
            {...getFieldProps('providerName')}
            error={Boolean(touched.providerName && errors.providerName)}
            helperText={touched.providerName && errors.providerName}
          />
          <TextField
            fullWidth
            label="Cost"
            margin="dense"
            {...getFieldProps('cost')}
            error={Boolean(touched.cost && errors.cost)}
            helperText={touched.cost && errors.cost}
          />
          <TextField
            id="outlined-select-currency"
            select
            label="Category"
            value={category}
            onChange={handleChange}
            helperText="Please select provider category"
          >
            {providerCategories.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                {option.label}
              </MenuItem>
            ))}
          </TextField>
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
