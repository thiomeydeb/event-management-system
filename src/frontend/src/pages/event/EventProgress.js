import { useState, useEffect } from 'react';
import { useFormik, Form, FormikProvider } from 'formik';
// material
import { Stack, TextField, Select } from '@mui/material';
import { LoadingButton } from '@mui/lab';
import MenuItem from '@mui/material/MenuItem';
import axios from 'axios';
import { eventSchema } from './validation/event';
import { basicAuthBase64Header, apiBasePath } from '../../constants/defaultValues';

const providerCategoryUrl = apiBasePath.concat('provider-category');

export default function EventProgress({
  setViewMode,
  setAlertOptions,
  url,
  getProviders,
  updateData
}) {
  const data = updateData === undefined ? {} : updateData;
  const categoryId = data.providerCategory ? data.providerCategory.id : 0;
  const categoryName = data.providerCategory ? data.providerCategory.name : '';
  const [category, setCategory] = useState({ id: categoryId, name: categoryName });
  const [providerCategories, setProviderCategories] = useState([
    { id: categoryId, name: categoryName }
  ]);
  const updateUrl = url.concat('/').concat(data.id);

  const getProviderCategories = () => {
    axios(providerCategoryUrl, {
      method: 'GET',
      headers: {
        authorization: basicAuthBase64Header
      }
    })
      .then((res) => {
        setProviderCategories(res.data.data);
      })
      .catch((error) => {
        setAlertOptions({
          open: true,
          message: 'failed to fetch provider category data',
          severity: 'error'
        });
        console.log(error);
      });
  };

  useEffect(() => {
    getProviderCategories();
  }, []);

  const getCategoryIndex = (id) => {
    for (let i = 0; i < providerCategories.length; i += 1) {
      if (providerCategories[i].id === id) {
        return i;
      }
    }
    return '';
  };

  const formik = useFormik({
    initialValues: {
      title: updateData.title,
      cost: updateData.cost,
      categoryId
    },
    validationSchema: eventSchema,
    onSubmit: (values, formikActions) => {
      console.log(values);
      formikActions.setSubmitting(false);
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
          getProviders();
          setViewMode('list');
        })
        .catch((error) => {
          setAlertOptions({
            open: true,
            message: 'failed to update provider',
            severity: 'error'
          });
          formikActions.setSubmitting(false);
          console.log(error);
        });
    }
  });

  const { errors, touched, isSubmitting, getFieldProps, setFieldValue, setFieldTouched } = formik;

  const handleChange = (event) => {
    setCategory(event.target.value);
    setFieldValue('categoryId', event.target.value.id);
    setFieldTouched('categoryId', true);
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
            label="Category"
            value={providerCategories[getCategoryIndex(category.id)]}
            onChange={handleChange}
            error={Boolean(touched.categoryId && errors.categoryId)}
          >
            {providerCategories.map((option, index) => (
              <MenuItem key={option.id} value={option}>
                {option.name}
              </MenuItem>
            ))}
          </Select>
          <TextField
            fullWidth
            label="Cost"
            margin="dense"
            {...getFieldProps('cost')}
            error={Boolean(touched.cost && errors.cost)}
            helperText={touched.cost && errors.cost}
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
