import React from 'react'

const ToggleSwitch = props => {
  const { watch, errors, register, values, labels, name } = props

  return (
    <>
      <input id='on' name={name} type='radio' value={values[0]} {...register(name)} />
      <input id='off' name={name} type='radio' value={values[1]} {...register(name)} />
      <label htmlFor='on' className='iedx-activate-label iedx-activate-label-on'>
        {labels[0]}
      </label>
      <div className='iedx-activate-toogle'></div>
      <div className='iedx-toogle-container'></div>
      <label htmlFor='off' className='iedx-activate-label iedx-activate-label-off'>
        {labels[1]}
      </label>
      {<span>{watch()[name] === values[0] ? labels[0] : labels[1]}</span>}
      {errors[name] && <p>{errors[name].message}</p>}
    </>
  )
}

export default ToggleSwitch
