import type { ChangeEvent } from 'react'
import type { EquipmentUnit, EquipmentUnitPayload, OperationStatus } from '../../../domain/equipmentUnit/EquipmentUnit'
import { useEquipmentUnitForm } from '../../../application/equipmentUnit/useEquipmentUnitForm'
import { Button } from '../../components/Button'
import { Banner } from '../../components/Banner'
import styles from './EquipmentUnitForm.module.css'

interface EquipmentUnitFormProps {
  editingUnit: EquipmentUnit | null
  onSubmit: (payload: EquipmentUnitPayload) => Promise<void>
  onCancel: () => void
}

// Renders the create/edit form. All state and submission logic live in useEquipmentUnitForm;
// this component is only responsible for wiring inputs to that hook and rendering feedback.
export function EquipmentUnitForm({ editingUnit, onSubmit, onCancel }: EquipmentUnitFormProps) {
  const { formValues, isSubmitting, formError, handleFieldChange, handleSubmit, operationStatusOptions } =
    useEquipmentUnitForm({ editingUnit, onSubmit })

  const handleModelChange = (event: ChangeEvent<HTMLInputElement>) => {
    handleFieldChange('model', event.target.value)
  }

  const handleSerialNumberChange = (event: ChangeEvent<HTMLInputElement>) => {
    handleFieldChange('serialNumber', event.target.value)
  }

  const handleOperationStatusChange = (event: ChangeEvent<HTMLSelectElement>) => {
    handleFieldChange('operationStatus', event.target.value as OperationStatus)
  }

  const handleAssignedMineSiteChange = (event: ChangeEvent<HTMLInputElement>) => {
    handleFieldChange('assignedMineSite', event.target.value)
  }

  const handleGpsLatitudeChange = (event: ChangeEvent<HTMLInputElement>) => {
    handleFieldChange('gpsLatitude', event.target.value)
  }

  const handleGpsLongitudeChange = (event: ChangeEvent<HTMLInputElement>) => {
    handleFieldChange('gpsLongitude', event.target.value)
  }

  const handleHoursOfOperationChange = (event: ChangeEvent<HTMLInputElement>) => {
    handleFieldChange('hoursOfOperation', event.target.value)
  }

  return (
    <form onSubmit={handleSubmit} className={styles.form}>
      {formError && <Banner type="error" message={formError} />}

      <label className={styles.field}>
        <span>Model</span>
        <input value={formValues.model} onChange={handleModelChange} required />
      </label>

      <label className={styles.field}>
        <span>Serial number</span>
        <input value={formValues.serialNumber} onChange={handleSerialNumberChange} required />
      </label>

      <label className={styles.field}>
        <span>Operation status</span>
        <select value={formValues.operationStatus} onChange={handleOperationStatusChange}>
          {operationStatusOptions.map((status) => (
            <option key={status} value={status}>
              {status}
            </option>
          ))}
        </select>
      </label>

      <label className={styles.field}>
        <span>Assigned mine site</span>
        <input value={formValues.assignedMineSite} onChange={handleAssignedMineSiteChange} required />
      </label>

      <div className={styles.row}>
        <label className={styles.field}>
          <span>GPS latitude</span>
          <input
            type="number"
            step="any"
            min={-90}
            max={90}
            value={formValues.gpsLatitude}
            onChange={handleGpsLatitudeChange}
            required
          />
        </label>

        <label className={styles.field}>
          <span>GPS longitude</span>
          <input
            type="number"
            step="any"
            min={-180}
            max={180}
            value={formValues.gpsLongitude}
            onChange={handleGpsLongitudeChange}
            required
          />
        </label>
      </div>

      <label className={styles.field}>
        <span>Hours of operation</span>
        <input
          type="number"
          min={0}
          value={formValues.hoursOfOperation}
          onChange={handleHoursOfOperationChange}
          required
        />
      </label>

      <div className={styles.actions}>
        <Button type="button" variant="secondary" onClick={onCancel} disabled={isSubmitting}>
          Cancel
        </Button>
        <Button type="submit" disabled={isSubmitting}>
          {isSubmitting ? 'Saving...' : editingUnit ? 'Save changes' : 'Register unit'}
        </Button>
      </div>
    </form>
  )
}
