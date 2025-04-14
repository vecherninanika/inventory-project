package ru.itport.sportinventory.entity

import io.jmix.core.metamodel.datatype.EnumClass

enum class ReportStatus(private val id: String, private val displayName: String) : EnumClass<String> {
    PENDING("P", "Ожидает обработки"),        // Ожидает обработки
    IN_PROGRESS("IP", "В работе"),           // В работе
    FIXED("F", "Ремонт завершен"),           // Ремонт завершен
    NOT_REPAIRABLE("NR", "Не подлежит ремонту"); // Не подлежит ремонту

    override fun getId(): String = id

    companion object {
        @JvmStatic
        fun fromId(id: String): ReportStatus? = entries.find { it.id == id }

        // Метод для получения отображаемого названия статуса на русском
        fun getDisplayName(status: ReportStatus): String {
            return status.displayName
        }
    }
}
