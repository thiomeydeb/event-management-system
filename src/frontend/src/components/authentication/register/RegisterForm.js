import * as Yup from 'yup';
import { useState } from 'react';
import { Icon } from '@iconify/react';
import { useFormik, Form, FormikProvider } from 'formik';
import eyeFill from '@iconify/icons-eva/eye-fill';
import eyeOffFill from '@iconify/icons-eva/eye-off-fill';
import { useNavigate } from 'react-router-dom';
// material
import { Stack, TextField, IconButton, InputAdornment } from '@mui/material';
import { LoadingButton } from '@mui/lab';
import axios from 'axios';
import { apiBasePath, basicAuthBase64Header } from '../../../constants/defaultValues';
import { registrationSchema } from '../../../pages/registration/validation/register';

// ----------------------------------------------------------------------

const registrationUrl = apiBasePath.concat('user');

export default function RegisterForm({ setAlertOptions }) {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);

  const formik = useFormik({
    initialValues: {
      firstName: '',
      lastName: '',
      middleName: '',
      phoneNumber: '',
      email: '',
      password: '',
      confirmPassword: '',
      identificationType: '',
      identificationNumber: '',
      companyName: ''
    },
    validationSchema: registrationSchema,
    onSubmit: (values, formikActions) => {
      axios(registrationUrl, {
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
            message: 'Registration successful',
            severity: 'success'
          });
          navigate('/login', { replace: true });
        })
        .catch((error) => {
          formikActions.setSubmitting(false);
          setAlertOptions({
            open: true,
            message: 'registration failed',
            severity: 'error'
          });
          console.log(error);
        });
    }
  });

  const { errors, touched, handleSubmit, isSubmitting, getFieldProps } = formik;

  return (
    <FormikProvider value={formik}>
      <Form autoComplete="off" noValidate onSubmit={handleSubmit}>
        <Stack spacing={3}>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
            <TextField
              fullWidth
              label="First name"
              {...getFieldProps('firstName')}
              error={Boolean(touched.firstName && errors.firstName)}
              helperText={touched.firstName && errors.firstName}
            />

            <TextField
              fullWidth
              label="Last name"
              {...getFieldProps('lastName')}
              error={Boolean(touched.lastName && errors.lastName)}
              helperText={touched.lastName && errors.lastName}
            />
          </Stack>

          <TextField
            fullWidth
            label="Middle name"
            {...getFieldProps('middleName')}
            error={Boolean(touched.middleName && errors.middleName)}
            helperText={touched.middleName && errors.middleName}
          />

          <TextField
            fullWidth
            autoComplete="username"
            type="email"
            label="Email address"
            {...getFieldProps('email')}
            error={Boolean(touched.email && errors.email)}
            helperText={touched.email && errors.email}
          />

          <TextField
            fullWidth
            label="Phone Number"
            {...getFieldProps('phoneNumber')}
            error={Boolean(touched.phoneNumber && errors.phoneNumber)}
            helperText={touched.phoneNumber && errors.phoneNumber}
          />

          <TextField
            fullWidth
            label="Company"
            {...getFieldProps('companyName')}
            error={Boolean(touched.companyName && errors.companyName)}
            helperText={touched.companyName && errors.companyName}
          />

          <TextField
            fullWidth
            label="Identification Type"
            {...getFieldProps('identificationType')}
            error={Boolean(touched.identificationType && errors.identificationType)}
            helperText={touched.identificationType && errors.identificationType}
          />

          <TextField
            fullWidth
            label="Identification Number"
            {...getFieldProps('identificationNumber')}
            error={Boolean(touched.identificationNumber && errors.identificationNumber)}
            helperText={touched.identificationNumber && errors.identificationNumber}
          />

          <TextField
            fullWidth
            autoComplete="current-password"
            type={showPassword ? 'text' : 'password'}
            label="Password"
            {...getFieldProps('password')}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton edge="end" onClick={() => setShowPassword((prev) => !prev)}>
                    <Icon icon={showPassword ? eyeFill : eyeOffFill} />
                  </IconButton>
                </InputAdornment>
              )
            }}
            error={Boolean(touched.password && errors.password)}
            helperText={touched.password && errors.password}
          />

          <TextField
            fullWidth
            autoComplete="current-password"
            type={showPassword ? 'text' : 'password'}
            label="Password"
            {...getFieldProps('confirmPassword')}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton edge="end" onClick={() => setShowPassword((prev) => !prev)}>
                    <Icon icon={showPassword ? eyeFill : eyeOffFill} />
                  </IconButton>
                </InputAdornment>
              )
            }}
            error={Boolean(touched.confirmPassword && errors.confirmPassword)}
            helperText={touched.confirmPassword && errors.confirmPassword}
          />

          <LoadingButton
            fullWidth
            size="large"
            type="submit"
            variant="contained"
            loading={isSubmitting}
          >
            Register
          </LoadingButton>
        </Stack>
      </Form>
    </FormikProvider>
  );
}
