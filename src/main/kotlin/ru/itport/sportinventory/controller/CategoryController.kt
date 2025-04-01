package ru.itport.sportinventory.controller

import org.springframework.web.bind.annotation.*
import ru.itport.sportinventory.models.BaseResponse
import ru.itport.sportinventory.models.CategoryDTO
import ru.itport.sportinventory.services.CategoryService
import ru.itport.sportinventory.utils.ControllerUtils.Companion.serviceCall
import java.util.*

@RestController
@CrossOrigin
@RequestMapping("custom/api/v1/category")
class CategoryController(
    val categoryService: CategoryService
) {

    @GetMapping("/all")
    fun getAllCategories(): BaseResponse<List<CategoryDTO>> {
        return serviceCall { categoryService.getAllCategories() }
    }

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: UUID): BaseResponse<CategoryDTO> {
        return serviceCall { categoryService.getCategoryById(id) }
    }
}
