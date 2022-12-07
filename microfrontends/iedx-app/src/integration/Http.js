import axios from 'axios'
import { SECURITY_TOKEN } from '../common/system-wide-variables'

const config = {
  headers: {
    'i-edx-token': SECURITY_TOKEN,
  },
}

export const getData = async (url, responseType) => {
  const data = await axios
    .get(url, { ...config, responseType: responseType ? responseType : null })
    .then(res => {
      return res.data
    })
    .catch(e => {
      return e
    })

  return errorCheck(data)
}

export const postData = async (url, payload) => {
  const data = await axios
    .post(url, payload, config)
    .then(res => {
      return res
    })
    .catch(e => {
      return e
    })

  return errorCheck(data)
}

export const putData = async (url, payload) => {
  const data = await axios
    .put(url, payload, config)
    .then(res => {
      return res
    })
    .catch(e => {
      return e
    })

  return errorCheck(data)
}

export const deleteData = async url => {
  const data = await axios
    .delete(url, config)
    .then(res => {
      return res
    })
    .catch(e => {
      return e
    })

  return errorCheck(data)
}

/**
 * Controlla se ci sono errori nella richiesta http appena eseguita.
 * Restituisce sempre un oggetto così formato:
 * {
 *   data,     (contiene i dati di risposta dalla richiesta http oppure i dati dell'errore)
 *   isError,  (true o false)
 * }
 */
const errorCheck = data => {
  if (data.hasOwnProperty('toJSON') && data.toJSON().name === 'Error') {
    return {
      data: data.toJSON(),
      isError: true,
    }
  }

  return {
    data,
    isError: false,
  }
}
