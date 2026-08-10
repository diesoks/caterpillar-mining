import type { EquipmentUnit } from '../../../domain/equipmentUnit/EquipmentUnit'
import { EquipmentUnitTableRow } from './EquipmentUnitTableRow'
import styles from './EquipmentUnitTable.module.css'

interface EquipmentUnitTableProps {
  equipmentUnits: EquipmentUnit[]
  onEdit: (equipmentUnit: EquipmentUnit) => void
  onDelete: (equipmentUnit: EquipmentUnit) => void
}

// Renders the equipment units collection as a table, or an empty-state message when there is
// nothing to show yet. Purely presentational - all state lives in the parent page.
export function EquipmentUnitTable({ equipmentUnits, onEdit, onDelete }: EquipmentUnitTableProps) {
  if (equipmentUnits.length === 0) {
    return <p className={styles.emptyState}>No equipment units registered yet.</p>
  }

  return (
    <table className={styles.table}>
      <thead>
        <tr>
          <th>ID</th>
          <th>Model</th>
          <th>Serial number</th>
          <th>Status</th>
          <th>Mine site</th>
          <th>GPS (lat, long)</th>
          <th>Hours</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {equipmentUnits.map((equipmentUnit) => (
          <EquipmentUnitTableRow
            key={equipmentUnit.id}
            equipmentUnit={equipmentUnit}
            onEdit={onEdit}
            onDelete={onDelete}
          />
        ))}
      </tbody>
    </table>
  )
}
