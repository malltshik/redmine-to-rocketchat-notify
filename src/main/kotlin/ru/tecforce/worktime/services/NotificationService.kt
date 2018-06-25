package ru.tecforce.worktime.services

import kotlinx.coroutines.experimental.launch
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.tecforce.worktime.clients.RedmineClient
import ru.tecforce.worktime.clients.RocketChatClient
import ru.tecforce.worktime.persistance.entities.User
import ru.tecforce.worktime.persistance.repositories.UserRepository
import java.util.*

@Service
class NotificationService(
        private val userRepository: UserRepository,
        private val redmineClient: RedmineClient,
        private val rocketChatClient: RocketChatClient
) {

    @Scheduled(fixedDelay = 1000000)
    // @Scheduled(cron = "0 0 17 * * MON-FRI")
    fun notifyUsers() {
        userRepository.findAll().forEach { user ->
            if (redmineClient.fetchWorklogs(user.id, Date()).map { it.hours }.sum() < 8.0)
                launch { notify(user) }
        }
    }

    private suspend fun notify(user: User) {
        rocketChatClient.sendMessage(
                user,
                "На ${Date()} у пользователя ${user.login} списано недостаточное колличество времени :fun:"
        )
    }

}