package ru.itport.sportinventory.models

import io.jmix.core.FileRef
import java.util.*

data class InventoryDTO(
    val id: UUID,
    val name: String,
    val category: CategoryDTO?,
    val description: String?,
    val photo: String?,
    val maxReservationPeriod: Int?,
    val quantity: Int?,
    val quantityFree: Int?
)