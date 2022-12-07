import { Provider } from 'react-redux'
import { HashRouter } from 'react-router-dom'

import store from '../../store'

export const Wrapper = ({ children }) => {
  return (
    <Provider store={store}>
      <HashRouter>{children}</HashRouter>
    </Provider>
  )
}
