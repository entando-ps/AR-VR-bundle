import React from 'react'
import { LinkContainer } from 'react-router-bootstrap'
import Badge from 'react-bootstrap/Badge'
import { ACTIVE, ADULT, BLANK_SPACE, CHILD, ELDER, INACTIVE, MISSING_DETAIL, TEEN, YOUNG_ADULT } from '../../common/system-wide-variables'
import { ROUTE_EXPERIENCES } from '../../common/routing/routes'

const ExperienceTableItem = props => {
  const { name, upload, status, profiles, id } = props.detailedExperience

  const nameToRender = name ? name : MISSING_DETAIL
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
  const profilesToRender =
    profiles && profiles.profiles
      ? profiles.profiles
          .map(profile => {
            switch (profile.age) {
              case CHILD.value:
                return BLANK_SPACE + CHILD.label
              case TEEN.value:
                return BLANK_SPACE + TEEN.label
              case YOUNG_ADULT.value:
                return BLANK_SPACE + YOUNG_ADULT.label
              case ADULT.value:
                return BLANK_SPACE + ADULT.label
              case ELDER.value:
                return BLANK_SPACE + ELDER.label
              default:
                return null
            }
          })
          .toString()
      : MISSING_DETAIL

  const dateToRender = new Date(upload).getDate() + '-' + (new Date(upload).getMonth() + 1) + '-' + new Date(upload).getFullYear()

  return (
    <LinkContainer to={`${ROUTE_EXPERIENCES.path}/${id}`}>
      <tr>
        <td>{nameToRender}</td>
        <td>{dateToRender}</td>
        <td>{statusToRender}</td>
        <td>{profilesToRender}</td>
      </tr>
    </LinkContainer>
  )
}

export default ExperienceTableItem
