package ru.tecforce.worktime

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*

@Service
class NotificationService(val redmineClient: RedmineClient) {

    @Scheduled(fixedRate = 20000)
    fun notifyUsers() {
        println("notifyUsers started")
        redmineClient.fetchUsers().forEach { user ->
            if(redmineClient.fetchWorklogs(user.id, Date()).map { it.hours }.sum() < 8.0)
                println("${user.login} PIDAR! NE SPISAL!")
        }
    }

}
