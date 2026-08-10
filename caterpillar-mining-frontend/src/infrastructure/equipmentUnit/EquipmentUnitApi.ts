import { httpClient } from '../http/httpClient'
import type { EquipmentUnit, EquipmentUnitPayload } from '../../domain/equipmentUnit/EquipmentUnit'

const RESOURCE_PATH = '/equipment-units'

// Infrastructure layer: the only place in the app that knows about REST/axios. It talks to the
// mining bounded context's EquipmentUnitsController and adapts responses back into domain types.
export const EquipmentUnitApi = {
  async getAll(): Promise<EquipmentUnit[]> {
    const response = await httpClient.get<EquipmentUnit[]>(RESOURCE_PATH)
    return response.data
  },

  async getById(id: number): Promise<EquipmentUnit> {
    const response = await httpClient.get<EquipmentUnit>(`${RESOURCE_PATH}/${id}`)
    return response.data
  },

  async create(payload: EquipmentUnitPayload): Promise<EquipmentUnit> {
    const response = await httpClient.post<EquipmentUnit>(RESOURCE_PATH, payload)
    return response.data
  },

  async update(id: number, payload: EquipmentUnitPayload): Promise<EquipmentUnit> {
    const response = await httpClient.put<EquipmentUnit>(`${RESOURCE_PATH}/${id}`, payload)
    return response.data
  },

  async remove(id: number): Promise<void> {
    await httpClient.delete(`${RESOURCE_PATH}/${id}`)
  },
}
