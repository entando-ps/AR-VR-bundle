import React, { useEffect } from 'react'
import { useDispatch } from 'react-redux'

import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import { routeActions } from '../../store/route-slice'
import { ROUTE_NEW_EXPERIENCE } from '../../common/routing/routes'
import ExperienceForm from '../../components/form/ExperienceForm'
import { NEW_EXPERIENCE_TITLE_LABEL } from '../../common/system-wide-variables'

const NewExperience = () => {
  const dispatch = useDispatch()

  useEffect(() => {
    dispatch(routeActions.setCurrentRoute(ROUTE_NEW_EXPERIENCE))
  })

  return (
    <Container>
      <Row>
        <Col>
          <h3 className='iedx-home-title'>{NEW_EXPERIENCE_TITLE_LABEL}</h3>
          <br/>
        </Col>
      </Row>
      <ExperienceForm />
    </Container>
  )
}

export default NewExperience
