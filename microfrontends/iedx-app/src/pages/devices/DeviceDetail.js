import React, {useEffect, useState} from 'react'
import {useParams} from 'react-router-dom'
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Spinner from 'react-bootstrap/Spinner'
import {useDispatch} from 'react-redux'

import {routeActions} from '../../store/route-slice'
import {ROUTE_DEVICE_DETAIL} from '../../common/routing/routes'
import DeviceForm from '../../components/form/DeviceForm'
import {getSingleDevice} from '../../integration/Integration'
import {
  DEVICE_DETAIL_TITLE_LABEL,
  POLLING_INTERVAL_DEVICE
} from '../../common/system-wide-variables'

const DeviceDetail = () => {
  const dispatch = useDispatch()
  const params = useParams()
  const [currentDevice, setCurrentDevice] = useState()
  const [pollingDevice, setPollingDevice] = useState()
  // console.log('Current DEVICE', currentDevice)

  useEffect(() => {
    const deviceDetailRoute = {
      ...ROUTE_DEVICE_DETAIL,
      breadcrumb: DEVICE_DETAIL_TITLE_LABEL + currentDevice?.name
    }
    dispatch(routeActions.setCurrentRoute(deviceDetailRoute))
  }, [dispatch, currentDevice?.name])

  // Recupera il dettaglio del device da passare al form
  useEffect(() => {
    const getDevice = async () => {
      const device = await getSingleDevice(params.parDeviceId)

      // console.log('DEVICE (raw from getDevice)', device)
      if (!device.isError) {
        setCurrentDevice(device.data)
      } else {
        console.error('Errore nel recupero del dispositivo:',
          device.data.message)
      }
    }

    getDevice()
  }, [params.parDeviceId])

  // Polling del dettaglio del device per aggiornare in caso di modifiche concorrenti
  useEffect(() => {
    let intervalId
    let isError = false

    if (currentDevice) {
      const getDevice = async () => {
        const device = await getSingleDevice(params.parDeviceId)

        if (!device.isError) {
          setPollingDevice(device.data)
        } else {
          isError = true
          console.error('Errore nel recupero del dispositivo:',
            device.data.message)
          setPollingDevice(
            {warning: `Il device ${currentDevice.name} è stato eliminato`})
        }
      }

      intervalId = setInterval(() => {
        if (isError) {
          clearInterval(intervalId)
        }
        process.env.NODE_ENV === 'development' && console.log(
          'Polling device...')
        getDevice()
      }, POLLING_INTERVAL_DEVICE)
    }

    if (intervalId) {
      return () => clearInterval(intervalId)
    }
  }, [params.parDeviceId, currentDevice])

  const outputToRender = currentDevice ? (
    <div className="mt-3">
      <h3 className='iedx-home-title'>{DEVICE_DETAIL_TITLE_LABEL}{currentDevice?.name}</h3>
      <DeviceForm currentDevice={currentDevice} pollingDevice={pollingDevice}/>
    </div>
  ) : (
    <Spinner animation='grow'/>
  )

  return (
    <Container>
      <Row>
        <Col>{currentDevice ? outputToRender : <p>Il device con
          codice {params.parDeviceId} non esiste</p>}
        </Col>
      </Row>
    </Container>
  )
}

export default DeviceDetail
