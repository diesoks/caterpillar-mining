import { useCallback, useEffect, useState } from 'react'
import { EquipmentUnitApi } from '../../infrastructure/equipmentUnit/EquipmentUnitApi'
import type { EquipmentUnit, EquipmentUnitPayload } from '../../domain/equipmentUnit/EquipmentUnit'
import { extractErrorMessage } from '../../shared/extractErrorMessage'

// Application layer: orchestrates the EquipmentUnitApi (infrastructure) to expose the
// equipment-units use cases (list, create, update, delete) as plain state + functions, with no
// knowledge of how they will be rendered.
export function useEquipmentUnits() {
  const [equipmentUnits, setEquipmentUnits] = useState<EquipmentUnit[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchEquipmentUnits = useCallback(async () => {
    setIsLoading(true)
    setError(null)
    try {
      const data = await EquipmentUnitApi.getAll()
      setEquipmentUnits(data)
    } catch (caughtError) {
      setError(extractErrorMessage(caughtError))
    } finally {
      setIsLoading(false)
    }
  }, [])

  useEffect(() => {
    void fetchEquipmentUnits()
  }, [fetchEquipmentUnits])

  const createEquipmentUnit = useCallback(async (payload: EquipmentUnitPayload) => {
    const created = await EquipmentUnitApi.create(payload)
    setEquipmentUnits((current) => [...current, created])
    return created
  }, [])

  const updateEquipmentUnit = useCallback(async (id: number, payload: EquipmentUnitPayload) => {
    const updated = await EquipmentUnitApi.update(id, payload)
    setEquipmentUnits((current) => current.map((unit) => (unit.id === id ? updated : unit)))
    return updated
  }, [])

  const deleteEquipmentUnit = useCallback(async (id: number) => {
    await EquipmentUnitApi.remove(id)
    setEquipmentUnits((current) => current.filter((unit) => unit.id !== id))
  }, [])

  return {
    equipmentUnits,
    isLoading,
    error,
    refetch: fetchEquipmentUnits,
    createEquipmentUnit,
    updateEquipmentUnit,
    deleteEquipmentUnit,
  }
}
