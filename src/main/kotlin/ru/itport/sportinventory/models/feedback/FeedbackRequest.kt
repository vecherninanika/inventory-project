package ru.itport.sportinventory.models.feedback

import io.jmix.core.FileRef
import java.util.*

data class FeedbackRequest(
    val inventoryId: UUID,
    val description: String,
    val photo: FileRef
)
