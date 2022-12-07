import React, { useEffect } from 'react'
import { Link } from 'react-router-dom'
import Card from 'react-bootstrap/Card'
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import LogoMng from '../images/gestione-progetti.png'
import LogoVr from '../images/vr-glasses.png'
import LogoAr from '../images/ar.png'
import { useDispatch } from 'react-redux'

import { routeActions } from '../store/route-slice'
import { ROUTE_HOME } from '../common/routing/routes'
import './Pages.css'
import { HOME_DEVICES_SUBTITLE_LABEL, HOME_DEVICES_TITLE_LABEL, HOME_EXPERIENCES_SUBTITLE_LABEL, HOME_EXPERIENCES_TITLE_LABEL, HOME_POIS_SUBTITLE_LABEL, HOME_POIS_TITLE_LABEL, HOME_SUBTITLE_LABEL, HOME_TITLE_LABEL } from '../common/system-wide-variables'

const Home = () => {
  const dispatch = useDispatch()

  useEffect(() => {
    dispatch(routeActions.setCurrentRoute(ROUTE_HOME))
  })

  return (
    <Container>
      <h3 className='iedx-home-title'>{HOME_TITLE_LABEL}</h3>
      <p className='iedx-home-subtitle'>{HOME_SUBTITLE_LABEL}</p>

      <div className='iedx-card-deck'>
        <Row>
          <Col md={6}>
            <Card>
              <img className='iedx-card-img' src={LogoVr} alt='Card image cap' />
              <div className='card-body'>
                <Link className='iedx-card-link' to='/devices'>
                  {HOME_DEVICES_TITLE_LABEL}
                </Link>
                <p className='card-text'>
                  {HOME_DEVICES_SUBTITLE_LABEL}
                </p>
              </div>
            </Card>
          </Col>
          <Col md={6}>
            <Card>
              <img className='iedx-card-img' src={LogoMng} alt='Card image cap' />
              <div className='card-body'>
                <Link className='iedx-card-link' to='/experiences'>
                  {HOME_EXPERIENCES_TITLE_LABEL}
                </Link>
                <p className='card-text'>
                  {HOME_EXPERIENCES_SUBTITLE_LABEL}
                </p>
              </div>
            </Card>
          </Col>
          <Col md={6}>
            <Card>
              <img className='iedx-card-img' src={LogoAr} alt='Card image cap' />
              <div className='card-body'>
                <Link className='iedx-card-link' to='/pois'>
                  {HOME_POIS_TITLE_LABEL}
                </Link>
                <p className='card-text'>
                  {HOME_POIS_SUBTITLE_LABEL}
                </p>
              </div>
            </Card>
          </Col>
        </Row>
      </div>
    </Container>
  )
}

export default Home
