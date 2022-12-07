import React from 'react'
import { useLocation } from 'react-router-dom'
import { ROUTE_HOME, ROUTE_ROOT } from '../../common/routing/routes'

import BreadcrumbManager from './BreadcrumbManager'

const LayoutWrapper = props => {
  const location = useLocation()

  return (
    <div className='iedx-LayoutWrapper'>
      <main>
        {location.pathname !== ROUTE_ROOT.path && location.pathname !== ROUTE_HOME.path && <BreadcrumbManager />}
        {props.children}
      </main>
    </div>
  )
}

export default LayoutWrapper