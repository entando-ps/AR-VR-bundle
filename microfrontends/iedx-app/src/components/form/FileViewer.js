import React from 'react'

const FileViewer = props => {
  // console.log('FILE VIEWER', props)

  switch (props?.data?.type) {
    case 'image/jpeg':
    case 'image/jpg':
    case 'image/png':
    case 'image/gif':
      return <img alt='modal  _image' src={URL.createObjectURL(props.data)} />
    default:
      return <>{props.data}</>
  }
}

export default FileViewer
