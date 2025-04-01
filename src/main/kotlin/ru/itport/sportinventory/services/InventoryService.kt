package ru.itport.sportinventory.services

import io.jmix.core.DataManager
import io.jmix.core.FileRef
import io.jmix.core.Sort
import io.jmix.core.querycondition.PropertyCondition
import io.jmix.core.security.CurrentAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
    import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itport.sportinventory.entity.*
import ru.itport.sportinventory.mapper.mapToString
import ru.itport.sportinventory.models.CategoryDTO
import ru.itport.sportinventory.models.InventoryDTO
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


@Service
open class InventoryService @Autowired constructor(
    private val dataManager: DataManager,
    private val telegramService: TelegramService,
    @Value("\${application.domain}") private val domain: String
) {

    open fun getAllInventory(): List<InventoryDTO> {
        val inventories = dataManager.load(Inventory::class.java)
            .all()
            .list()

        return inventories.map { inventory ->
            InventoryDTO(
                inventory.id!!,
                inventory.name!!,
                inventory.category?.id?.let { inventory.category?.name?.let { it1 -> CategoryDTO(it, it1) } },
                inventory.description,
                inventory.photo?.mapToString(domain) ?: "",
                inventory.maxReservationPeriod,
                inventory.quantity,
                inventory.quantityFree
            )
        }
    }

    open fun getInventoryByCategory(category: String): List<InventoryDTO> {
        val inventories = dataManager.load(Inventory::class.java).condition(
            PropertyCondition.equal("category.name", category)
        ).list()

        return inventories.map { inventory ->
            InventoryDTO(
                inventory.id!!,
                inventory.name!!,
                inventory.category?.id?.let { inventory.category?.name?.let { it1 -> CategoryDTO(it, it1) } },
                inventory.description,
                inventory.photo?.mapToString(domain) ?: "",
                inventory.maxReservationPeriod,
                inventory.quantity,
                inventory.quantityFree
            )
        }
    }

    open fun getInventoryById(id: UUID): InventoryDTO {
        val inventory = dataManager.load(Inventory::class.java)
            .id(id)
            .one()

        return InventoryDTO(
            inventory.id!!,
            inventory.name!!,
            inventory.category?.id?.let { inventory.category?.name?.let { it1 -> CategoryDTO(it, it1) } },
            inventory.description,
            inventory.photo?.mapToString(domain) ?: "",
            inventory.maxReservationPeriod,
            inventory.quantity,
            inventory.quantityFree
        )
    }

    @Transactional
    open fun makeReservation(inventoryId: UUID, startDate: Date, endDate: Date, user: UserDetails): BookingJournal {
        val today = Date()
        // Проверка, что дата начала бронирования не в прошлом
        if (startDate.before(today)) {
            throw IllegalArgumentException("Дата начала бронирования не может быть в прошлом.")
        }
        // Проверка, что дата окончания бронирования после даты начала
        if (endDate.before(startDate)) {
            throw IllegalArgumentException("Дата окончания бронирования не может быть раньше даты начала.")
        }
        // Загружаем объект инвентаря по переданному ID
        val inventory = dataManager.load(Inventory::class.java)
            .id(inventoryId)
            .one()

        // Проверяем, есть ли доступный инвентарь для бронирования
        if (inventory.quantityFree == null || inventory.quantityFree!! <= 0) {
            throw IllegalStateException("Нет доступного инвентаря для бронирования.")
        }

        // Загружаем пользователя по данным аутентификации
        val user = dataManager.load(User::class.java)
            .condition(PropertyCondition.equal("username", user.username))
            .optional()
            .orElseThrow { IllegalStateException("Пользователь не найден.") }

        // Проверяем, есть ли у пользователя активное бронирование этого инвентаря
        val lastReservation = dataManager.load(BookingJournal::class.java)
            .query("select b from BookingJournal b where b.inventory.id = :inventoryId and b.user.id = :userId order by b.endDate desc")
            .parameter("inventoryId", inventory.id)
            .parameter("userId", user.id)
            .list()
            .firstOrNull()

        // Если есть активное бронирование, выбрасываем исключение
        if (lastReservation != null && lastReservation.endDate?.after(startDate) == true) {
            throw IllegalStateException("У вас уже есть активное бронирование этого инвентаря.")
        }

        // Создаем новый журнал бронирования
        val bookingJournal = dataManager.create(BookingJournal::class.java)
        bookingJournal.user = user
        bookingJournal.inventory = inventory
        bookingJournal.startDate = startDate
        bookingJournal.endDate = endDate

        // Уменьшаем количество свободных единиц инвентаря
        inventory.quantityFree = inventory.quantityFree!! - 1

        // Сохраняем изменения в базе данных
        return try {
            dataManager.save(inventory, bookingJournal)
            val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))

            val formattedStartDate = dateFormat.format(startDate)
            val formattedEndDate = dateFormat.format(endDate)

            val message = "Здравствуйте, ${user.username}! Ваше бронирование инвентаря ${inventory.name} успешно выполнено с $formattedStartDate по $formattedEndDate."
            if (user.telegramId.isNullOrBlank()) {
                println("Ошибка: Не найден Telegram ID для пользователя ${user.username}. Сообщение не отправлено.")
            } else {
                telegramService.sendMessage(user.telegramId!!, message)
            }

            bookingJournal
        } catch (e: Exception) {
            throw IllegalStateException("Ошибка при бронировании: ${e.message}")
        }
    }

    @Transactional
    open fun returnInventory(inventoryId: UUID, returnDate: Date, photo: FileRef): BookingJournal {
        val inventoryItem = dataManager.load(Inventory::class.java).condition(
            PropertyCondition.equal("id", inventoryId))
            .one()

        inventoryItem.quantityFree = inventoryItem.quantityFree?.plus(1)
        dataManager.save(inventoryItem)

        val reservation = dataManager.load(BookingJournal::class.java).condition(
            PropertyCondition.equal("inventory", inventoryItem))
            .sort(Sort.by(Sort.Direction.DESC, "endDate"))
            .list().first()

        reservation.endDate = returnDate
        reservation.photo = photo
        dataManager.save(reservation)

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
        val formattedReturnDate = dateFormat.format(returnDate)

        val message = "Здравствуйте, ${reservation.user?.username}! Ваше бронирование инвентаря ${inventoryItem.name} завершено. Дата возврата: $formattedReturnDate."
        reservation.user?.telegramId?.let { telegramService.sendMessage(it, message) }

        return reservation
    }
}
