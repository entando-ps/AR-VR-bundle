import * as Yup from 'yup'

export const assetValidationSchema = Yup.object().shape({
  thumbnailFile: Yup.mixed()
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
