import React, {useEffect} from 'react'
import {useDispatch} from 'react-redux'
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

import {ROUTE_NEW_DEVICE} from '../../common/routing/routes'
import {routeActions} from '../../store/route-slice'
import DeviceForm from '../../components/form/DeviceForm'
import { NEW_DEVICE_TITLE_LABEL } from '../../common/system-wide-variables'

const NewDevice = () => {
  const dispatch = useDispatch()

  useEffect(() => {
    dispatch(routeActions.setCurrentRoute(ROUTE_NEW_DEVICE))
  })

  return (
    <Container>
      <Row>
        <Col>
          <h3 className='iedx-home-title'>{NEW_DEVICE_TITLE_LABEL}</h3>
        </Col>
      </Row>
      <DeviceForm/>
    </Container>
  )
}

export default NewDevice
