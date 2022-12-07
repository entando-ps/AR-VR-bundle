import ReactDOM from 'react-dom'
import React from 'react'
import App from '../App'
import { HashRouter } from 'react-router-dom'
import { Provider } from 'react-redux'

import store from '../store/index'

class IEDXapp extends HTMLElement {
  connectedCallback() {
    this.mountPoint = document.createElement('span')
    this.render()
  }

  render() {
    ReactDOM.render(
      <Provider store={store}>
        <HashRouter>
          <React.StrictMode>
            <App />
          </React.StrictMode>
        </HashRouter>
      </Provider>,
      this.appendChild(this.mountPoint)
    )
  }
}

customElements.get('iedx-app') || customElements.define('iedx-app', IEDXapp)
