import styles from './Spinner.module.css'

// Minimal loading indicator, reused anywhere the app is waiting on a network request.
export function Spinner() {
  return <div className={styles.spinner} role="status" aria-label="Loading" />
}
