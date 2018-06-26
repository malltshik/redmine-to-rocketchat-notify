package ru.tecforce.worktime.services

import kotlinx.coroutines.experimental.launch
import org.springframework.stereotype.Service
import ru.tecforce.worktime.clients.RedmineClient
import ru.tecforce.worktime.clients.RedmineUser
import ru.tecforce.worktime.clients.RocketChatClient
import java.util.*

@Service
class NotificationService(private val redmineClient: RedmineClient, private val rocketChatClient: RocketChatClient) {

    // @Scheduled(fixedDelay = 1000000)
    // @Scheduled(cron = "0 0 17 * * MON-FRI")
    fun notifyUsers() {
        redmineClient.fetchUsers().forEach { user ->
            if (redmineClient.fetchWorklogs(user.id, Date()).map { it.hours }.sum() < 8.0)
                launch { notify(user) }
        }
    }

    private suspend fun notify(user: RedmineUser) {
        rocketChatClient.sendMessage(
                user,
                "На ${Date()} у пользователя ${user.login} списано недостаточное колличество времени :fun:"
        )
    }

}