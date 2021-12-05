import * as Yup from 'yup';

export const addProviderCategorySchema = Yup.object({
  name: Yup.string()
    .min(3, 'Name must contain 3 or more characters')
    .max(20, 'Name must not be longer that 20 characters')
    .required('Event category name required'),
  code: Yup.string()
    .min(1, 'Code must contain 1 or more characters')
    .max(20, 'Code must not be longer that 20 characters')
    .required('Event category code required')
});
