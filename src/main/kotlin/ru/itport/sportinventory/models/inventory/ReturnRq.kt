package ru.itport.sportinventory.models.inventory

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.jmix.core.FileRef
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReturnRq(
    @JsonProperty("inventoryId")
    val inventoryId: UUID,

    @JsonProperty("returnDate")
    val returnDate: Date,

    @JsonProperty("photo")
    val photo: FileRef,
)