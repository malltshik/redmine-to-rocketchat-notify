package ru.tecforce.worktime.services

import kotlinx.coroutines.experimental.launch
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.tecforce.worktime.clients.RedmineClient
import ru.tecforce.worktime.clients.RocketChatClient
import ru.tecforce.worktime.persistance.entities.Employee
import java.util.*

@Service
class NotificationService(
        private val employeeService: EmployeeService,
        private val redmineClient: RedmineClient,
        private val rocketChatClient: RocketChatClient) {

    @Scheduled(cron = "0 0 17 * * MON-FRI")
    fun notifyUsers() {
        employeeService.findAll().forEach { user ->
            if (checkConditions(user)) {
                launch { notify(user) }
            }
        }
    }

    private fun checkConditions(user: Employee): Boolean {
        return (redmineClient.fetchWorklogs(user.redmineId, Date()).map { it.hours }.sum() < 8.0)
                .and(user.notificationEnable)
    }

    private suspend fun notify(user: Employee) {
        rocketChatClient.sendMessage(user, "На ${Date()} у пользователя ${user.username} " +
                "списано недостаточное колличество времени"
        )
    }

}