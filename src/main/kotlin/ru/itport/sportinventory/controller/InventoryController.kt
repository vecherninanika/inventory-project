package ru.itport.sportinventory.controller

import io.jmix.core.FileRef
import io.jmix.core.security.CurrentAuthentication
import org.springframework.web.bind.annotation.*
import ru.itport.sportinventory.models.BaseResponse
import ru.itport.sportinventory.models.InventoryDTO
import ru.itport.sportinventory.services.InventoryService
import java.util.*
import org.springframework.format.annotation.DateTimeFormat
import ru.itport.sportinventory.models.inventory.ReserveRq
import ru.itport.sportinventory.models.inventory.ReturnRq
import ru.itport.sportinventory.utils.ControllerUtils.Companion.serviceCall

@RestController
@CrossOrigin
@RequestMapping("custom/api/v1/inventory")
class InventoryController(
    private val inventoryService: InventoryService,
    private val currentAuthentication: CurrentAuthentication
) {

    @GetMapping("/all")
    fun getAllInventory(): BaseResponse<List<InventoryDTO>> {
        return serviceCall { inventoryService.getAllInventory() }
    }

    @GetMapping("/{id}")
    fun getInventoryById(@PathVariable id: UUID): BaseResponse<InventoryDTO> {
        return serviceCall { inventoryService.getInventoryById(id) }
    }

    @GetMapping("/category/{category}")
    fun getInventoryByCategory(@PathVariable category: String): BaseResponse<List<InventoryDTO>> {
        return serviceCall { inventoryService.getInventoryByCategory(category) }
    }

    @PostMapping("/make-reservation")
    fun reserveInventory(
        @RequestBody inventory: ReserveRq,

        ): BaseResponse<String> {
        val user = currentAuthentication.user
        return serviceCall {
            inventoryService.makeReservation(inventory.inventoryId, inventory.startDate, inventory.endDate, user)
            "Бронирование успешно!"
        }
    }

    @PostMapping("/return")
    fun returnInventory(
        @RequestBody inventory: ReturnRq,
    ): BaseResponse<String> {
        return serviceCall {
            inventoryService.returnInventory(inventory.inventoryId, inventory.returnDate, inventory.photo)
            "Инвентарь успешно возвращен!"
        }
    }

}