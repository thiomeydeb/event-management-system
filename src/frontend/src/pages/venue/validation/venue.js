import * as Yup from 'yup';

export const venueSchema = Yup.object({
  name: Yup.string()
    .min(3, 'Name must contain 3 or more characters')
    .max(100, 'Name must not be longer that 100 characters')
    .required('Venue name required'),
  location: Yup.string()
    .min(3, 'Location must contain 3 or more characters')
    .max(100, 'Location must not be longer that 100 characters')
    .required('Location required'),
  amount: Yup.number().min(0, 'Price must be a positive number').required('Price required')
});
