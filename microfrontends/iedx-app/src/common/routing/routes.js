import { Redirect } from 'react-router-dom'

import DeviceDetail from '../../pages/devices/DeviceDetail'
import Devices from '../../pages/devices/Devices'
import NewDevice from '../../pages/devices/NewDevice'
import ExperienceDetail from '../../pages/experiences/ExperienceDetail'
import Experiences from '../../pages/experiences/Experiences'
import NewExperience from '../../pages/experiences/NewExperience'
import Pois from '../../pages/pois/Pois'
import NewPoi from '../../pages/pois/NewPoi'
import Home from '../../pages/Home'
import NotFound from '../../pages/NotFound'

// ROOT
export const ROUTE_ROOT = {
  id: 'root',
  path: '/',
  breadcrumb: '',
  exact: true,
  childOf: [],
}
// HOME
export const ROUTE_HOME = {
  id: 'home',
  path: '/home',
  breadcrumb: 'Home',
  exact: false,
  childOf: [],
}
// LISTE
export const ROUTE_DEVICES = {
  id: 'devices',
  path: '/devices',
  breadcrumb: 'Dispositivi',
  exact: true,
  childOf: [ROUTE_HOME],
}
export const ROUTE_EXPERIENCES = {
  id: 'experiences',
  path: '/experiences',
  breadcrumb: 'Esperienze',
  exact: true,
  childOf: [ROUTE_HOME],
}
export const ROUTE_POIS = {
  id: 'pois',
  path: '/pois',
  breadcrumb: 'Punti di Interesse',
  exact: true,
  childOf: [ROUTE_HOME],
}
// NUOVI ELEMENTI
export const ROUTE_NEW_DEVICE = {
  id: 'new-device',
  path: '/devices/newDevice',
  breadcrumb: 'Nuovo dispositivo',
  exact: true,
  childOf: [ROUTE_DEVICES],
}
export const ROUTE_NEW_EXPERIENCE = {
  id: 'new-experience',
  path: '/experiences/newExperience',
  breadcrumb: 'Nuova esperienza',
  exact: true,
  childOf: [ROUTE_EXPERIENCES],
}
export const ROUTE_NEW_POI = {
  id: 'new-poi',
  path: '/pois/newPoi',
  breadcrumb: 'Nuovo POI',
  exact: true,
  childOf: [ROUTE_POIS],
}
// DETTAGLI ELEMENTI
export const ROUTE_DEVICE_DETAIL = {
  id: 'device-detail',
  path: '/devices/:parDeviceId',
  breadcrumb: 'Dettaglio dispositivo',
  exact: false,
  childOf: [ROUTE_DEVICES],
}
export const ROUTE_EXPERIENCE_DETAIL = {
  id: 'experience-detail',
  path: '/experiences/:parExperienceId',
  breadcrumb: 'Dettaglio esperienza',
  exact: false,
  childOf: [ROUTE_EXPERIENCES],
}
// 404
// export const ROUTE_NOT_FOUND = {
//   id: 'not-found',
//   path: '*',
//   breadcrumb: 'Pagina non trovata!',
//   exact: false,
//   childOf: [],
// }

export const routes = [
  // ROOT
  ROUTE_ROOT,
  // HOME
  ROUTE_HOME,
  // LISTE
  ROUTE_DEVICES,
  ROUTE_EXPERIENCES,
  ROUTE_POIS,
  // NUOVI ELEMENTI
  ROUTE_NEW_DEVICE,
  ROUTE_NEW_EXPERIENCE,
  ROUTE_NEW_POI,
  // DETTAGLI ELEMENTI
  ROUTE_DEVICE_DETAIL,
  ROUTE_EXPERIENCE_DETAIL,
  // 404
  // ROUTE_NOT_FOUND,
]

/* NB: gli elementi da renderizzare per ogni route sono stati messi in un array separato e non all'interno dei singoli oggetti delle route. Questo perché
  le singole route devono essere pushate all'interno dello stato globale gestito da Redux in maniera tale da visualizzare le breadcrumb
  (vedere BreadcrumbManager.js per l'algoritmo di costruzione e rendering delle breadcrumb), ed è best practice evitare di pushare
  elementi non serializzabili all'interno dello stato. */
export const routesElements = [
  {
    id: 'root',
    element: <Redirect to='/home' key='root' />,
  },
  {
    id: 'home',
    element: <Home key='home' />,
  },
  {
    id: 'devices',
    element: <Devices key='devices' />,
  },
  {
    id: 'experiences',
    element: <Experiences key='experiences' />,
  },
  {
    id: 'pois',
    element: <Pois key='pois' />,
  },
  {
    id: 'new-device',
    element: <NewDevice key='new-device' />,
  },
  {
    id: 'new-experience',
    element: <NewExperience key='new-experience' />,
  },
  {
    id: 'new-poi',
    element: <NewPoi key='new-poi' />,
  },
  {
    id: 'device-detail',
    element: <DeviceDetail key='device-detail' />,
  },
  {
    id: 'experience-detail',
    element: <ExperienceDetail key='experience-detail' />,
  },
  // {
  //   id: 'not-found',
  //   element: <NotFound key='not-found' />,
  // },
]
