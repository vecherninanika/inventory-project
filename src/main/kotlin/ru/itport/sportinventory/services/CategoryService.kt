package ru.itport.sportinventory.services

import io.jmix.core.DataManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.itport.sportinventory.entity.Category
import ru.itport.sportinventory.exception.ErrorDescriptor
import ru.itport.sportinventory.exception.PlatformException
import ru.itport.sportinventory.models.CategoryDTO
import java.util.*

@Service
class CategoryService @Autowired constructor(
    private val dataManager: DataManager
) {

    fun getAllCategories(): List<CategoryDTO> {
        val categories = dataManager.load(Category::class.java)
            .all()
            .list()

        return categories.map { category ->
            CategoryDTO(category.id!!, category.name!!)
        }
    }

    fun getCategoryById(id: UUID): CategoryDTO {
        val category = dataManager.load(Category::class.java)
            .id(id)
            .one()

        return CategoryDTO(category.id!!, category.name!!)
    }
}
