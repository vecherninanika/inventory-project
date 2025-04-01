package ru.itport.sportinventory.entity

import io.jmix.core.metamodel.datatype.EnumClass

enum class Status(private val id: String) : EnumClass<String> {
    AVAILABLE("A"),
    BOOKED("B");

    override fun getId() = id

    companion object {

        @JvmStatic
        fun fromId(id: String): Status? = Status.values().find { it.id == id }
    }
}
