import * as Yup from 'yup'

export const poiValidationSchema = Yup.object().shape({
  name: Yup.string()
  .min(6, 'Il nome deve contenere almeno 6 caratteri')
  .max(25, 'Il nome non deve superare i 25 caratteri')
  .required('Il campo è obbligatorio'),

  info: Yup.string()
  .min(6, 'Il campo informazioni deve contenere almeno 6 caratteri')
  .required('Il campo è obbligatorio'),

  lat: Yup.number()
  .test(
    "maxDigitsAfterDecimal",
    "Il numero inserito deve avere massimo 9 decimali",
    (number) => /^\d+(\.\d{1,9})?$/.test(number)
  )
  .typeError('Devi inserire coordinate valide es.54.6876 ')
  .min(0, 'Min value 0.')
  .required('Il campo è obbligatorio'),

  lng: Yup.number()
  .test(
    "maxDigitsAfterDecimal",
    "Il numero inserito deve avere massimo 9 decimali",
    (number) => /^\d+(\.\d{1,9})?$/.test(number)
  )
  .typeError('Devi inserire coordinate valide es.54.6876 ')
  .min(0, 'Min value 0.')
  .required('Il campo è obbligatorio'),

  file: Yup.mixed()
  .test('fileFormat', 'Il file deve essere una immagine', value => {
    if (value && value.length > 0) {
      return (
        value[0].type === 'image/jpeg' || value[0].type === 'image/jpg'
        || value[0].type === 'image/png' || value[0].type === 'image/gif'
      )
    } else {
      return true
    }
  })
  .required('Il campo è obbligatorio'),
})

