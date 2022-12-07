import React from 'react'
import { Button } from 'react-bootstrap'
import { MISSING_DETAIL } from '../../common/system-wide-variables'
import { deletePoi } from '../../integration/Integration'

const PoisTableItem = props => {
  const { name, info, id, location } = props.poi

  const nameToRender = name ? name : MISSING_DETAIL
  const infoToRender = info ? info : MISSING_DETAIL
  const latitudeToRender = location.lat ? location.lat : MISSING_DETAIL
  const longitudeToRender = location.lng ? location.lng : MISSING_DETAIL

  const handleDelete = async () => {
    const poi = await deletePoi(id)

    if (!poi.isError) {
      console.log(`Il POI ${name} è stato eliminato correttamente`)
    } else {
      console.error("Errore nell'eliminazione del POI:", poi.data.message)
    }

    props.setReloadToken(new Date().getTime().toString())
  }

  return (
    <tr>
      <td>{nameToRender}</td>
      <td>{infoToRender}</td>
      <td>{latitudeToRender}</td>
      <td>{longitudeToRender}</td>
      <td>
        <Button size='sm' variant='danger' onClick={handleDelete}>
          Elimina POI
        </Button>
      </td>
    </tr>
  )
}

export default PoisTableItem
