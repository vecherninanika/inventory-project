package ru.itport.sportinventory.services

import io.jmix.core.DataManager
import io.jmix.core.FileRef
import io.jmix.core.security.CurrentAuthentication
import io.jmix.core.event.EntityChangedEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itport.sportinventory.entity.*
import java.util.*

@Service
open class FeedbackService @Autowired constructor(
    private val dataManager: DataManager,
    private val telegramService: TelegramService,
    private val currentAuthentication: CurrentAuthentication
) {

    @Transactional
    open fun reportBrokenEquipment(inventoryId: UUID, description: String, photo: FileRef?): BrokenEquipmentReport {
        val inventory = dataManager.load(Inventory::class.java)
            .id(inventoryId)
            .one()

        val user = currentAuthentication.user as? User ?: throw IllegalStateException("Пользователь не найден.")

        val report = dataManager.create(BrokenEquipmentReport::class.java).apply {
            this.inventory = inventory
            this.user = user
            this.description = description
            this.photo = photo
            this.status = ReportStatus.PENDING
            this.createdDate = Date()
        }

        dataManager.save(report)

        val message = "Здравствуйте, ${user.username}! Ваша заявка на поломку '${inventory.name}' зарегистрирована. Мы уведомим Вас о статусе рассмотрения."
        user.telegramId?.let { telegramService.sendMessage(it, message) }

        return report
    }

    @EventListener
    open fun onReportChanged(event: EntityChangedEvent<BrokenEquipmentReport>) {
        if (event.type == EntityChangedEvent.Type.UPDATED) {
            val reportId = event.entityId.getValue()
            val report = dataManager.load(BrokenEquipmentReport::class.java).id(reportId).one()

            val statusDisplayName = report.status?.let { ReportStatus.getDisplayName(it) }

            val message = "Здравствуйте, ${report.user?.username}! Статус вашей заявки на '${report.inventory?.name}' изменен на '$statusDisplayName'."
            report.user?.telegramId?.let { telegramService.sendMessage(it, message) }
        }
    }
}
