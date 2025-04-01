package ru.itport.sportinventory.view.category

import com.vaadin.flow.router.Route
import io.jmix.flowui.view.EditedEntityContainer
import io.jmix.flowui.view.StandardDetailView
import io.jmix.flowui.view.ViewController
import io.jmix.flowui.view.ViewDescriptor
import ru.itport.sportinventory.entity.Category
import ru.itport.sportinventory.view.main.MainView

@Route(value = "categories/:id", layout = MainView::class)
@ViewController(id = "Category.detail")
@ViewDescriptor(path = "category-detail-view.xml")
@EditedEntityContainer("categoryDc")
class CategoryDetailView : StandardDetailView<Category>() {
}