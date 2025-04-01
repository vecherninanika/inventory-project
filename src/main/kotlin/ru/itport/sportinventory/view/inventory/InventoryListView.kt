package ru.itport.sportinventory.view.inventory

import com.vaadin.flow.router.Route
import io.jmix.flowui.view.*
import ru.itport.sportinventory.entity.Inventory
import ru.itport.sportinventory.view.main.MainView


@Route(value = "inventories", layout = MainView::class)
@ViewController(id = "Inventory.list")
@ViewDescriptor(path = "inventory-list-view.xml")
@LookupComponent("inventoriesDataGrid")
@DialogMode(width = "64em")
class InventoryListView : StandardListView<Inventory>() {
}