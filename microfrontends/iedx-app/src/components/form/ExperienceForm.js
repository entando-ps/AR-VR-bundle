import React, { useEffect, useState } from 'react'
import { Button, Col } from 'react-bootstrap'
import { Prompt, useHistory } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { yupResolver } from '@hookform/resolvers/yup'

import Logotrash from '../../images/trashbin.png'
import { ROUTE_EXPERIENCES } from '../../common/routing/routes'
import { experienceValidationSchemaPOST, experienceValidationSchemaPUT } from './validation/experienceValidationSchema'
import TextField from './custom-fields/TextField'
import TextareaField from './custom-fields/TextareaField'
import FileUploadField from './custom-fields/FileUploadField'
import {
  addNewExperience,
  appendFileListToFormData,
  appendSingleValueToFormData,
  deleteExperience,
  deserializeProfiles,
  editExperience,
  serializeProfiles,
} from '../../integration/Integration'
import ToggleSwitch from './custom-fields/ToggleSwitch'
import CheckboxGroup from './custom-fields/CheckboxGroup'
import ExperienceAssets from './assets/ExperienceAssets'
import UploadExperienceAsset from './assets/UploadExperienceAsset'
import { ACTIVE, ADULT, CHILD, CREATE, ELDER, INACTIVE, TEEN, UPDATE, YOUNG_ADULT } from '../../common/system-wide-variables'
import ModalElement from '../layout/ModalElement'

const ExperienceForm = props => {
  const history = useHistory()
  // Configurazione React Hook Form
  const {
    register,
    handleSubmit,
    resetField,
    watch,
    setValue,
    formState: { errors, dirtyFields },
  } = useForm({
    resolver: yupResolver(props.currentExperience ? experienceValidationSchemaPUT : experienceValidationSchemaPOST),
    mode: 'onTouched',
    reValidateMode: 'onChange',
    defaultValues: {
      id: props.currentExperience?.id ? props.currentExperience.id : '',
      name: props.currentExperience?.name ? props.currentExperience.name : '',
      description: props.currentExperience?.description ? props.currentExperience.description : '',
      path: props.currentExperience?.path ? props.currentExperience.path : '',
      profiles:
        props.currentExperience?.profiles?.profiles?.length > 0
          ? deserializeProfiles(props.currentExperience.profiles.profiles)
          : { profiles: null },
      status: props.currentExperience?.status ? props.currentExperience.status.toString() : INACTIVE.value.toString(),
      upload: props.currentExperience?.upload ? props.currentExperience.upload : '',
      xml: props.currentExperience?.xml ? props.currentExperience.xml : '',
      thumbnail: props.currentExperience?.thumbnail ? props.currentExperience.thumbnail : '',
    },
  })
  // Stato per visualizzare/nascondere l'xml
  const [showXml, setShowXml] = useState(false)
  // Stato per visualizzare/nascondere la thumbnail
  const [showThumbnail, setShowThumbnail] = useState(false)
  // Stato che imposta l'azione del form (vedere useEffect)
  const [action, setAction] = useState()
  // Stato per tenere traccia di quando l'utente toglie il focus dal form
  const [isFormFocused, setIsFormFocused] = useState(false)
  // Stato per mostrare e nascondere il modale in caso di eliminazione concorrente dell'esperienza
  const [showModal, setShowModal] = useState(false)

  // Imposta l'azione del form (create o update)
  useEffect(() => {
    if (props.currentExperience) {
      setAction(UPDATE)
    } else {
      setAction(CREATE)
    }
  }, [props.currentExperience])

  // Confronto fra l'esperienza corrente e l'esperienza pollata
  useEffect(() => {
    const checkIncomingPollingExperience = async () => {
      if (props.pollingExperience) {
        // Se l'esperienza in polling è stata eliminata viene mostrato un modale
        if (props.pollingExperience.warning) {
          await handleIsFormFocusedFalse()
          console.warn(props.pollingExperience.warning)
          setShowModal(true)
          return
        }
        if (watch().name !== props.pollingExperience.experience.data.name && !dirtyFields.name) {
          setValue('name', props.pollingExperience.experience.data.name)
        }
        if (watch().description !== props.pollingExperience.experience.data.description && !dirtyFields.description) {
          setValue('description', props.pollingExperience.experience.data.description)
        }
        if (watch().path !== props.pollingExperience.experience.data.path && !dirtyFields.path) {
          setValue('path', props.pollingExperience.experience.data.path)
        }
        if (
          !(
            JSON.stringify(watch().profiles) ===
            JSON.stringify(deserializeProfiles(props.pollingExperience.experience.data.profiles?.profiles))
          ) &&
          !dirtyFields.profiles
        ) {
          setValue('profiles', deserializeProfiles(props.pollingExperience.experience.data.profiles?.profiles))
        }
        if (watch().status !== props.pollingExperience.experience.data.status.toString() && !dirtyFields.status) {
          setValue('status', props.pollingExperience.experience.data.status.toString())
        }
        if (watch().upload !== props.pollingExperience.experience.data.upload && !dirtyFields.upload) {
          setValue('upload', props.pollingExperience.experience.data.upload)
        }
        if (watch().xml !== props.pollingExperience.experience.data.xml && !dirtyFields.xml) {
          setValue('xml', props.pollingExperience.experience.data.xml)
        }
        if (watch().thumbnail !== props.pollingExperience.experience.data.thumbnail && !dirtyFields.thumbnail) {
          setValue('thumbnail', props.pollingExperience.experience.data.thumbnail)
        }
      }
    }

    checkIncomingPollingExperience()
  }, [props.pollingExperience, dirtyFields, setValue, watch])

  // Si occupa dell'inserimento o della modifica di una esperienza
  const submitForm = async values => {
    await handleIsFormFocusedFalse()

    // Caso CREATE
    if (action === CREATE) {
      // Creazione e popolamento del formData
      const formData = new FormData()
      appendFileListToFormData(formData, values.xml, 'xmlFile')
      appendFileListToFormData(formData, values.thumbnail, 'thumbnailFile')
      appendSingleValueToFormData(formData, values.name, 'name')
      appendSingleValueToFormData(formData, values.description, 'description')

      const experience = await addNewExperience(formData)

      if (!experience.isError) {
        console.log(`L'esperienza ${experience.data.data.name} è stato inserita correttamente`)
      } else {
        console.error("Errore nell'inserimento di una nuova esperienza:", experience.data.message)
      }
    }

    // Caso UPDATE
    if (action === UPDATE) {
      const experience = await editExperience({
        ...values,
        profiles: { profiles: serializeProfiles(values.profiles) },
      })

      if (!experience.isError) {
        console.log(`L'esperienza ${experience.data.data.name} è stata modificata correttamente`)
      } else {
        console.error("Errore nella modifica dell'esperienza:", experience.data.message)
      }
    }

    history.push(ROUTE_EXPERIENCES.path)
  }

  // Si occupa dell'eliminazione di un dispositivo
  const handleDelete = async () => {
    await handleIsFormFocusedFalse()

    const experienceName = props.currentExperience.name
    const experience = await deleteExperience(props.currentExperience.id)

    if (!experience.isError) {
      console.log(`L'esperienza ${experienceName} è stata eliminata correttamente`)
    } else {
      console.error("Errore nell'eliminazione dell'esperienza:", experience.data.message)
    }

    history.push(ROUTE_EXPERIENCES.path)
  }

  // Controlla il pulsante 'Annulla'
  const handleCancel = async () => {
    await handleIsFormFocusedFalse()
    history.push(ROUTE_EXPERIENCES.path)
  }

  // Setta a true lo stato quando l'utente fa focus sul form
  const handleIsFormFocusedTrue = () => {
    setIsFormFocused(true)
  }

  // Setta a false lo stato quando l'utente toglie il focus dal form
  const handleIsFormFocusedFalse = () => {
    setIsFormFocused(false)
  }

  // Resetta un campo di input
  const handleFieldReset = field => {
    resetField(field)
    document.getElementById(field).value = ''
  }

  // Toggler di stato per la visualizzazione del popup dell'xml
  const handleToggleXml = () => {
    setShowXml(prev => !prev)
  }

  // Toggler di stato per la visualizzazione del popup della thumbnail
  const handleToggleThumbnail = () => {
    setShowThumbnail(prev => !prev)
  }

  // Chiude il modale che informa che l'esperienza è stata eliminata
  const handleCloseModal = () => {
    setShowModal(false)
    history.push(ROUTE_EXPERIENCES.path)
  }

  const formToRender = (
    <div className="iedx-upload-experiences">
      <Prompt when={isFormFocused} message={() => 'Sei sicuro di voler abbandonare? I dati non salvati verranno persi.'} />

      {showModal && <ModalElement modalBody={props.pollingExperience.warning} handleCloseModal={handleCloseModal} />}

      {/*<form className='iedx-form-wrapper' onSubmit={handleSubmit(submitForm)}*/}
      <form className='iedx-form-wrapper' onFocus={handleIsFormFocusedTrue}>
        <div className='iedx-up-action-button'>
          <div className='iedx-action-button'>
            <div className='iedx-radio-container'>
              <div className='iedx-radio-toggle-container'>
                <div className='iedx-submit'>
                  {action === UPDATE && (
                    // Renderizzazione condizionale toggler attiva / disattiva (solo per la modifica)
                    <ToggleSwitch
                      register={register}
                      watch={watch}
                      errors={errors}
                      name='status'
                      values={[ACTIVE.value, INACTIVE.value]}
                      labels={[ACTIVE.maleLabel, INACTIVE.maleLabel]}
                    />
                  )}
                </div>
              </div>
            </div>
          </div>

          <div className='iedx-action-button'>
            {/* Renderizzazione condizionale pulsante elimina (solo per la modifica) */}
            {action === UPDATE && (
              <Button size='sm' variant='outline-secondary' type='button' onClick={handleDelete}>
                <img className='iedx-card-img' src={Logotrash} alt='trash bin' />
              </Button>
            )}
          </div>
        </div>

        <Col className='iedx-form'>
          <div className='mb-3'>
            {/* Input di tipo testo: nome */}
            <TextField
              errors={errors}
              register={register}
              name='name'
              label='Nome *'
              placeholder="Inserisci il nome dell'esperienza"
              disabled={false}
            />
          </div>

          <div className='mb-3'>
            {/* Input di tipo textarea: descrizione */}
            <TextareaField
              errors={errors}
              register={register}
              name='description'
              label='Descrizione'
              placeholder="Inserisci un'eventuale descrizione testuale"
            />
          </div>

          <div className='iedx-section-wrapper'>
            {action === UPDATE && (
              // Input gruppo di checkbox: profili (solo per la modifica)
              <>
                <div className='iedx-hr'></div>
                <p>Utenza target</p>
                <CheckboxGroup
                  errors={errors}
                  register={register}
                  name='profiles'
                  labels={[CHILD.label, TEEN.label, YOUNG_ADULT.label, ADULT.label, ELDER.label]}
                  values={[CHILD.value, TEEN.value, YOUNG_ADULT.value, ADULT.value, ELDER.value]}
                />
              </>
            )}
          </div>

          <div className='iedx-hr'></div>

          {action === CREATE && (
            <div className='iedx-section-wrapper'>
              <p>Experience Script e Immagine di Anteprima (thumbnail) </p>

              <div className='iedx-iedx-file-upload-create'>
                <div className='mb-3'>
                  <br />
                  {/* Input di tipo file: xml (solo per la creazione) */}
                  <FileUploadField
                    watch={watch}
                    errors={errors}
                    register={register}
                    handleFieldReset={handleFieldReset}
                    label='File XML *'
                    name='xml'
                    buttonLabel='Svuota'
                    miltipleUploads={false}
                  />
                </div>

                <div className='mb-3'>
                  {/* Input di tipo file: thumbnail (solo per la creazione) */}
                  <br />
                  <FileUploadField
                    watch={watch}
                    errors={errors}
                    register={register}
                    handleFieldReset={handleFieldReset}
                    label='Thumbnail'
                    name='thumbnail'
                    buttonLabel='Svuota'
                    miltipleUploads={false}
                  />
                </div>
              </div>
            </div>
          )}

          {action === UPDATE && (
            <div className='iedx-section-wrapper'>
              <p>Experience Script e Immagine di Anteprima (thumbnail) </p>

              <div className='iedx-iedx-file-upload-update'>
                <div className='mb-3'>
                  {/* Input di tipo testo disabled: xml (solo per la modifica) */}
                  <TextField errors={errors} register={register} name='xml' label='File XML *' placeholder='File XML' disabled={true} />
                </div>
                {/* Pulsante e renderizzazione condizionale modale xml */}
                {props.currentExperienceXml && (
                  <Button variant='link' onClick={handleToggleXml}>
                    File XML
                  </Button>
                )}
                {showXml && (
                  <ModalElement
                    modalBody={
                      props.pollingExperience?.experienceXml?.data ? props.pollingExperience.experienceXml.data : props.currentExperienceXml
                    }
                    handleCloseModal={handleToggleXml}
                  />
                )}

                <div className='mb-3'>
                  {/* Input di tipo testo disabled: thumbnail (solo per la modifica) */}
                  <TextField
                    errors={errors}
                    register={register}
                    name='thumbnail'
                    label='Thumbnail'
                    placeholder='Thumbnail'
                    disabled={true}
                  />
                </div>
                {/* Pulsante e renderizzazione condizionale modale thumbnail */}
                {props.currentExperienceThumbnail && (
                  <Button variant='link' onClick={handleToggleThumbnail}>
                    Thumbnail
                  </Button>
                )}
                {showThumbnail && (
                  <ModalElement
                    modalBody={
                      props.pollingExperience?.experienceThumbnail?.data
                        ? props.pollingExperience.experienceThumbnail.data
                        : props.currentExperienceThumbnail
                    }
                    handleCloseModal={handleToggleThumbnail}
                  />
                )}
              </div>
            </div>
          )}
        </Col>
      </form>

      {action === UPDATE && (
        <div className='iedx-iedx-file-upload-nuovo iedx-form-update col'>
          <div className='iedx-hr'></div>
          <div className='iedx-section-wrapper'>
            <p>Gestisci asset </p>
            <UploadExperienceAsset currentExperience={props.currentExperience} setReloadToken={props.setReloadToken} />
            <br />
            <ExperienceAssets currentExperience={props.currentExperience} reloadToken={props.reloadToken} />
          </div>
        </div>
      )}

      <div className='mt-4 iedx-action-button iedx-low-action-button'>
        {/* Pulsanti */}
        <Button variant='outline-primary' type='button' onClick={handleCancel}>
          Annulla
        </Button>
        <Button variant='primary' type='submit' onClick={handleSubmit(submitForm)}>
          Salva
        </Button>
      </div>
    </div>
  )

  return <>{formToRender}</>
}

export default ExperienceForm
