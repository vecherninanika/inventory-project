package ru.itport.sportinventory.view.inventory

import com.vaadin.flow.router.Route
import io.jmix.flowui.view.EditedEntityContainer
import io.jmix.flowui.view.StandardDetailView
import io.jmix.flowui.view.ViewController
import io.jmix.flowui.view.ViewDescriptor
import ru.itport.sportinventory.entity.Inventory
import ru.itport.sportinventory.view.main.MainView

@Route(value = "inventories/:id", layout = MainView::class)
@ViewController(id = "Inventory.detail")
@ViewDescriptor(path = "inventory-detail-view.xml")
@EditedEntityContainer("inventoryDc")
class InventoryDetailView : StandardDetailView<Inventory>() {
}