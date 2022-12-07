import React from 'react'
import { Route, Switch } from 'react-router-dom'

import { routes, routesElements } from './routes'

const AppRouter = () => {
  const routesToProvide = routes.map(route => {
    return (
      <Route path={route.path} exact={route.exact} key={route.path}>
        {routesElements
          .filter(element => {
            return element.id === route.id
          })
          .map(element => {
            return element.element
          })}
      </Route>
    )
  })

  return <Switch>{routesToProvide}</Switch>
}

export default AppRouter
