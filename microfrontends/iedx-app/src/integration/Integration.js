import { deleteData, getData, postData, putData } from './Http'

// endpoints
export const urlDevices = `${process.env.REACT_APP_PUBLIC_API_URL}/devices`
export const urlSingleDevice = `${process.env.REACT_APP_PUBLIC_API_URL}/device`
export const urlExperiences = `${process.env.REACT_APP_PUBLIC_API_URL}/experiences`
export const urlSingleExperience = `${process.env.REACT_APP_PUBLIC_API_URL}/experience`
export const urlSingleAsset = {
  firstPart: urlSingleExperience,
  secondPart: '/asset?isAsset=',
}
export const urlPois = `${process.env.REACT_APP_PUBLIC_API_URL}/pois`
export const urlSinglePoi = `${process.env.REACT_APP_PUBLIC_API_URL}/poi`

/***********
 * GENERAL
 */
export const appendFileListToFormData = (formData, fileList, label) => {
  if (fileList?.length > 0) {
    Array.from(fileList).map(file => formData.append(label, file))
  }
}

export const appendSingleValueToFormData = (formData, value, label) => {
  formData.append(label, value)
}

export const serializeProfiles = profiles => {
  if (profiles?.length > 0) {
    return profiles.map(profile => {
      return { age: profile }
    })
  }

  return
}

export const deserializeProfiles = profiles => {
  if (profiles?.length > 0) {
    return profiles.map(profile => {
      return profile.age
    })
  }
}

/***********
 * DEVICES
 */
export const getAllDevices = async () => {
  const data = await getData(urlDevices)

  return data
}

export const getSingleDevice = async id => {
  const data = await getData(`${urlSingleDevice}/${id}`)

  return data
}

export const addNewDevice = async payload => {
  const data = await postData(urlSingleDevice, payload)

  return data
}

export const editDevice = async payload => {
  const data = await putData(urlSingleDevice, payload)

  return data
}

export const deleteDevice = async id => {
  const data = await deleteData(`${urlSingleDevice}/${id}`)

  return data
}

/***************
 * EXPERIENCES
 */
export const getAllExperiences = async () => {
  const data = await getData(urlExperiences)

  return data
}

export const getSingleExperience = async id => {
  const data = await getData(`${urlSingleExperience}/${id}`)

  return data
}

export const addNewExperience = async formData => {
  const data = await postData(urlSingleExperience, formData)

  return data
}

export const editExperience = async payload => {
  const data = await putData(urlSingleExperience, payload)

  return data
}

export const deleteExperience = async id => {
  const data = await deleteData(`${urlSingleExperience}/${id}`)

  return data
}

export const getSingleExperienceXml = async id => {
  const data = await getData(`${urlSingleExperience}/${id}/xml`)

  return data
}

export const getSingleExperienceThumbnail = async id => {
  const data = await getData(`${urlSingleExperience}/${id}/thumbnail`, 'blob')

  return data
}

export const getSingleExperienceAssets = async id => {
  const data = await getData(`${urlSingleExperience}/${id}/describe`)

  return data
}

export const addAssetToExperience = async (id, formData, assetFlag) => {
  const data = await putData(`${urlSingleAsset.firstPart}/${id}${urlSingleAsset.secondPart}${assetFlag}`, formData)

  return data
}

/***************
 * POI punti di interesse
 */

export const getAllPois = async () => {
  const data = await getData(urlPois)

  return data
}

export const getSinglePoi = async id => {
  const data = await getData(`${urlSinglePoi}/${id}`)

  return data
}

export const addNewPoi = async payload => {
  const data = await postData(urlSinglePoi, payload)

  return data
}

export const deletePoi = async id => {
  const data = await deleteData(`${urlSinglePoi}/${id}`)

  return data
}
