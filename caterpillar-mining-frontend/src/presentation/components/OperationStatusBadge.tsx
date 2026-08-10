import type { OperationStatus } from '../../domain/equipmentUnit/EquipmentUnit'
import styles from './OperationStatusBadge.module.css'

interface OperationStatusBadgeProps {
  status: OperationStatus
}

const STATUS_CLASS_NAME: Record<OperationStatus, string> = {
  ACTIVE: styles.active,
  IN_MAINTENANCE: styles.inMaintenance,
  INACTIVE: styles.inactive,
}

const STATUS_LABEL: Record<OperationStatus, string> = {
  ACTIVE: 'Active',
  IN_MAINTENANCE: 'In maintenance',
  INACTIVE: 'Inactive',
}

// Small colored badge representing an equipment unit's operational status.
export function OperationStatusBadge({ status }: OperationStatusBadgeProps) {
  return <span className={`${styles.badge} ${STATUS_CLASS_NAME[status]}`}>{STATUS_LABEL[status]}</span>
}
