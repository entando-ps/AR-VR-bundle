import React from 'react'
import { LinkContainer } from 'react-router-bootstrap'
import Badge from 'react-bootstrap/Badge'
import { ACTIVE, INACTIVE, MISSING_DETAIL } from '../../common/system-wide-variables'
import { ROUTE_DEVICES } from '../../common/routing/routes'

const DeviceTableItem = props => {
  const { name, note, status, id } = props.device

  const nameToRender = name ? name : MISSING_DETAIL
  const noteToRender = note ? note : MISSING_DETAIL
  const statusToRender =
    status === ACTIVE.value ? (
      <Badge pill bg='success'>
        {ACTIVE.maleLabel}
      </Badge>
    ) : (
      <Badge pill bg='danger'>
        {INACTIVE.maleLabel}
      </Badge>
    )

  return (
    <LinkContainer to={`${ROUTE_DEVICES.path}/${id}`}>
      <tr>
        <td>{nameToRender}</td>
        <td>{noteToRender}</td>
        <td>{statusToRender}</td>
      </tr>
    </LinkContainer>
  )
}

export default DeviceTableItem
