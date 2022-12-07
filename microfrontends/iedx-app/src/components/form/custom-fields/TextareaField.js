import React from 'react'

const TextareaField = props => {
  const { errors, register, name, label, placeholder } = props

  return (
    <>
      <label className='form-label' htmlFor={name}>
        {label}
      </label>
      <textarea className='form-control' id={name} name={name} placeholder={placeholder} as='textarea' {...register(name)} />
      {errors[name] && <p>{errors[name].message}</p>}
    </>
  )
}

export default TextareaField
