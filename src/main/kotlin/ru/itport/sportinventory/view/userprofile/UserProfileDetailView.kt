package ru.itport.sportinventory.view.userprofile

import com.vaadin.flow.router.Route
import io.jmix.flowui.view.EditedEntityContainer
import io.jmix.flowui.view.StandardDetailView
import io.jmix.flowui.view.ViewController
import io.jmix.flowui.view.ViewDescriptor
import ru.itport.sportinventory.entity.UserProfile
import ru.itport.sportinventory.view.main.MainView

@Route(value = "user-profiles/:id", layout = MainView::class)
@ViewController(id = "UserProfile.detail")
@ViewDescriptor(path = "user-profile-detail-view.xml")
@EditedEntityContainer("userProfileDc")
class UserProfileDetailView : StandardDetailView<UserProfile>() {
}