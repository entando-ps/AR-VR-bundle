import React, { useEffect, useState } from 'react'
import Container from 'react-bootstrap/Container'
import Table from 'react-bootstrap/Table'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Button from 'react-bootstrap/Button'
import { LinkContainer } from 'react-router-bootstrap'
import { useDispatch } from 'react-redux'

import { routeActions } from '../../store/route-slice'
import { ROUTE_DEVICES } from '../../common/routing/routes'
import DeviceTableItem from '../../components/layout/DeviceTableItem'
import { getAllDevices } from '../../integration/Integration'
import {
  DEVICES_BUTTON_LABEL,
  DEVICES_SUBTITLE_LABEL,
  DEVICES_TABLE_TITLE_LABEL,
  DEVICES_TITLE_LABEL,
  POLLING_INTERVAL_DEVICE_LIST,
  DEVICES_NO_ITEMS,
} from '../../common/system-wide-variables'

/** TODO-OPT Query parameters per il sorting
 *
 */

const Devices = () => {
  const dispatch = useDispatch()
  const [devices, setDevices] = useState()
  const [devicesToRender, setDevicesToRender] = useState()

  useEffect(() => {
    dispatch(routeActions.setCurrentRoute(ROUTE_DEVICES))
  })

  useEffect(() => {
    let isComponentMounted = true

    const getDevices = async () => {
      const devices = await getAllDevices()

      if (isComponentMounted) {
        if (!devices.isError) {
          setDevices(devices.data)
        } else {
          console.error('Errore nel recupero dei dispositivi:', devices.data.message)
        }
      }
    }

    getDevices()

    const intervalId = setInterval(() => {
      // process.env.NODE_ENV === 'development' && console.log('Polling device list...')
      getDevices()
    }, POLLING_INTERVAL_DEVICE_LIST)

    return () => {
      clearInterval(intervalId)
      isComponentMounted = false
    }
  }, [])

  useEffect(() => {
    if (devices) {
      setDevicesToRender(
        devices.map(device => {
          return <DeviceTableItem device={device} key={device.id} />
        })
      )
    }
  }, [devices])

  return (
    <Container>
      <Row>
        <Col>
          <h3 className='iedx-home-title'>{DEVICES_TITLE_LABEL}</h3>
          <p className='iedx-home-subtitle'>{DEVICES_SUBTITLE_LABEL}</p>
        </Col>
      </Row>
      <div className='iedx-table-container'>
        <div className='iedx-table-header'>
          <p className='iedx-table-name'>{DEVICES_TABLE_TITLE_LABEL}</p>
          <LinkContainer to={`${ROUTE_DEVICES.path}/newDevice`}>
            <Button className='iedx-add-button'>{DEVICES_BUTTON_LABEL}</Button>
          </LinkContainer>
        </div>
        {devicesToRender?.length > 0 && (
          <Table striped hover responsive>
            <thead>
              <tr>
                <th>Nome</th>
                <th>Descrizione</th>
                <th>Stato</th>
              </tr>
            </thead>
            <tbody>{devicesToRender}</tbody>
          </Table>
        )}
        {devicesToRender?.length === 0 && <p>{DEVICES_NO_ITEMS}</p>}
      </div>
    </Container>
  )
}

export default Devices
