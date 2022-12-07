import React from 'react'
import { yupResolver } from '@hookform/resolvers/yup'
import { Button } from 'react-bootstrap'
import { useForm } from 'react-hook-form'

import FileUploadField from '../custom-fields/FileUploadField'
import CheckboxGroup from '../custom-fields/CheckboxGroup'
import { IS_ASSET } from '../../../common/system-wide-variables'
import { assetValidationSchema } from '../validation/assetValidationSchema'
import { addAssetToExperience, appendFileListToFormData } from '../../../integration/Integration'

const UploadExperienceAsset = props => {
  // console.log('UploadExperienceAsset -> PROPS', props)

  const {
    register,
    handleSubmit,
    resetField,
    watch,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(assetValidationSchema),
    mode: 'onTouched',
    reValidateMode: 'onChange',
    defaultValues: {
      id: props.currentExperience.id,
      file: null,
      thumbnailFile: null,
      isAsset: true,
    },
  })

  const submitForm = async values => {
    // console.log('UploadExperienceAsset -> FORM VALUES', values)
    if (values.file?.length > 0 || values.thumbnailFile?.length > 0) {
      const formData = new FormData()
      appendFileListToFormData(formData, values.file, 'file')
      appendFileListToFormData(formData, values.thumbnailFile, 'thumbnailFile')

      const experience = await addAssetToExperience(props.currentExperience.id, formData, values.isAsset)

      if (!experience.isError) {
        console.log(`L'asset è stato caricato correttamente`)
      } else {
        console.error("Errore nel caricamento dell'asset:", experience.data.message)
      }
    } else {
      return
    }

    handleFieldReset('file')
    handleFieldReset('thumbnailFile')
    onAfterSubmit()
  }

  // Resetta un campo di input
  const handleFieldReset = field => {
    resetField(field)
    document.getElementById(field).value = ''
  }

  const onAfterSubmit = () => {
    props.setReloadToken(new Date().getTime().toString())
  }

  console.log('IS ASSET', watch().isAsset)

  const formToRender = (
    <form onSubmit={handleSubmit(submitForm)}>
      <div className='mt-2 iedx-action-button-space-between iedx-low-action-button'>
        <CheckboxGroup errors={errors} register={register} name='isAsset' labels={[IS_ASSET.label]} values={[IS_ASSET.value]} />
      </div>
      <br />
      <FileUploadField
        watch={watch}
        errors={errors}
        register={register}
        handleFieldReset={handleFieldReset}
        label={watch().isAsset ? 'Aggiorna / Aggiungi scenario' : 'Aggiorna file XML'}
        name='file'
        buttonLabel='Svuota'
        miltipleUploads={false}
      />
      <br />
      <FileUploadField
        watch={watch}
        errors={errors}
        register={register}
        handleFieldReset={handleFieldReset}
        label={
          watch().isAsset
            ? 'Aggiorna / Aggiungi immagine di anteprima (thumbnail) dello scenario'
            : 'Aggiorna immagine di anteprima (thumbnail) intera esperienza'
        }
        name='thumbnailFile'
        buttonLabel='Svuota'
        miltipleUploads={false}
      />
      <br />
      <div className='mt-2 iedx-action-button-space-end iedx-low-action-button'>
        <Button variant='primary' type='submit'>
          Upload
        </Button>
      </div>
    </form>
  )

  return <>{formToRender}</>
}

export default UploadExperienceAsset
