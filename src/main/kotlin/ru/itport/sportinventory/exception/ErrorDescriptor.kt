package ru.itport.sportinventory.exception

enum class ErrorDescriptor(val code: String, val message: String) {
    INTERNAL_ERROR("SERVER_ERROR", "Возникла ошибка при выполнении запроса"),
    INVALID_CATEGORY_ID("INVALID_CATEGORY", "Категории не существует")
}
