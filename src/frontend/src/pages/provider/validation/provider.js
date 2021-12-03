import * as Yup from 'yup';

export const providerSchema = Yup.object({
  title: Yup.string()
    .min(3, 'Title must contain 3 or more characters')
    .max(100, 'Title must not be longer that 100 characters')
    .required('Title required'),
  categoryId: Yup.number().min(0, 'Select category Id').required('Category required'),
  cost: Yup.number().min(0, 'Price must be a positive number').required('Price required')
});
