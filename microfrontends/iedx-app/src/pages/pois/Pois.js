import React, { useEffect, useState } from 'react'
import Container from 'react-bootstrap/Container'
import Table from 'react-bootstrap/Table'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Button from 'react-bootstrap/Button'
import { LinkContainer } from 'react-router-bootstrap'
import { useDispatch } from 'react-redux'

import PoisTableItem from '../../components/layout/PoisTableItem'
import { getAllPois } from '../../integration/Integration'
import { routeActions } from '../../store/route-slice'
import { ROUTE_POIS } from '../../common/routing/routes'
import {
  POIS_NO_ITEMS,
  POIS_TITLE_LABEL,
  POIS_SUBTITLE_LABEL,
  POIS_TABLE_TITLE_LABEL,
  POIS_BUTTON_LABEL,
  POLLING_INTERVAL_POI_LIST,
} from '../../common/system-wide-variables'

/** TODO-OPT Query parameters per il sorting
 *
 */

const Pois = () => {
  const dispatch = useDispatch()
  const [pois, setPois] = useState()
  const [poisToRender, setPoisToRender] = useState()
  const [reloadToken, setReloadToken] = useState(new Date().getTime().toString())

  useEffect(() => {
    dispatch(routeActions.setCurrentRoute(ROUTE_POIS))
  })

  useEffect(() => {
    let isComponentMounted = true

    const getPois = async () => {
      const pois = await getAllPois()

      if (isComponentMounted) {
        if (!pois.isError) {
          setPois(pois.data)
        } else {
          console.error('Errore nel recupero dei dispositivi:', pois.data.message)
        }
      }
    }

    getPois()

    const intervalId = setInterval(() => {
      process.env.NODE_ENV === 'development' && console.log('Polling POI list...')
      getPois()
    }, [POLLING_INTERVAL_POI_LIST])

    return () => {
      clearInterval(intervalId)
      isComponentMounted = false
    }
  }, [reloadToken])

  useEffect(() => {
    if (pois) {
      setPoisToRender(
        pois.map(poi => {
          return <PoisTableItem poi={poi} key={poi.id} setReloadToken={setReloadToken} />
        })
      )
    }
  }, [pois])

  return (
    <Container>
      <Row>
        <Col>
          <h3 className='iedx-home-title'>{POIS_TITLE_LABEL}</h3>
          <p className='iedx-home-subtitle'>{POIS_SUBTITLE_LABEL}</p>
        </Col>
      </Row>
      <div className='iedx-table-container iedx-poi'>
        <div className='iedx-table-header'>
          <p className='iedx-table-name'>{POIS_TABLE_TITLE_LABEL}</p>
          <LinkContainer to={`${ROUTE_POIS.path}/NewPoi`}>
            <Button className='iedx-add-button'>{POIS_BUTTON_LABEL}</Button>
          </LinkContainer>
        </div>
        {poisToRender?.length > 0 && (
          <Table striped hover responsive>
            <thead>
              <tr>
                <th>Nome</th>
                <th>Info</th>
                <th>Latitudine</th>
                <th>Longitudine</th>
                <th>Azione</th>
              </tr>
            </thead>
            <tbody>{poisToRender}</tbody>
          </Table>
        )}
        {poisToRender?.length === 0 && <p>{POIS_NO_ITEMS}</p>}
      </div>
    </Container>
  )
}

export default Pois
