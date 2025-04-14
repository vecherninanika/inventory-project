package ru.itport.sportinventory.view.user

import com.vaadin.flow.router.Route
import io.jmix.flowui.view.EditedEntityContainer
import io.jmix.flowui.view.StandardDetailView
import io.jmix.flowui.view.ViewController
import io.jmix.flowui.view.ViewDescriptor
import ru.itport.sportinventory.entity.User
import ru.itport.sportinventory.view.main.MainView

@Route(value = "users/:id", layout = MainView::class)
@ViewController(id = "User.detail")
@ViewDescriptor(path = "user-detail-view.xml")
@EditedEntityContainer("userDc")
class UserDetailView : StandardDetailView<User>() {
}