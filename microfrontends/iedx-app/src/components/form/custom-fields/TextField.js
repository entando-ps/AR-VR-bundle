import React from 'react'

const TextField = props => {
  const {errors, register, name, label, placeholder, type, disabled} = props

  return (
    <>
      <label className='form-label' htmlFor={name}>
        {label}
      </label>
      <input className='form-control' id={name} name={name} type={type}
             placeholder={placeholder} {...register(name)} disabled={disabled}/>
      {errors[name] && <p>{errors[name].message}</p>}
    </>
  )
}

export default TextField
