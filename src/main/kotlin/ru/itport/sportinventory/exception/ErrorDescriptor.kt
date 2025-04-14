package ru.itport.sportinventory.exception

enum class ErrorDescriptor(val code: String, val message: String) {
    INTERNAL_ERROR("SERVER_ERROR", "Возникла ошибка при выполнении запроса"),
    END_BEFORE_START("END_LT_START", "Дата окончания бронирования не может быть раньше даты начала."),
    DATE_IN_PAST("DATE_IN_PAST", "Дата начала бронирования не может быть в прошлом."),
    INVENTORY_NOT_FOUND("INVENTORY_NOT_FOUND", "Инвентарь не найден."),
    NO_AVAILABLE_INVENTORY("NO_AVAILABLE_INVENTORY", "Нет доступного инвентаря для бронирования."),
    NO_ACTIVE_RESERVATION("NO_ACTIVE_RESERVATION", "У вас нет активной брони этого инвентаря."),
    USER_NOT_FOUND("USER_NOT_FOUND", "Пользователь не найден."),
    USER_ALREADY_BOOKED("USER_ALREADY_BOOKED", "У вас уже есть активное бронирование этого инвентаря."),
    BOOKING_ERROR("BOOKING_ERROR", "Возникла ошибка при создании бронирования."),
    CATEGORY_NOT_FOUND("CATEGORY_NOT_FOUND", "Категория не найдена.")
}

