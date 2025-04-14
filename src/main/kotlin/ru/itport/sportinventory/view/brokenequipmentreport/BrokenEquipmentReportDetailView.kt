package ru.itport.sportinventory.view.brokenequipmentreport

import com.vaadin.flow.router.Route
import io.jmix.flowui.view.EditedEntityContainer
import io.jmix.flowui.view.StandardDetailView
import io.jmix.flowui.view.ViewController
import io.jmix.flowui.view.ViewDescriptor
import ru.itport.sportinventory.entity.BrokenEquipmentReport
import ru.itport.sportinventory.view.main.MainView

@Route(value = "broken-equipment-reports/:id", layout = MainView::class)
@ViewController(id = "BrokenEquipmentReport.detail")
@ViewDescriptor(path = "broken-equipment-report-detail-view.xml")
@EditedEntityContainer("brokenEquipmentReportDc")
class BrokenEquipmentReportDetailView : StandardDetailView<BrokenEquipmentReport>() {
}