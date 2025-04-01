package ru.itport.sportinventory.view.category

import com.vaadin.flow.router.Route
import io.jmix.flowui.view.*
import ru.itport.sportinventory.entity.Category
import ru.itport.sportinventory.view.main.MainView


@Route(value = "categories", layout = MainView::class)
@ViewController(id = "Category.list")
@ViewDescriptor(path = "category-list-view.xml")
@LookupComponent("categoriesDataGrid")
@DialogMode(width = "64em")
class CategoryListView : StandardListView<Category>() {
}