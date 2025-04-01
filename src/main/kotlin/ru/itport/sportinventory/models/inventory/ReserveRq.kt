package ru.itport.sportinventory.models.inventory

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReserveRq(
    @JsonProperty("inventoryId")
    val inventoryId: UUID,

    @JsonProperty("startDate")
    val startDate: Date,

    @JsonProperty("endDate")
    val endDate: Date,
)