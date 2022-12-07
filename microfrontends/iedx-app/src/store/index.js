import { configureStore } from '@reduxjs/toolkit'

import routeSlice from './route-slice'

const store = configureStore({
  reducer: {
    route: routeSlice.reducer,
  },
})

export default store
