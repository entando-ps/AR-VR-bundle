import React from 'react'

import {
  ASSET_TYPE_IMAGE,
  ASSET_TYPE_VIDEO,
  ASSET_TYPE_THUMBNAIL,
  ASSET_TYPE_SUBTITLE,
  ASSET_TYPE_DETAILS_PANEL,
  MISSING_DETAIL,
} from '../../common/system-wide-variables'

const AssetTableItem = props => {
  const { title, assetFilename, type } = props.asset

  const setTypeToRender = () => {
    switch (type) {
      case ASSET_TYPE_IMAGE.value:
        return ASSET_TYPE_IMAGE.label
      case ASSET_TYPE_VIDEO.value:
        return ASSET_TYPE_VIDEO.label
      case ASSET_TYPE_THUMBNAIL.value:
        return ASSET_TYPE_THUMBNAIL.label
      case ASSET_TYPE_SUBTITLE.value:
        return ASSET_TYPE_SUBTITLE.label
      case ASSET_TYPE_DETAILS_PANEL.value:
        return ASSET_TYPE_DETAILS_PANEL.label
      default:
        return MISSING_DETAIL
    }
  }

  const typeToRender = setTypeToRender()
  const titleToRender = title ? title : MISSING_DETAIL
  const assetFilenameToRender = assetFilename ? assetFilename : MISSING_DETAIL

  return (
    <tr>
      <td>{titleToRender}</td>
      <td>{assetFilenameToRender}</td>
      <td>{typeToRender}</td>
    </tr>
  )
}

export default AssetTableItem
