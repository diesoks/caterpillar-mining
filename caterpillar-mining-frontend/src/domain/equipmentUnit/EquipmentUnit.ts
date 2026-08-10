// Domain layer: framework-agnostic types describing the MiningEquipmentUnit business entity.
// This layer has no dependency on axios, React, or any other outer layer.

export type OperationStatus = 'ACTIVE' | 'IN_MAINTENANCE' | 'INACTIVE'

export const OPERATION_STATUS_OPTIONS: OperationStatus[] = [
  'ACTIVE',
  'IN_MAINTENANCE',
  'INACTIVE',
]

export interface EquipmentUnit {
  id: number
  equipmentUnitId: string
  model: string
  serialNumber: string
  operationStatus: OperationStatus
  assignedMineSite: string
  gpsLatitude: number
  gpsLongitude: number
  hoursOfOperation: number
  createdAt: string
  updatedAt: string
}

// Shape accepted by the backend's create/update endpoints.
export interface EquipmentUnitPayload {
  model: string
  serialNumber: string
  operationStatus: OperationStatus
  assignedMineSite: string
  gpsLatitude: number
  gpsLongitude: number
  hoursOfOperation: number
}

// Controlled-input-friendly shape (every field is a string) used by the equipment unit form.
export interface EquipmentUnitFormValues {
  model: string
  serialNumber: string
  operationStatus: OperationStatus
  assignedMineSite: string
  gpsLatitude: string
  gpsLongitude: string
  hoursOfOperation: string
}
