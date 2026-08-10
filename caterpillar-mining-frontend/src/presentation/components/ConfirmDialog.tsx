import { Modal } from './Modal'
import { Button } from './Button'
import { Banner } from './Banner'
import styles from './ConfirmDialog.module.css'

interface ConfirmDialogProps {
  isOpen: boolean
  title: string
  message: string
  errorMessage?: string | null
  isConfirming: boolean
  onConfirm: () => void
  onCancel: () => void
}

// Reusable yes/no confirmation dialog, built on top of the generic Modal + Button components.
export function ConfirmDialog({
  isOpen,
  title,
  message,
  errorMessage,
  isConfirming,
  onConfirm,
  onCancel,
}: ConfirmDialogProps) {
  return (
    <Modal isOpen={isOpen} title={title} onClose={onCancel}>
      {errorMessage && <Banner type="error" message={errorMessage} />}
      <p className={styles.message}>{message}</p>
      <div className={styles.actions}>
        <Button variant="secondary" onClick={onCancel} disabled={isConfirming}>
          Cancel
        </Button>
        <Button variant="danger" onClick={onConfirm} disabled={isConfirming}>
          {isConfirming ? 'Deleting...' : 'Delete'}
        </Button>
      </div>
    </Modal>
  )
}
