package ru.itport.sportinventory.models

import java.util.*

data class BookingJournalDTO(
    val inventoryId: UUID?,
    val inventoryName: String?,
    val inventoryPhoto: String,
    val startDate: Date?,
    val endDate: Date?
)