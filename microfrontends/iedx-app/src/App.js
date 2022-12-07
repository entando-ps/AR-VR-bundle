import React from 'react'

import LayoutWrapper from './components/layout/LayoutWrapper'
import AppRouter from './common/routing/AppRouter'

function App() {
  console.info(`Running application in ${process.env.NODE_ENV} mode`)

  return (
    <LayoutWrapper>
      <AppRouter />
    </LayoutWrapper>
  )
}

export default App
