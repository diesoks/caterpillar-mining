import type { MouseEvent, ReactNode } from 'react'
import styles from './Modal.module.css'

interface ModalProps {
  isOpen: boolean
  title: string
  onClose: () => void
  children: ReactNode
}

// Generic, reusable modal dialog shell. Feature components supply the title and body content;
// this component only owns the overlay/positioning/close behavior.
export function Modal({ isOpen, title, onClose, children }: ModalProps) {
  if (!isOpen) return null

  const handleOverlayClick = () => {
    onClose()
  }

  const handleContentClick = (event: MouseEvent<HTMLDivElement>) => {
    event.stopPropagation()
  }

  return (
    <div className={styles.overlay} onClick={handleOverlayClick}>
      <div className={styles.content} onClick={handleContentClick}>
        <div className={styles.header}>
          <h2 className={styles.title}>{title}</h2>
          <button
            type="button"
            className={styles.closeButton}
            onClick={onClose}
            aria-label="Close dialog"
          >
            &times;
          </button>
        </div>
        {children}
      </div>
    </div>
  )
}
