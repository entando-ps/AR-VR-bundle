import React from 'react'

const CheckboxGroup = props => {
  const { errors, register, name, labels, values } = props

  const checkboxesToRender = values.map((value, index) => {
    return (
      <label key={labels[index]}>
        <input type='checkbox' value={value} name={name} {...register(name)} />
        {labels[index]}
      </label>
    )
  })

  return (
    <div className='iedx-checkbox-wrapper'>
      {checkboxesToRender}
      {errors[name] && <p>{errors[name].message}</p>}
    </div>
  )
}

export default CheckboxGroup
