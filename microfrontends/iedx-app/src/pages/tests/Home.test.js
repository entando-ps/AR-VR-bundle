import { render, screen } from '@testing-library/react'

import Home from '../Home'
import { Wrapper } from '../../common/test-utils/testing-library-utils'
import {
  HOME_DEVICES_SUBTITLE_LABEL,
  HOME_DEVICES_TITLE_LABEL,
  HOME_EXPERIENCES_SUBTITLE_LABEL,
  HOME_EXPERIENCES_TITLE_LABEL,
  HOME_POIS_SUBTITLE_LABEL,
  HOME_POIS_TITLE_LABEL,
  HOME_SUBTITLE_LABEL,
  HOME_TITLE_LABEL,
} from '../../common/system-wide-variables'

test('Home page renders correctly', () => {
  render(<Home />, { wrapper: Wrapper })

  const mainTitle = screen.getByText(HOME_TITLE_LABEL)
  expect(mainTitle).toBeInTheDocument()

  const mainSubtitles = screen.getByText(HOME_SUBTITLE_LABEL)
  expect(mainSubtitles).toBeInTheDocument()

  const devicesCardTitle = screen.getByText(HOME_DEVICES_TITLE_LABEL)
  expect(devicesCardTitle).toBeInTheDocument()

  const devicesCardSubtitles = screen.getByText(HOME_DEVICES_SUBTITLE_LABEL)
  expect(devicesCardSubtitles).toBeInTheDocument()

  const experiencesCardTitle = screen.getByText(HOME_EXPERIENCES_TITLE_LABEL)
  expect(experiencesCardTitle).toBeInTheDocument()

  const experiencesCardSubtitle = screen.getByText(HOME_EXPERIENCES_SUBTITLE_LABEL)
  expect(experiencesCardSubtitle).toBeInTheDocument()

  const poiCardTitle = screen.getByText(HOME_POIS_TITLE_LABEL)
  expect(poiCardTitle).toBeInTheDocument()

  const poiCardSubtitles = screen.getByText(HOME_POIS_SUBTITLE_LABEL)
  expect(poiCardSubtitles).toBeInTheDocument()
})
