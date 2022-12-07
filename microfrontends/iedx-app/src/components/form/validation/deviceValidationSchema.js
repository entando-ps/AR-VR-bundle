import * as Yup from 'yup'

export const deviceValidationSchema = Yup.object().shape({
  deviceId: Yup.string().max(55, "L'ID Univoco non deve superare i 55 caratteri").required('Il campo è obbligatorio'),
  name: Yup.string()
    .min(3, 'Il nome deve contenere almeno 3 caratteri')
    .max(25, 'Il nome non deve superare i 25 caratteri')
    .required('Il campo è obbligatorio'),
  note: Yup.string(),
})
