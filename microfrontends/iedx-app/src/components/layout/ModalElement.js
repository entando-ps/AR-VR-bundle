import React from 'react'
import { Button, Modal } from 'react-bootstrap'

import FileViewer from '../form/FileViewer'

const ModalElement = props => {
  const { modalBody, handleCloseModal } = props

  return (
    <Modal show onHide={handleCloseModal}>
      <Modal.Body>
        <FileViewer data={modalBody} />
      </Modal.Body>
      <Modal.Footer>
        <Button onClick={handleCloseModal}>Chiudi</Button>
      </Modal.Footer>
    </Modal>
  )
}

export default ModalElement
