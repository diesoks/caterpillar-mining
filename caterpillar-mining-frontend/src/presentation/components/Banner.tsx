import styles from './Banner.module.css'

type BannerType = 'error' | 'success'

interface BannerProps {
  type: BannerType
  message: string
  onDismiss?: () => void
}

// Small reusable banner for surfacing error or success messages returned by the API.
export function Banner({ type, message, onDismiss }: BannerProps) {
  return (
    <div className={`${styles.banner} ${styles[type]}`} role="alert">
      <span>{message}</span>
      {onDismiss && (
        <button
          type="button"
          className={styles.dismissButton}
          onClick={onDismiss}
          aria-label="Dismiss message"
        >
          &times;
        </button>
      )}
    </div>
  )
}
