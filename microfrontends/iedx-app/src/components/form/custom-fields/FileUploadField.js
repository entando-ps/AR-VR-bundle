import React, { useEffect } from 'react'
import { Button } from 'react-bootstrap'

const FileUploadField = props => {
  const { watch, errors, register, handleFieldReset, label, name, buttonLabel, multipleUploads } = props

  useEffect(() => {
    if (watch[name] === 0) handleFieldReset(name)
  }, [watch, handleFieldReset, name])

  return (
    <>
      <label className='form-label' htmlFor={name}>
        {label}
      </label>
      <input type='file' className='form-control' id={name} name={name} multiple={multipleUploads} {...register(name)} />
      {watch()[name] && watch()[name].length > 0 ? <div className="iedx-empty-button iedx-action-button-space-end"><Button onClick={() => handleFieldReset(name)}>{buttonLabel}</Button></div> : ''}
      {errors[name] && <p>{errors[name].message}</p>}
    </>
  )
}

export default FileUploadField
