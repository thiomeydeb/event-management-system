import * as Yup from 'yup'

export const eventTypeSchema = Yup.object({
  name: Yup.string()
    .min(3, 'Name must contain 3 or more characters')
    .max(100, "Name must not be longer that 100 characters")
    .required("Please enter name")
});