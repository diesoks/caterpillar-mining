import { useState } from 'react'
import type { EquipmentUnit, EquipmentUnitPayload } from '../../../domain/equipmentUnit/EquipmentUnit'
import { useEquipmentUnits } from '../../../application/equipmentUnit/useEquipmentUnits'
import { extractErrorMessage } from '../../../shared/extractErrorMessage'
import { Button } from '../../components/Button'
import { Banner } from '../../components/Banner'
import { Spinner } from '../../components/Spinner'
import { Modal } from '../../components/Modal'
import { ConfirmDialog } from '../../components/ConfirmDialog'
import { EquipmentUnitTable } from './EquipmentUnitTable'
import { EquipmentUnitForm } from './EquipmentUnitForm'
import styles from './EquipmentUnitsPage.module.css'

// Page-level container: owns the equipment-units list (via useEquipmentUnits) plus the local UI
// state for the create/edit modal and the delete confirmation dialog, and wires them to the
// presentational EquipmentUnitTable / EquipmentUnitForm / ConfirmDialog components.
export function EquipmentUnitsPage() {
  const { equipmentUnits, isLoading, error, refetch, createEquipmentUnit, updateEquipmentUnit, deleteEquipmentUnit } =
    useEquipmentUnits()

  const [isFormOpen, setIsFormOpen] = useState(false)
  const [editingUnit, setEditingUnit] = useState<EquipmentUnit | null>(null)
  const [unitPendingDeletion, setUnitPendingDeletion] = useState<EquipmentUnit | null>(null)
  const [isDeleting, setIsDeleting] = useState(false)
  const [deleteError, setDeleteError] = useState<string | null>(null)

  const handleOpenCreateForm = () => {
    setEditingUnit(null)
    setIsFormOpen(true)
  }

  const handleOpenEditForm = (equipmentUnit: EquipmentUnit) => {
    setEditingUnit(equipmentUnit)
    setIsFormOpen(true)
  }

  const handleCloseForm = () => {
    setIsFormOpen(false)
    setEditingUnit(null)
  }

  const handleFormSubmit = async (payload: EquipmentUnitPayload) => {
    if (editingUnit) {
      await updateEquipmentUnit(editingUnit.id, payload)
    } else {
      await createEquipmentUnit(payload)
    }
    handleCloseForm()
  }

  const handleRequestDelete = (equipmentUnit: EquipmentUnit) => {
    setDeleteError(null)
    setUnitPendingDeletion(equipmentUnit)
  }

  const handleCancelDelete = () => {
    setUnitPendingDeletion(null)
  }

  const handleConfirmDelete = async () => {
    if (!unitPendingDeletion) return
    setIsDeleting(true)
    setDeleteError(null)
    try {
      await deleteEquipmentUnit(unitPendingDeletion.id)
      setUnitPendingDeletion(null)
    } catch (caughtError) {
      setDeleteError(extractErrorMessage(caughtError))
    } finally {
      setIsDeleting(false)
    }
  }

  return (
    <div className={styles.page}>
      <header className={styles.header}>
        <div>
          <h1 className={styles.title}>Caterpillar Mining - Equipment Units</h1>
          <p className={styles.subtitle}>Fleet registry for the mining bounded context</p>
        </div>
        <Button onClick={handleOpenCreateForm}>+ Register unit</Button>
      </header>

      {error && (
        <>
          <Banner type="error" message={error} />
          <Button variant="secondary" onClick={refetch}>
            Retry
          </Button>
        </>
      )}

      {isLoading ? (
        <Spinner />
      ) : (
        <EquipmentUnitTable
          equipmentUnits={equipmentUnits}
          onEdit={handleOpenEditForm}
          onDelete={handleRequestDelete}
        />
      )}

      <Modal
        isOpen={isFormOpen}
        title={editingUnit ? 'Edit equipment unit' : 'Register equipment unit'}
        onClose={handleCloseForm}
      >
        <EquipmentUnitForm
          key={editingUnit?.id ?? 'create'}
          editingUnit={editingUnit}
          onSubmit={handleFormSubmit}
          onCancel={handleCloseForm}
        />
      </Modal>

      <ConfirmDialog
        isOpen={unitPendingDeletion !== null}
        title="Delete equipment unit"
        message={
          unitPendingDeletion
            ? `Are you sure you want to delete "${unitPendingDeletion.model}" (${unitPendingDeletion.serialNumber})?`
            : ''
        }
        errorMessage={deleteError}
        isConfirming={isDeleting}
        onConfirm={handleConfirmDelete}
        onCancel={handleCancelDelete}
      />
    </div>
  )
}
