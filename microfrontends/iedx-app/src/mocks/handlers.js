import { rest } from 'msw'

import { urlDevices } from '../integration/Integration'

export const handlers = [
  rest.get(urlDevices, (req, res, ctx) => {
    return res(
      ctx.json([
        {
          id: 6,
          deviceId: 'AAA:BBB',
          status: 1,
          note: 'Sala uno',
          name: 'Device 1',
          updated: null,
          added: '2022-03-14T11:51:16+01:00',
        },
        {
          id: 9,
          deviceId: 'CCC:DDD',
          status: 0,
          note: 'Sala due',
          name: 'Device 2',
          updated: null,
          added: '2022-03-14T11:51:16+01:00',
        },
      ])
    )
  }),
]
