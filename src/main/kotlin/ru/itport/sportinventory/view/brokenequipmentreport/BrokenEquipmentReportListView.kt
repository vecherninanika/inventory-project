package ru.itport.sportinventory.view.brokenequipmentreport

import com.vaadin.flow.router.Route
import io.jmix.flowui.view.*
import ru.itport.sportinventory.entity.BrokenEquipmentReport
import ru.itport.sportinventory.view.main.MainView


@Route(value = "broken-equipment-reports", layout = MainView::class)
@ViewController(id = "BrokenEquipmentReport.list")
@ViewDescriptor(path = "broken-equipment-report-list-view.xml")
@LookupComponent("brokenEquipmentReportsDataGrid")
@DialogMode(width = "64em")
class BrokenEquipmentReportListView : StandardListView<BrokenEquipmentReport>() {
}