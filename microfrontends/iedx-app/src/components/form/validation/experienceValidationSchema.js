import * as Yup from 'yup'

export const experienceValidationSchemaPOST = Yup.object().shape({
  name: Yup.string()
    .min(3, 'Il nome deve contenere almeno 3 caratteri')
    .max(25, 'Il nome non deve superare i 25 caratteri')
    .required('Il campo è obbligatorio'),
  description: Yup.string(),
  xml: Yup.mixed()
    .test('name', 'Il campo è obbligatorio', value => {
      return value[0] && value[0].name !== ''
    })
    .test('fileFormat', 'Il file deve essere in formato .xml', value => {
      if (value) {
        return value.length > 0 && value[0].type === 'text/xml'
      } else {
        return true
      }
    })
    .required('Il campo è obbligatorio'),
  thumbnail: Yup.mixed()
    .test('fileFormat', 'Il file deve essere una immagine', value => {
      if (value && value.length > 0) {
        return (
          value[0].type === 'image/jpeg' || value[0].type === 'image/jpg' || value[0].type === 'image/png' || value[0].type === 'image/gif'
        )
      } else {
        return true
      }
    })
    .notRequired()
    .nullable(),
})

export const experienceValidationSchemaPUT = Yup.object().shape({
  name: Yup.string()
    .min(3, 'Il nome deve contenere almeno 3 caratteri')
    .max(25, 'Il nome non deve superare i 25 caratteri')
    .required('Il campo è obbligatorio'),
  description: Yup.string(),
})
