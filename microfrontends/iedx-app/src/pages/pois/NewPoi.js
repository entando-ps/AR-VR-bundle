import React, { useEffect } from 'react'
import { useDispatch } from 'react-redux'
import { Col, Row, Container } from 'react-bootstrap'

import PoiForm from '../../components/form/PoiForm'
import { ROUTE_NEW_POI } from '../../common/routing/routes'
import { NEW_POI_TITLE_LABEL } from '../../common/system-wide-variables'
import { routeActions } from '../../store/route-slice'

const NewPoi = () => {
  const dispatch = useDispatch()

  useEffect(() => {
    dispatch(routeActions.setCurrentRoute(ROUTE_NEW_POI))
  })

  return (
    <Container>
      <Row>
        <Col>
          <h3 className='iedx-home-title'>{NEW_POI_TITLE_LABEL}</h3>
          <br/>
        </Col>
      </Row>
      <PoiForm />
    </Container>
  )
}

export default NewPoi
