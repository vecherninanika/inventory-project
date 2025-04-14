package ru.itport.sportinventory.view.userprofile

import com.vaadin.flow.router.Route
import io.jmix.flowui.view.*
import ru.itport.sportinventory.entity.UserProfile
import ru.itport.sportinventory.view.main.MainView


@Route(value = "user-profiles", layout = MainView::class)
@ViewController(id = "UserProfile.list")
@ViewDescriptor(path = "user-profile-list-view.xml")
@LookupComponent("userProfilesDataGrid")
@DialogMode(width = "64em")
class UserProfileListView : StandardListView<UserProfile>() {
}