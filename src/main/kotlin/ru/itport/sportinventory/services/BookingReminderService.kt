package ru.itport.sportinventory.services

import io.jmix.core.DataManager
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.itport.sportinventory.entity.BookingJournal
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Service
class BookingReminderService(
    private val dataManager: DataManager,
    private val telegramService: TelegramService
) {
    @Scheduled(cron = "0 0 9 * * ?") // Каждый день в 9 утра
    fun sendReminders() {
        val tomorrow = LocalDate.now().plusDays(1)
        val tomorrowDate = Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant())

        val bookings = dataManager.load(BookingJournal::class.java)
            .query("select b from BookingJournal b where b.endDate = :endDate")
            .parameter("endDate", tomorrowDate)
            .list()

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))

        bookings.forEach { booking ->
            // Форматируем дату возврата
            val formattedReturnDate = dateFormat.format(booking.endDate)

            // Формируем сообщение для пользователя
            val message = "Здравствуйте, ${booking.user?.username}! Ваше бронирование инвентаря ${booking.inventory?.name} заканчивается завтра, ${formattedReturnDate}. Пожалуйста, верните инвентарь вовремя."

            // Проверка на наличие telegramId пользователя и отправка сообщения
            booking.user?.telegramId?.let {
                telegramService.sendMessage(it, message)
            }
        }
    }
}
