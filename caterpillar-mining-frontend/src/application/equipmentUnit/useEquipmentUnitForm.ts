import { useState, type FormEvent } from 'react'
import {
  OPERATION_STATUS_OPTIONS,
  type EquipmentUnit,
  type EquipmentUnitFormValues,
  type EquipmentUnitPayload,
} from '../../domain/equipmentUnit/EquipmentUnit'
import { extractErrorMessage } from '../../shared/extractErrorMessage'

const EMPTY_FORM_VALUES: EquipmentUnitFormValues = {
  model: '',
  serialNumber: '',
  operationStatus: 'ACTIVE',
  assignedMineSite: '',
  gpsLatitude: '',
  gpsLongitude: '',
  hoursOfOperation: '',
}

function toFormValues(equipmentUnit: EquipmentUnit): EquipmentUnitFormValues {
  return {
    model: equipmentUnit.model,
    serialNumber: equipmentUnit.serialNumber,
    operationStatus: equipmentUnit.operationStatus,
    assignedMineSite: equipmentUnit.assignedMineSite,
    gpsLatitude: String(equipmentUnit.gpsLatitude),
    gpsLongitude: String(equipmentUnit.gpsLongitude),
    hoursOfOperation: String(equipmentUnit.hoursOfOperation),
  }
}

function toPayload(formValues: EquipmentUnitFormValues): EquipmentUnitPayload {
  return {
    model: formValues.model.trim(),
    serialNumber: formValues.serialNumber.trim(),
    operationStatus: formValues.operationStatus,
    assignedMineSite: formValues.assignedMineSite.trim(),
    gpsLatitude: Number(formValues.gpsLatitude),
    gpsLongitude: Number(formValues.gpsLongitude),
    hoursOfOperation: Number(formValues.hoursOfOperation),
  }
}

interface UseEquipmentUnitFormOptions {
  editingUnit: EquipmentUnit | null
  onSubmit: (payload: EquipmentUnitPayload) => Promise<void>
}

// Application layer: owns the equipment-unit form's state, field-change handling and submission
// flow (including turning any thrown error into a display-ready message), independent of how the
// form is rendered. The caller is expected to remount this hook (via a React "key") whenever
// "editingUnit" switches to a different record, rather than relying on an effect to resync state.
export function useEquipmentUnitForm({ editingUnit, onSubmit }: UseEquipmentUnitFormOptions) {
  const [formValues, setFormValues] = useState<EquipmentUnitFormValues>(() =>
    editingUnit ? toFormValues(editingUnit) : EMPTY_FORM_VALUES,
  )
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [formError, setFormError] = useState<string | null>(null)

  const handleFieldChange = (field: keyof EquipmentUnitFormValues, value: string) => {
    setFormValues((current) => ({ ...current, [field]: value }))
  }

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setFormError(null)
    setIsSubmitting(true)
    try {
      await onSubmit(toPayload(formValues))
    } catch (caughtError) {
      setFormError(extractErrorMessage(caughtError))
    } finally {
      setIsSubmitting(false)
    }
  }

  return {
    formValues,
    isSubmitting,
    formError,
    handleFieldChange,
    handleSubmit,
    operationStatusOptions: OPERATION_STATUS_OPTIONS,
  }
}
