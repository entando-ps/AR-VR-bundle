import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Spinner from 'react-bootstrap/Spinner'
import { useDispatch } from 'react-redux'

import { routeActions } from '../../store/route-slice'
import { ROUTE_EXPERIENCE_DETAIL } from '../../common/routing/routes'
import { getSingleExperience, getSingleExperienceThumbnail, getSingleExperienceXml } from '../../integration/Integration'
import ExperienceForm from '../../components/form/ExperienceForm'
import { EXPERIENCE_DETAIL_TITLE_LABEL, POLLING_INTERVAL_EXPERIENCE } from '../../common/system-wide-variables'

const ExperienceDetail = () => {
  const params = useParams()
  const dispatch = useDispatch()
  const [currentExperience, setCurrentExperience] = useState()
  const [currentExperienceXml, setCurrentExperienceXml] = useState()
  const [currentExperienceThumbnail, setCurrentExperienceThumbnail] = useState()
  const [pollingExperience, setPollingExperience] = useState()
  // Token utilizzato per triggerare il rerender del componente e dei suoi children
  const [reloadToken, setReloadToken] = useState(new Date().getTime().toString())
  // console.log('Current EXPERIENCE', currentExperience)

  useEffect(() => {
    const experienceDetailRoute = {
      ...ROUTE_EXPERIENCE_DETAIL,
      breadcrumb: EXPERIENCE_DETAIL_TITLE_LABEL + currentExperience?.name
    }
    dispatch(routeActions.setCurrentRoute(experienceDetailRoute))
  }, [dispatch, currentExperience?.name])

  // Recupera il dettaglio dell'esperienza da inviare al form
  useEffect(() => {
    const getExperience = async () => {
      const experience = await getSingleExperience(params.parExperienceId)

      // console.log('EXPERIENCE (raw from getExperience)', experience)
      if (!experience.isError) {
        setCurrentExperience(experience.data)
      } else {
        console.error("Errore nel recupero dell'esperienza:", experience.data.message)
      }
    }

    getExperience()
  }, [params.parExperienceId, reloadToken])

  // Polling del dettaglio dell'esperienza, dell'XML e della thumbnail per aggiornare in caso di modifiche concorrenti
  useEffect(() => {
    let intervalId
    let isError = false

    if (currentExperience) {
      const getExperience = async () => {
        // Caricamento dell'esperienza
        const experience = await getSingleExperience(params.parExperienceId)

        if (!experience.isError) {
          // Caricamento dell'experience script XML
          const experienceXml = await getSingleExperienceXml(params.parExperienceId)

          if (experience.data.thumbnail) {
            // Caricamento della thumbnail
            const experienceThumbnail = await getSingleExperienceThumbnail(params.parExperienceId)
            setPollingExperience({
              experience,
              experienceXml,
              experienceThumbnail,
            })
            return
          }
          setPollingExperience({
            experience,
            experienceXml,
          })
        } else {
          isError = true
          console.error("Errore nel recupero dell'esperienza:", experience.data.message)
          setPollingExperience({ warning: `L'esperienza ${currentExperience.name} è stata eliminata` })
        }
      }

      intervalId = setInterval(() => {
        if (isError) {
          clearInterval(intervalId)
        }
        process.env.NODE_ENV === 'development' && console.log('Polling experience...')
        getExperience()
      }, POLLING_INTERVAL_EXPERIENCE)
    }

    if (intervalId) {
      return () => clearInterval(intervalId)
    }
  }, [currentExperience, params.parExperienceId])

  // Recupera l'XML da inviare al form
  useEffect(() => {
    if (currentExperience) {
      const getExperienceXml = async () => {
        const experienceXml = await getSingleExperienceXml(params.parExperienceId)

        // console.log('XML', experienceXml)
        if (!experienceXml.isError) {
          setCurrentExperienceXml(experienceXml.data)
        } else {
          console.error("Errore nel recupero dell'XML dell'esperienza:", experienceXml.data.message)
        }
      }

      getExperienceXml()
    }
  }, [params.parExperienceId, reloadToken, currentExperience])

  // Recupera la thumbnail da inviare al form
  useEffect(() => {
    if (currentExperience?.thumbnail) {
      const getExperienceThumbnail = async () => {
        const experienceThumbnail = await getSingleExperienceThumbnail(params.parExperienceId)

        // console.log('THUMBNAIL', experienceThumbnail)
        if (!experienceThumbnail.isError) {
          setCurrentExperienceThumbnail(experienceThumbnail.data)
        } else {
          console.error("Errore nel recupero della thumbnail dell'esperienza:", experienceThumbnail.data.message)
        }
      }

      getExperienceThumbnail()
    }
  }, [params.parExperienceId, reloadToken, currentExperience])

  const outputToRender = currentExperience ? (
    <div className="mt-3">
      <h3 className='iedx-home-title'>{EXPERIENCE_DETAIL_TITLE_LABEL}{currentExperience?.name}</h3>
      <ExperienceForm
        reloadToken={reloadToken}
        setReloadToken={setReloadToken}
        currentExperience={currentExperience}
        currentExperienceXml={currentExperienceXml}
        currentExperienceThumbnail={currentExperienceThumbnail}
        pollingExperience={pollingExperience}
      />
    </div>
  ) : (
    <Spinner animation='grow' />
  )

  return (
    <Container>
      <Row>
        <Col>{currentExperience ? outputToRender : <p>L'esperienza con codice {params.parExperienceId} non esiste</p>}</Col>
      </Row>
    </Container>
  )
}

export default ExperienceDetail
