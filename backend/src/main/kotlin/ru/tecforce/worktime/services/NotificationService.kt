package ru.tecforce.worktime.services

import kotlinx.coroutines.experimental.launch
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.tecforce.worktime.clients.RedmineClient
import ru.tecforce.worktime.clients.RocketChatClient
import ru.tecforce.worktime.persistance.entities.Employee
import java.text.SimpleDateFormat
import java.util.*

@Service
class NotificationService(
        private val employeeService: EmployeeService,
        private val redmineClient: RedmineClient,
        private val rocketChatClient: RocketChatClient) {

    @Scheduled(cron = "0 0 17 * * MON-FRI")
    fun notifyUsers() {
        employeeService.findAll().forEach { user ->
            val logged = redmineClient.fetchWorklogs(user.redmineId, Date()).map { it.hours }.sum()
            if (user.notificationEnable.and(logged < user.requiredTimeToLog)) {
                launch { notify(user, logged) }
            }
        }
    }

    private suspend fun notify(user: Employee, logged: Double) {
        rocketChatClient.sendMessage(user, ":alarm_clock: " +
                "На `${Date().toString("dd.MM.YYYY")}` у пользователя `${user.username}` " +
                "списано недостаточное колличество времени! " +
                "Списано: `$logged` из `${user.requiredTimeToLog}`"
        )
    }

    fun Date.toString(pattern: String): String {
        return SimpleDateFormat(pattern).format(this)
    }

}