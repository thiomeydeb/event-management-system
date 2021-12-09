import * as Yup from 'yup';

export const registrationSchema = Yup.object().shape({
  firstName: Yup.string().min(2, 'Too Short!').max(50, 'Too Long!').required('First name required'),
  middleName: Yup.string()
    .min(2, 'Too Short!')
    .max(50, 'Too Long!')
    .required('Middle name required'),
  lastName: Yup.string().min(2, 'Too Short!').max(50, 'Too Long!').required('Last name required'),
  email: Yup.string().email('Email must be a valid email address').required('Email is required'),
  password: Yup.string().required('Password is required'),
  phoneNumber: Yup.string().required('Phone number is required'),
  confirmPassword: Yup.string().required('Confirm password is required'),
  identificationType: Yup.string().required('Identification type is required'),
  identificationNumber: Yup.string().required('Identification number is required'),
  companyName: Yup.string().required('Company name is required')
});
