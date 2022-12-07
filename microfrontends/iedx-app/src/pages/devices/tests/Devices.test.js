import { render, screen } from '@testing-library/react'
import { rest } from 'msw'

import { Wrapper } from '../../../common/test-utils/testing-library-utils'
import Devices from '../Devices'
import {
  DEVICES_BUTTON_LABEL,
  DEVICES_NO_ITEMS,
  DEVICES_SUBTITLE_LABEL,
  DEVICES_TABLE_TITLE_LABEL,
  DEVICES_TITLE_LABEL,
} from '../../../common/system-wide-variables'
import { server } from '../../../mocks/server'
import { urlDevices } from '../../../integration/Integration'

test('Devices page render correctly with no devices in the list', async () => {
  server.resetHandlers(
    rest.get(urlDevices, (req, res, ctx) => {
      return res(ctx.json([]))
    })
  )
  render(<Devices key='devices' />, { wrapper: Wrapper })

  const mainTitle = screen.getByText(DEVICES_TITLE_LABEL)
  expect(mainTitle).toBeInTheDocument()

  const mainSubtitles = screen.getByText(DEVICES_SUBTITLE_LABEL)
  expect(mainSubtitles).toBeInTheDocument()

  const tableTitle = screen.getByText(DEVICES_TABLE_TITLE_LABEL)
  expect(tableTitle).toBeInTheDocument()

  const addNewDeviceButton = screen.getByRole('button', { name: DEVICES_BUTTON_LABEL })
  expect(addNewDeviceButton).toBeInTheDocument()
  expect(addNewDeviceButton).toBeEnabled()

  // moccare be per far funzionare questo test
  const noItemsMessage = await screen.findByText(DEVICES_NO_ITEMS)
  expect(noItemsMessage).toBeInTheDocument()
})
