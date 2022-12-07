import React, { useState, useEffect } from 'react'
import { useHistory, Prompt } from 'react-router-dom'
import { Button, Col, Modal } from 'react-bootstrap'
import { useForm } from 'react-hook-form'
import { yupResolver } from '@hookform/resolvers/yup'

import Logotrash from '../../images/trashbin.png'
import { deviceValidationSchema } from './validation/deviceValidationSchema'
import { addNewDevice, deleteDevice, editDevice } from '../../integration/Integration'
import { ROUTE_DEVICES } from '../../common/routing/routes'
import LogoMng from '../../images/gestione-progetti.png'
import ToggleSwitch from './custom-fields/ToggleSwitch'
import TextField from './custom-fields/TextField'
import TextareaField from './custom-fields/TextareaField'
import { CREATE, UPDATE, INACTIVE, ACTIVE } from '../../common/system-wide-variables'
import ModalElement from '../layout/ModalElement'

const DeviceForm = props => {
  const {
    register,
    handleSubmit,
    watch,
    setValue,
    formState: { errors, dirtyFields },
  } = useForm({
    resolver: yupResolver(deviceValidationSchema),
    mode: 'onTouched',
    reValidateMode: 'onChange',
    defaultValues: {
      deviceId: props.currentDevice ? props.currentDevice.deviceId : '',
      name: props.currentDevice ? props.currentDevice.name : '',
      note: props.currentDevice ? props.currentDevice.note : '',
      status: props.currentDevice ? props.currentDevice.status.toString() : INACTIVE.value.toString(),
    },
  })
  const history = useHistory()
  const [action, setAction] = useState()
  const [isFormFocused, setIsFormFocused] = useState(false)
  const [showModal, setShowModal] = useState(false)

  // Imposta l'azione del form (create o update)
  useEffect(() => {
    if (props.currentDevice) {
      setAction(UPDATE)
    } else {
      setAction(CREATE)
    }
  }, [props.currentDevice])

  // Confronto fra il device corrente e il device pollato
  useEffect(() => {
    const checkIncomingPollingDevice = async () => {
      if (props.pollingDevice) {
        // Se il dispositivo in polling è stato eliminato viene mostrato un modale
        if (props.pollingDevice.warning) {
          await handleIsFormFocusedFalse()
          console.warn(props.pollingDevice.warning)
          setShowModal(true)
          return
        }
        if (watch().deviceId !== props.pollingDevice.deviceId && !dirtyFields.deviceId) {
          setValue('deviceId', props.pollingDevice.deviceId)
        }
        if (watch().name !== props.pollingDevice.name && !dirtyFields.name) {
          setValue('name', props.pollingDevice.name)
        }
        if (watch().note !== props.pollingDevice.note && !dirtyFields.note) {
          setValue('note', props.pollingDevice.note)
        }
        if (watch().status !== props.pollingDevice.status.toString() && !dirtyFields.status) {
          setValue('status', props.pollingDevice.status.toString())
        }
      }
    }

    checkIncomingPollingDevice()
  }, [props.pollingDevice, dirtyFields, setValue, watch])

  // Si occupa dell'inserimento o della modifica di un dispositivo
  const submitForm = async values => {
    await handleIsFormFocusedFalse()

    if (action === CREATE) {
      const device = await addNewDevice(values)

      if (!device.isError) {
        console.log(`Il dispositivo ${device.data.data.name} è stato inserito correttamente`)
      } else {
        console.error("Errore nell'inserimento di un nuovo dispositivo:", device.data.message)
      }
    }

    if (action === UPDATE) {
      const device = await editDevice({
        ...values,
        id: props.currentDevice.id,
      })

      if (!device.isError) {
        console.log(`Il dispositivo ${device.data.data.name} è stato modificato correttamente`)
      } else {
        console.error('Errore nella modifica del dispositivo:', device.data.message)
      }
    }

    history.push(ROUTE_DEVICES.path)
  }

  // Si occupa dell'eliminazione di un dispositivo
  const handleDelete = async () => {
    await handleIsFormFocusedFalse()

    const deviceName = props.currentDevice.name
    const device = await deleteDevice(props.currentDevice.id)

    if (!device.isError) {
      console.log(`Il dispositivo ${deviceName} è stato eliminato correttamente`)
    } else {
      console.error("Errore nell'eliminazione del dispositivo:", device.data.message)
    }

    history.push(ROUTE_DEVICES.path)
  }

  const handleCancel = async () => {
    await handleIsFormFocusedFalse()
    history.push(ROUTE_DEVICES.path)
  }

  const handleIsFormFocusedTrue = () => {
    setIsFormFocused(true)
  }

  const handleIsFormFocusedFalse = () => {
    setIsFormFocused(false)
  }

  const handleCloseModal = () => {
    setShowModal(false)
    history.push(ROUTE_DEVICES.path)
  }

  // Renderizzazione condizionale del pulsante 'elimina' (solo per l'update)
  const deleteButton = action === UPDATE && (
    <Button size='sm' variant='outline-secondary' type='button' onClick={handleDelete}>
      <img className='iedx-card-img' src={Logotrash} alt='trash bin' />
    </Button>
  )

  const formToRender = (
    <>
      <Prompt when={isFormFocused} message={() => 'Sei sicuro di voler abbandonare? I dati non salvati verranno persi.'} />

      {showModal && <ModalElement modalBody={props.pollingDevice.warning} handleCloseModal={handleCloseModal} />}

      <form className='iedx-form-wrapper' onSubmit={handleSubmit(submitForm)} onFocus={handleIsFormFocusedTrue}>
        <div className='iedx-up-action-button'>
          <div className='iedx-action-button'>
            <div className='iedx-radio-container'>
              <div className='iedx-radio-toggle-container'>
                <div className='iedx-submit'>
                  {/* Input di tipo radio button (toggle switch) */}
                  <ToggleSwitch
                    register={register}
                    watch={watch}
                    errors={errors}
                    name='status'
                    values={[ACTIVE.value, INACTIVE.value]}
                    labels={[ACTIVE.femaleLabel, INACTIVE.femaleLabel]}
                  />
                </div>
              </div>
            </div>
          </div>

          <div className='iedx-action-button'>{deleteButton}</div>
        </div>

        <Col className='iedx-form'>
          <div className='mb-3'>
            {/* Input di tipo testo */}
            <TextField
              errors={errors}
              register={register}
              name='deviceId'
              label='ID Univoco *'
              placeholder="Inserisci l'identificativo del dispositivo VR/MR"
              disabled={props.currentDevice}
            />
          </div>
          <div className='mb-3'>
            {/* Input di tipo testo */}
            <TextField
              errors={errors}
              register={register}
              name='name'
              label='Nome *'
              placeholder='Inserisci un nome per riconoscere il dispositivo VR/MR'
            />
          </div>
          <div className='mb-3'>
            {/* Input di tipo textarea */}
            <TextareaField
              errors={errors}
              register={register}
              name='note'
              label='Descrizione'
              placeholder="Inserisci un'eventuale descrizione testuale"
            />
          </div>
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

export default DeviceForm
