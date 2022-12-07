import React, { useEffect, useState } from 'react'
import { useSelector } from 'react-redux'
import Breadcrumb from 'react-bootstrap/Breadcrumb'
import { LinkContainer } from 'react-router-bootstrap'

const BreadcrumbManager = () => {
  const currentRoute = useSelector(state => state.route.currentRoute)
  const [routes, setRoutes] = useState()
  const [breadcrumbsToRender, setBreadcrumbsToRender] = useState([])

  // Crea l'array delle routes che verranno poi utilizzate per renderizzare le breadcrumb
  useEffect(() => {
    if (currentRoute) {
      const routeFamily = []

      // Popola routeFamily con tutti i parenti della route corrente
      const createRouteFamily = route => {
        const cycleThroughParents = route => {
          if (route.childOf.length === 0) {
            return
          }

          route.childOf.map(parent => {
            cycleThroughParents(parent)
            return routeFamily.push(parent)
          })
        }

        cycleThroughParents(route)
        // Aggiunge la route corrente in coda a routeFamily
        routeFamily.push(currentRoute)
        // Mette routeFamily nello stato
        setRoutes(routeFamily)
      }

      createRouteFamily(currentRoute)
    }
  }, [currentRoute])

  // Crea l'array di breadcrumbs renderizzate a partire dalle route create col primo useEffect
  useEffect(() => {
    if (routes?.length > 0) {
      setBreadcrumbsToRender(
        routes.map((route, index) => {
          if (index === routes.length - 1) {
            return (
              <Breadcrumb.Item key={index} active>
                {route.breadcrumb}
              </Breadcrumb.Item>
            )
          }

          return (
            <LinkContainer key={index} to={route.path}>
              <Breadcrumb.Item>{route.breadcrumb}</Breadcrumb.Item>
            </LinkContainer>
          )
        })
      )
    }
  }, [routes])

  return <Breadcrumb>{breadcrumbsToRender}</Breadcrumb>
}

export default BreadcrumbManager
