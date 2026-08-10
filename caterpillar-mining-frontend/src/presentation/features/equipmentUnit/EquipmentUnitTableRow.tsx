import type { EquipmentUnit } from '../../../domain/equipmentUnit/EquipmentUnit'
import { OperationStatusBadge } from '../../components/OperationStatusBadge'
import { Button } from '../../components/Button'
import styles from './EquipmentUnitTable.module.css'

interface EquipmentUnitTableRowProps {
  equipmentUnit: EquipmentUnit
  onEdit: (equipmentUnit: EquipmentUnit) => void
  onDelete: (equipmentUnit: EquipmentUnit) => void
}

// Small, presentational component: renders a single row and delegates all behavior to its
// parent via the onEdit/onDelete callbacks.
export function EquipmentUnitTableRow({ equipmentUnit, onEdit, onDelete }: EquipmentUnitTableRowProps) {
  const handleEditClick = () => {
    onEdit(equipmentUnit)
  }

  const handleDeleteClick = () => {
    onDelete(equipmentUnit)
  }

  return (
    <tr>
      <td>{equipmentUnit.id}</td>
      <td>{equipmentUnit.model}</td>
      <td>{equipmentUnit.serialNumber}</td>
      <td>
        <OperationStatusBadge status={equipmentUnit.operationStatus} />
      </td>
      <td>{equipmentUnit.assignedMineSite}</td>
      <td>
        {equipmentUnit.gpsLatitude.toFixed(4)}, {equipmentUnit.gpsLongitude.toFixed(4)}
      </td>
      <td>{equipmentUnit.hoursOfOperation}</td>
      <td className={styles.actionsCell}>
        <Button variant="secondary" onClick={handleEditClick}>
          Edit
        </Button>
        <Button variant="danger" onClick={handleDeleteClick}>
          Delete
        </Button>
      </td>
    </tr>
  )
}
