package ru.itport.sportinventory.view.bookingjournal

import com.vaadin.flow.router.Route
import io.jmix.flowui.view.*
import ru.itport.sportinventory.entity.BookingJournal
import ru.itport.sportinventory.view.main.MainView


@Route(value = "booking-journals", layout = MainView::class)
@ViewController(id = "BookingJournal.list")
@ViewDescriptor(path = "booking-journal-list-view.xml")
@LookupComponent("bookingJournalsDataGrid")
@DialogMode(width = "64em")
class BookingJournalListView : StandardListView<BookingJournal>() {
}