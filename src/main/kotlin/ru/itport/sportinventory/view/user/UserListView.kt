package ru.itport.sportinventory.view.user

import com.vaadin.flow.router.Route
import io.jmix.flowui.view.*
import ru.itport.sportinventory.entity.User
import ru.itport.sportinventory.view.main.MainView


@Route(value = "users", layout = MainView::class)
@ViewController(id = "User.list")
@ViewDescriptor(path = "user-list-view.xml")
@LookupComponent("usersDataGrid")
@DialogMode(width = "64em")
class UserListView : StandardListView<User>() {
}