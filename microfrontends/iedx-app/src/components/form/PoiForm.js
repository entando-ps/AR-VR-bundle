import React, { useState } from 'react'
import { useHistory, Prompt } from 'react-router-dom'
import { Button, Col } from 'react-bootstrap'
import { useForm } from 'react-hook-form'
import { yupResolver } from '@hookform/resolvers/yup'

import { poiValidationSchema } from './validation/poiValidationSchema'
import { addNewPoi, appendFileListToFormData, appendSingleValueToFormData } from '../../integration/Integration'
import { ROUTE_POIS } from '../../common/routing/routes'
import TextField from './custom-fields/TextField'
import TextareaField from './custom-fields/TextareaField'
import FileUploadField from './custom-fields/FileUploadField'

const PoiForm = props => {
  const {
    register,
    handleSubmit,
    resetField,
    watch,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(poiValidationSchema),
    mode: 'onTouched',
    reValidateMode: 'onChange',
    defaultValues: {
      file: props.currentPoi ? props.currentPoi.file : '',
      info: props.currentPoi ? props.currentPoi.info : '',
      name: props.currentPoi ? props.currentPoi.name : '',
      link: props.currentPoi ? props.currentPoi.link : '',
      src: props.currentPoi ? props.currentPoi.src : 'src',
      lat: props.currentPoi?.lat ? props.currentPoi.lat : null,
      lng: props.currentPoi?.lng ? props.currentPoi.lng : null,
      // locations: props.currentPoi?.locations?.length > 0 ? deserializeLocation(props.currentPoi.locations) : {},
    },
  })

  const history = useHistory()
  // Stato che imposta l'azione del form (vedere useEffect)
  const [action, setAction] = useState()
  // Stato per tenere traccia di quando l'utente toglie il focus dal form
  const [isFormFocused, setIsFormFocused] = useState(false)
  // Stato per visualizzare/nascondere l'icona
  const [showThumbnail, setShowThumbnail] = useState(false)

  // si occupa dell'inserimento o della modifica di un poi
  const submitForm = async values => {
    console.log('values', values)

    await handleIsFormFocusedFalse()

    const formData = new FormData()
    appendFileListToFormData(formData, values.file, 'file')
    appendSingleValueToFormData(formData, values.info, 'info')
    appendSingleValueToFormData(formData, values.name, 'name')
    appendSingleValueToFormData(formData, values.link, 'link')
    // appendSingleValueToFormData(formData, values.name, 'src')
    appendSingleValueToFormData(formData, values.lat, 'lat')
    appendSingleValueToFormData(formData, values.lng, 'lng')

    // for (var pair of formData.entries()) {
    //   console.log(pair[0] + ', ' + pair[1])
    // }

    const poi = await addNewPoi(formData)
    // console.log(`test ${poi.data.data.name}`)

    if (!poi.isError) {
      console.log(`Il poi ${poi.data.data.name} è stato inserito correttamente`)
    } else {
      console.error("Errore nell'inserimento di un nuovo poi:", poi.data.message)
    }

    history.push(ROUTE_POIS.path)
  }

  const handleCancel = async () => {
    await handleIsFormFocusedFalse()
    history.push(ROUTE_POIS.path)
  }

  const handleIsFormFocusedTrue = () => {
    setIsFormFocused(true)
  }

  const handleIsFormFocusedFalse = () => {
    setIsFormFocused(false)
  }

  // Resetta un campo di input
  const handleFieldReset = field => {
    resetField(field)
    document.getElementById(field).value = ''
  }

  const formToRender = (
    <>
      <Prompt when={isFormFocused} message={() => 'Sei sicuro di voler abbandonare? I dati non salvati verranno persi.'} />

      <form className='iedx-form-wrapper iedx-poi' onSubmit={handleSubmit(submitForm)} onFocus={handleIsFormFocusedTrue}>
        <div className='justify-content-end iedx-up-action-button'>
          <Col md={2} className='iedx-action-button'></Col>
        </div>

        <Col className='iedx-form'>
          <div className='mb-3'>
            {/* Input di tipo testo */}
            <TextField
              errors={errors}
              register={register}
              name='name'
              label='Nome *'
              placeholder='Inserisci il nome del punto di interesse'
            />
          </div>

          <div className='mb-3'>
            {/* Input di tipo testo */}
            <TextareaField
              errors={errors}
              register={register}
              name='info'
              label='Descrizione *'
              placeholder='Inserisci la descrizione testuale della scheda informativa del punto di interesse'
            />
          </div>

          <div className='mb-3'>
            {/* Input di tipo testo */}
            <TextField errors={errors} register={register} name='link' label='Link' placeholder='Inserisci un eventuale link esterno' />
          </div>

          <div className='mb-3'>
            {/* Input di tipo number */}
            <TextField
              errors={errors}
              register={register}
              name='lat'
              label='Latitudine *'
              placeholder='Inserisci la latitudine del punto di interesse prescelto'
            />
          </div>

          <div className='mb-3'>
            {/* Input di tipo number */}
            <TextField
              errors={errors}
              register={register}
              name='lng'
              label='Longitudine *'
              placeholder='Inserisci la longitudine del punto di interesse prescelto'
            />
          </div>

          {/* Input di tipo file: POI icona  (solo per la creazione) */}
          {/* <div className='mb-3'>
            <FileUploadField
              watch={watch}
              errors={errors}
              register={register}
              handleFieldReset={handleFieldReset}
              label='Icona POI'
              name='file'
              buttonLabel='Svuota'
              miltipleUploads={false}
            />
          </div> */}
        </Col>

        <div className='mt-4 iedx-action-button iedx-low-action-button'>
          {/* Pulsanti */}
          <Button variant='outline-primary' type='button' onClick={handleCancel}>
            Annulla
          </Button>
          <Button variant='primary' type='submit'>
            Salva
          </Button>
        </div>
      </form>
    </>
  )
  return <>{formToRender}</>
}

export default PoiForm
