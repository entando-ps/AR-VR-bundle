import React, { useEffect, useState } from 'react'
import { Table } from 'react-bootstrap'
import { POLLING_INTERVAL_ASSET_LIST, EXPERIENCE_ASSETS_NO_ITEMS } from '../../../common/system-wide-variables'

import { getSingleExperienceAssets } from '../../../integration/Integration'
import AssetTableItem from '../../layout/AssetTableItem'

const ExperienceAssets = props => {
  // console.log('ExperienceAssets -> PROPS', props)

  const [currentExperienceAssets, setCurrentExperienceAssets] = useState([])
  const [assetsToRender, setAssetsToRender] = useState([])

  useEffect(() => {
    const getAssets = async () => {
      const assets = await getSingleExperienceAssets(props.currentExperience.id)
      // console.log('ASSETS', assets)

      if (!assets.error) {
        if (assets.data?.assets?.length > 0) {
          setCurrentExperienceAssets(
            assets.data.assets.filter(scenario => {
              return scenario.assetPresentOnDisk
            })
          )
        } else {
          console.log(
            `L'esperienza ${props.currentExperience.name} non contiene alcun scenario`)
        }
      } else {
        console.error(
          `Errore nel recupero degli asset per l'esperienza ${props.currentExperience.name}`,
          assets.data.message)
      }
    }

    getAssets()

    const intervalId = setInterval(() => {
      process.env.NODE_ENV === 'development' && console.log('Polling assets...')
      getAssets()
    }, POLLING_INTERVAL_ASSET_LIST)

    return () => clearInterval(intervalId)
  }, [props.currentExperience.id, props.currentExperience.name,
    props.reloadToken])

  useEffect(() => {
    if (currentExperienceAssets.length > 0) {
      setAssetsToRender(currentExperienceAssets.map((asset, index) => {
        return <AssetTableItem asset={asset} key={index}/>
      }))
    }
  }, [currentExperienceAssets])

  // console.log('ExperienceAssets', currentExperienceAssets)

  return (
    <div className="lol">
      {assetsToRender?.length > 0 && (
        <Table striped hover responsive>
          <thead>
            <tr>
              <th>Titolo lilli</th>
              <th>Source</th>
              <th>Tipo</th>
            </tr>
          </thead>
          <tbody>{assetsToRender}</tbody>
        </Table>
      )}
      {assetsToRender?.length === 0 && <p>{EXPERIENCE_ASSETS_NO_ITEMS}</p>}
    </div>
  )
}

export default ExperienceAssets
