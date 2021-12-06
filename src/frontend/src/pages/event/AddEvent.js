import { useFormik, Form, FormikProvider } from 'formik';
// material
import { Select, Stack, TextField } from '@mui/material';
import { LoadingButton } from '@mui/lab';
import axios from 'axios';
import { useEffect, useState } from 'react';
import MenuItem from '@mui/material/MenuItem';
import _ from 'lodash';
import { apiBasePath, basicAuthBase64Header } from '../../constants/defaultValues';
import { eventSchema } from './validation/event';

const eventTypeUrl = apiBasePath.concat('event-type');
const providerUrl = apiBasePath.concat('provider');
const textFieldStyle = { width: '49%', float: 'right' };
const selectStyle = { width: '50%', marginTop: '8px' };

export default function AddEvent({ setViewMode, setAlertOptions, url, getEvents }) {
  const [category, setCategory] = useState({ id: 0, name: '' });
  const [selectedVenue, setSelectedVenue] = useState({ id: 0, title: '' });
  const [selectedEventType, setSelectedEventType] = useState({ id: 0, title: '' });
  const [selectedEntertainment, setSelectedEntertainment] = useState({ id: 0, title: '' });
  const [selectedSecurity, setSelectedSecurity] = useState({ id: 0, title: '' });
  const [selectedCatering, setSelectedCatering] = useState({ id: 0, title: '' });
  const [selectedDesign, setSelectedDesign] = useState({ id: 0, title: '' });
  const [selectedMc, setSelectedMc] = useState({ id: 0, title: '' });
  const [eventTypes, setEventTypes] = useState([]);
  const [providers, setProviders] = useState([]);
  const [venues, setVenues] = useState([]);
  const [entertainment, setEntertainment] = useState([]);
  const [catering, setCatering] = useState([]);
  const [security, setSecurity] = useState([]);
  const [mc, setMc] = useState([]);
  const [design, setDesign] = useState([]);

  const getEventTypes = () => {
    axios(eventTypeUrl, {
      method: 'GET',
      headers: {
        authorization: basicAuthBase64Header
      }
    })
      .then((res) => {
        setEventTypes(res.data.data);
      })
      .catch((error) => {
        setAlertOptions({
          open: true,
          message: 'failed to fetch event types',
          severity: 'error'
        });
        console.log(error);
      });
  };

  const getProviders = () => {
    axios(providerUrl, {
      method: 'GET',
      headers: {
        authorization: basicAuthBase64Header
      }
    })
      .then((res) => {
        const providers = res.data.data;
        const venues = _.filter(providers, ['providerCategory.code', 'venue']);
        setVenues(venues);

        const entertainment = _.filter(providers, ['providerCategory.code', 'entertainment']);
        setEntertainment(entertainment);

        const security = _.filter(providers, ['providerCategory.code', 'security']);
        setSecurity(security);

        const design = _.filter(providers, ['providerCategory.code', 'design']);
        setDesign(design);

        const catering = _.filter(providers, ['providerCategory.code', 'catering']);
        setCatering(catering);

        const mc = _.filter(providers, ['providerCategory.code', 'mc']);
        setMc(mc);
        console.log(res.data.data);

        setProviders(res.data.data);
      })
      .catch((error) => {
        setAlertOptions({
          open: true,
          message: 'failed to fetch providers',
          severity: 'error'
        });
        console.log(error);
      });
  };

  useEffect(() => {
    getEventTypes();
    getProviders();
  }, []);

  const getEventIndex = (id, array) => {
    for (let i = 0; i < array.length; i += 1) {
      if (array[i].id === id) {
        return i;
      }
    }
    return '';
  };

  const formik = useFormik({
    initialValues: {
      name: '',
      eventTypeId: 0,
      attendees: 0,
      managementAmount: 0,
      totalAmount: 0,
      otherInformation: '',
      venueId: 0,
      entertainmentId: 0,
      cateringId: 0,
      securityId: 0,
      designId: 0,
      mcId: 0
    },
    validationSchema: eventSchema,
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
            message: 'Event added',
            severity: 'success'
          });
          getEvents();
          setViewMode('list');
        })
        .catch((error) => {
          setAlertOptions({
            open: true,
            message: 'failed to add event',
            severity: 'error'
          });
          console.log(error);
        });
    }
  });

  const { errors, touched, isSubmitting, getFieldProps, setFieldValue, setFieldTouched } = formik;

  const handleChange = (event, setSelectedOption, field) => {
    setSelectedOption(event.target.value);
    setFieldValue(field, event.target.value.id);
    setFieldTouched(field, true);
  };

  return (
    <FormikProvider value={formik}>
      <Form autoComplete="off" noValidate>
        <Stack spacing={3}>
          <TextField
            fullWidth
            label="Event title"
            margin="dense"
            {...getFieldProps('title')}
            error={Boolean(touched.title && errors.title)}
            helperText={touched.title && errors.title}
          />
          <Select
            id="outlined-select-category"
            label="Event Type"
            value={eventTypes[getEventIndex(selectedEventType.id, eventTypes)]}
            onChange={(event) => handleChange(event, setSelectedEventType, 'eventTypeId')}
            error={Boolean(touched.eventTypeId && errors.eventTypeId)}
          >
            {eventTypes.map((option, index) => (
              <MenuItem key={option.id} value={option}>
                {option.name}
              </MenuItem>
            ))}
          </Select>
          <TextField
            fullWidth
            label="Attendees"
            margin="dense"
            {...getFieldProps('attendees')}
            error={Boolean(touched.attendees && errors.attendees)}
            helperText={touched.attendees && errors.attendees}
          />

          <div>
            <TextField
              select
              style={selectStyle}
              id="outlined-select-category"
              label="Venue"
              value={venues[getEventIndex(selectedVenue.id, venues)]}
              onChange={(event) => handleChange(event, setSelectedVenue, 'venueId')}
              error={Boolean(touched.venueId && errors.venueId)}
              helperText={touched.venueId && errors.venueId}
            >
              {venues.map((option, index) => (
                <MenuItem key={option.id} value={option}>
                  {option.title}
                </MenuItem>
              ))}
            </TextField>
            <TextField style={textFieldStyle} id="venueAmountField" label="Amount" margin="dense" />
          </div>

          <div>
            <TextField
              style={selectStyle}
              select
              id="outlined-select-category"
              label="Entertainment"
              value={entertainment[getEventIndex(selectedEntertainment.id, entertainment)]}
              onChange={(event) => handleChange(event, setSelectedEntertainment, 'entertainmentId')}
              error={Boolean(touched.entertainmentId && errors.entertainmentId)}
              helperText={touched.entertainmentId && errors.entertainmentId}
            >
              {entertainment.map((option, index) => (
                <MenuItem key={option.id} value={option}>
                  {option.title}
                </MenuItem>
              ))}
            </TextField>
            <TextField
              style={textFieldStyle}
              id="entertainmentAmountField"
              label="Amount"
              margin="dense"
            />
          </div>
          <div>
            <TextField
              style={selectStyle}
              select
              id="outlined-select-category"
              label="Catering"
              value={catering[getEventIndex(selectedCatering.id, catering)]}
              onChange={(event) => handleChange(event, setSelectedCatering, 'cateringId')}
              error={Boolean(touched.cateringId && errors.cateringId)}
              helperText={touched.cateringId && errors.cateringId}
            >
              {catering.map((option, index) => (
                <MenuItem key={option.id} value={option}>
                  {option.title}
                </MenuItem>
              ))}
            </TextField>
            <TextField
              style={textFieldStyle}
              id="cateringAmountField"
              label="Amount"
              margin="dense"
            />
          </div>
          <div>
            <TextField
              style={selectStyle}
              select
              id="outlined-select-category"
              label="Security"
              value={security[getEventIndex(selectedSecurity.id, security)]}
              onChange={(event) => handleChange(event, setSelectedSecurity, 'securityId')}
              error={Boolean(touched.securityId && errors.securityId)}
              helperText={touched.securityId && errors.securityId}
            >
              {security.map((option, index) => (
                <MenuItem key={option.id} value={option}>
                  {option.title}
                </MenuItem>
              ))}
            </TextField>
            <TextField
              style={textFieldStyle}
              id="securityAmountField"
              label="Amount"
              margin="dense"
            />
          </div>
          <div>
            <TextField
              style={selectStyle}
              select
              id="outlined-select-category"
              label="Design"
              value={design[getEventIndex(selectedDesign.id, design)]}
              onChange={(event) => handleChange(event, setSelectedDesign, 'designId')}
              error={Boolean(touched.designId && errors.designId)}
              helperText={touched.designId && errors.designId}
            >
              {design.map((option, index) => (
                <MenuItem key={option.id} value={option}>
                  {option.title}
                </MenuItem>
              ))}
            </TextField>
            <TextField
              style={textFieldStyle}
              id="designAmountField"
              label="Amount"
              margin="dense"
            />
          </div>
          <div>
            <TextField
              style={selectStyle}
              select
              id="outlined-select-category"
              label="Master of Ceremony"
              value={mc[getEventIndex(selectedMc.id, mc)]}
              onChange={(event) => handleChange(event, setSelectedMc, 'mcId')}
              error={Boolean(touched.mcId && errors.mcId)}
              helperText={touched.mcId && errors.mcId}
            >
              {mc.map((option, index) => (
                <MenuItem key={option.id} value={option}>
                  {option.title}
                </MenuItem>
              ))}
            </TextField>
            <TextField style={textFieldStyle} id="mcAmountField" label="Amount" margin="dense" />
          </div>
          <TextField
            fullWidth
            label="Management Amount"
            margin="dense"
            {...getFieldProps('managementAmount')}
            error={Boolean(touched.managementAmount && errors.managementAmount)}
            helperText={touched.managementAmount && errors.managementAmount}
          />
          <TextField
            fullWidth
            label="Total Amount"
            margin="dense"
            {...getFieldProps('totalAmount')}
            error={Boolean(touched.totalAmount && errors.totalAmount)}
            helperText={touched.totalAmount && errors.totalAmount}
          />
          <TextField
            fullWidth
            label="Other Information"
            margin="dense"
            {...getFieldProps('otherInformation')}
            error={Boolean(touched.otherInformation && errors.otherInformation)}
            helperText={touched.otherInformation && errors.otherInformation}
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
