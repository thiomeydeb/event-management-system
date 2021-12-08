import * as Yup from 'yup';

export const eventSchema = Yup.object({
  name: Yup.string()
    .min(3, 'Name must contain 3 or more characters')
    .max(100, 'Name must not be longer that 100 characters')
    .required('Name required'),
  eventTypeId: Yup.number().min(0, 'Select event type').required('Event type required'),
  attendees: Yup.number()
    .min(0, 'Attendees must be a positive number')
    .required('Attendees required'),
  managementAmount: Yup.number()
    .min(0, 'Amount must be a positive number')
    .required('amount required'),
  totalAmount: Yup.number().min(0, 'Amount must be a positive number').required('amount required'),
  otherInformation: Yup.string()
    .min(3, 'Information must contain 3 or more characters')
    .max(100, 'Information must not be longer that 100 characters')
    .required('Information required'),
  venueId: Yup.number().min(0, 'Select venue').required('Venue required'),
  entertainmentId: Yup.number().min(0, 'Select entertainment').required('Entertainment required'),
  cateringId: Yup.number().min(0, 'Select catering').required('Catering required'),
  securityId: Yup.number().min(0, 'Select security').required('Security required'),
  designId: Yup.number().min(0, 'Select design').required('Design required'),
  mcId: Yup.number().min(0, 'Select Mc').required('Mc required')
});
