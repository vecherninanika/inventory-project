package ru.itport.sportinventory.services

import io.jmix.core.DataManager
import io.jmix.core.FileRef
import io.jmix.core.querycondition.PropertyCondition
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itport.sportinventory.entity.*
import ru.itport.sportinventory.exception.ErrorDescriptor
import ru.itport.sportinventory.exception.PlatformException
import ru.itport.sportinventory.mapper.mapToString
import ru.itport.sportinventory.models.BookingJournalDTO
import ru.itport.sportinventory.models.CategoryDTO
import ru.itport.sportinventory.models.InventoryDTO
import java.text.SimpleDateFormat
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
            .optional()
            .orElseThrow { PlatformException(ErrorDescriptor.INVENTORY_NOT_FOUND) }

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

    open fun getInventoryByUser(telegramId: String): List<BookingJournalDTO> {
        val user = dataManager.load(UserProfile::class.java).condition(
            PropertyCondition.equal("telegramId", telegramId)
                ).optional()
                .orElseThrow { PlatformException(ErrorDescriptor.USER_NOT_FOUND) }
                .user

        val reservations = dataManager.load(BookingJournal::class.java)
            .query("select b from BookingJournal b where b.status = :status and b.user.id = :userId order by b.endDate desc")
            .parameter("status", Status.ACTIVE)
            .parameter("userId", user?.id ?: 0)
            .list()

        return reservations.map { reservation ->
            BookingJournalDTO(
                reservation.inventory?.id,
                reservation.inventory?.name,
                reservation.inventory?.photo.toString(),
                reservation.startDate,
                reservation.endDate
            )
        }
    }

    @Transactional
    open fun makeReservation(inventoryId: UUID, startDate: Date, endDate: Date, userDetails: UserDetails): BookingJournal {
        // Проверка, что дата начала бронирования не в прошлом - по какой-то причине не работает
//        val today = Date()
//        if (startDate.before(today)) {
//            throw PlatformException(ErrorDescriptor.DATE_IN_PAST)
//        }

        // Проверка, что дата окончания бронирования после даты начала
        if (endDate.before(startDate)) {
            throw PlatformException(ErrorDescriptor.END_BEFORE_START)
        }
        // Загружаем объект инвентаря по переданному ID
        val inventory = dataManager.load(Inventory::class.java)
            .id(inventoryId)
            .optional()
            .orElseThrow { PlatformException(ErrorDescriptor.INVENTORY_NOT_FOUND) }

        // Проверяем, есть ли доступный инвентарь для бронирования
        if (inventory.quantityFree == null || inventory.quantityFree!! <= 0) {
            throw PlatformException(ErrorDescriptor.NO_AVAILABLE_INVENTORY)
        }

        // Загружаем пользователя по данным аутентификации
        val user = dataManager.load(User::class.java)
            .condition(PropertyCondition.equal("username", userDetails.username))
            .optional()
            .orElseThrow { PlatformException(ErrorDescriptor.USER_NOT_FOUND) }

        // Проверяем, есть ли у пользователя активное бронирование этого инвентаря
        val lastReservation = dataManager.load(BookingJournal::class.java)
            .query("select b from BookingJournal b where b.inventory.id = :inventoryId and b.user.id = :userId order by b.endDate desc")
            .parameter("inventoryId", inventory.id)
            .parameter("userId", user.id)
            .list()
            .firstOrNull()

        // Если есть активное бронирование, выбрасываем исключение
        if (lastReservation != null && lastReservation.endDate?.after(startDate) == true) {
            throw PlatformException(ErrorDescriptor.USER_ALREADY_BOOKED)
        }

        // Создаем новый журнал бронирования
        val bookingJournal = dataManager.create(BookingJournal::class.java)
        bookingJournal.user = user
        bookingJournal.inventory = inventory
        bookingJournal.startDate = startDate
        bookingJournal.endDate = endDate
        bookingJournal.setStatus(Status.ACTIVE)

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
            throw PlatformException(ErrorDescriptor.BOOKING_ERROR)
        }
    }

    @Transactional
    open fun returnInventory(inventoryId: UUID, returnDate: Date, photo: FileRef): BookingJournal {
        val inventory = dataManager.load(Inventory::class.java).condition(
            PropertyCondition.equal("id", inventoryId))
            .optional()
            .orElseThrow { PlatformException(ErrorDescriptor.INVENTORY_NOT_FOUND) }

        inventory.quantityFree = inventory.quantityFree?.plus(1)
        dataManager.save(inventory)

        val reservation = dataManager.load(BookingJournal::class.java)
            .query("select b from BookingJournal b where b.inventory.id = :inventoryId and b.status = :statusId order by b.endDate desc")
            .parameter("inventoryId", inventory.id)
            .parameter("statusId", Status.ACTIVE.id)
            .list()
            .firstOrNull()

        if (reservation == null) {
            throw PlatformException(ErrorDescriptor.NO_ACTIVE_RESERVATION)
        }

        reservation.endDate = returnDate
        reservation.photo = photo
        reservation.setStatus(Status.ENDED)
        dataManager.save(reservation)

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
        val formattedReturnDate = dateFormat.format(returnDate)

        val message = "Здравствуйте, ${reservation.user?.username}! Ваше бронирование инвентаря ${inventory.name} завершено. Дата возврата: $formattedReturnDate."
        reservation.user?.telegramId?.let { telegramService.sendMessage(it, message) }

        return reservation
    }
}