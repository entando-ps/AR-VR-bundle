import React, { useState, useEffect } from 'react'
import Container from 'react-bootstrap/Container'
import Table from 'react-bootstrap/Table'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Button from 'react-bootstrap/Button'
import { LinkContainer } from 'react-router-bootstrap'
import { useDispatch } from 'react-redux'

import { routeActions } from '../../store/route-slice'
import { ROUTE_EXPERIENCES } from '../../common/routing/routes'
import ExperienceTableItem from '../../components/layout/ExperienceTableItem'
import { getAllExperiences, getSingleExperience } from '../../integration/Integration'
import {
  EXPERIENCES_BUTTON_LABEL,
  EXPERIENCES_NO_ITEMS,
  EXPERIENCES_SUBTITLE_LABEL,
  EXPERIENCES_TABLE_TITLE_LABEL,
  EXPERIENCES_TITLE_LABEL,
  POLLING_INTERVAL_EXPERIENCE_LIST,
} from '../../common/system-wide-variables'

/** TODO-OPT Query parameters per il sorting
 *
 */

const Experiences = () => {
  const dispatch = useDispatch()
  const [experiences, setExperiences] = useState()
  const [detailedExperiences, setDetailedExperiences] = useState()
  const [experiencesToRender, setExperiencesToRender] = useState()

  useEffect(() => {
    dispatch(routeActions.setCurrentRoute(ROUTE_EXPERIENCES))
  })

  // Recupera le esperienze
  useEffect(() => {
    let isComponentMounted = true

    const getExperiences = async () => {
      const experiences = await getAllExperiences()

      if (isComponentMounted) {
        if (!experiences.isError) {
          setExperiences(experiences.data)
        } else {
          console.error('Errore nel recupero delle esperienze:', experiences.data.message)
        }
      }
    }

    getExperiences()

    const intervalId = setInterval(() => {
      process.env.NODE_ENV === 'development' && console.log('Polling experience list...')
      getExperiences()
    }, [POLLING_INTERVAL_EXPERIENCE_LIST])

    return () => {
      clearInterval(intervalId)
      isComponentMounted = false
    }
  }, [])

  // Recupera i dettagli di tutte le singole esperienze
  useEffect(() => {
    if (experiences) {
      const getDetailedExperiences = () => {
        const detailedExperiencesPromises = experiences.map(async experience => {
          return await getSingleExperience(experience.id)
        })

        Promise.all(detailedExperiencesPromises).then(detailedExperiences => {
          return setDetailedExperiences(detailedExperiences)
        })
      }

      getDetailedExperiences()
    }
  }, [experiences])

  useEffect(() => {
    if (detailedExperiences) {
      setExperiencesToRender(
        detailedExperiences.map(detailedExperience => {
          if (!detailedExperience.isError) {
            return <ExperienceTableItem detailedExperience={detailedExperience.data} key={detailedExperience.data.id} />
          } else {
            console.error("Errore nel recupero del dettaglio dell'esperienza:", detailedExperience.data.message)
            return null
          }
        })
      )
    }
  }, [detailedExperiences])

  return (
    <Container>
      <Row>
        <Col>
          <h3 className='iedx-home-title'>{EXPERIENCES_TITLE_LABEL}</h3>
          <p className='iedx-home-subtitle'>{EXPERIENCES_SUBTITLE_LABEL}</p>
        </Col>
      </Row>
      <div className='iedx-table-container'>
        <div className='iedx-table-header'>
          <p className='iedx-table-name'>{EXPERIENCES_TABLE_TITLE_LABEL}</p>
          <LinkContainer to={`${ROUTE_EXPERIENCES.path}/newExperience`}>
            <Button className='iedx-add-button'>{EXPERIENCES_BUTTON_LABEL}</Button>
          </LinkContainer>
        </div>
        {experiencesToRender?.length > 0 && (
          <Table striped hover responsive>
            <thead>
              <tr>
                <th>Nome</th>
                <th>Data</th>
                <th>Stato</th>
                <th>Utenza target</th>
              </tr>
            </thead>
            <tbody>{experiencesToRender}</tbody>
          </Table>
        )}
        {experiencesToRender?.length === 0 && <p>{EXPERIENCES_NO_ITEMS}</p>}
      </div>
    </Container>
  )
}

export default Experiences
