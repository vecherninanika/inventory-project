package ru.itport.sportinventory.services

import io.jmix.core.DataManager
import io.jmix.core.querycondition.PropertyCondition
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.itport.sportinventory.entity.TelegramChat
import ru.itport.sportinventory.entity.User
import ru.itport.sportinventory.entity.UserProfile
import ru.itport.sportinventory.models.auth.TelegramAuthRq
import ru.itport.sportinventory.security.DatabaseUserRepository

@Service
class UserService @Autowired constructor(
    private val dataManager: DataManager,
    private val telegramService: TelegramService,
    private val databaseUserRepository: DatabaseUserRepository
) {

    fun createUser(telegramAuthRq: TelegramAuthRq, groupId: String): User {

        val userProfile = dataManager.create(UserProfile::class.java)
        userProfile.telegramId = telegramAuthRq.telegramId

        val user = dataManager.create(User::class.java)
        user.username = telegramAuthRq.username
        user.firstName = telegramAuthRq.firstName
        user.lastName = telegramAuthRq.lastName

        val createdUser = dataManager.save(user)
        userProfile.user = createdUser
        dataManager.save(userProfile)

        return createdUser
    }

    fun checkMembership(telegramId: String): String? {
        return dataManager.load(TelegramChat::class.java)
            .all()
            .list()
            .firstOrNull{ telegramService.userInChat(it.chatID, telegramId) }?.chatID
    }

    fun loadUserByUsername(username: String): User? {
        return dataManager.load(User::class.java)
            .query("select u from User u where u.username = :username")
            .parameter("username", username)
            .optional()
            .orElse(null)
    }

    fun loadByTelegramId(telegramId: String): User? {
        return dataManager.load(User::class.java).condition(
            PropertyCondition.equal("userProfile.telegramId", telegramId)
        ).optional()
            .orElse(null)
    }
}