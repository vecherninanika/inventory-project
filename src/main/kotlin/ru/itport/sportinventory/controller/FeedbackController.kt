package ru.itport.sportinventory.controller

import io.jmix.core.security.CurrentAuthentication
import org.springframework.web.bind.annotation.*
import ru.itport.sportinventory.entity.BrokenEquipmentReport
import ru.itport.sportinventory.entity.ReportStatus
import ru.itport.sportinventory.models.BaseResponse
import ru.itport.sportinventory.models.feedback.FeedbackRequest
import ru.itport.sportinventory.services.FeedbackService
import ru.itport.sportinventory.utils.ControllerUtils.Companion.serviceCall
import java.util.*

@RestController
@CrossOrigin
@RequestMapping("custom/api/v1/feedback")
class FeedbackController(
    private val feedbackService: FeedbackService,
    private val currentAuthentication: CurrentAuthentication
) {

    @PostMapping("/report")
    fun reportBrokenEquipment(
        @RequestBody request: FeedbackRequest
    ): BaseResponse<String> {
        val user = currentAuthentication.user ?: throw IllegalStateException("Пользователь не найден.")
        return serviceCall {
            feedbackService.reportBrokenEquipment(request.inventoryId, request.description, request.photo)
            "Заявка на поломку успешно создана!"
        }
    }
}
