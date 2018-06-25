package ru.tecforce.worktime.persistance.repositories

import org.springframework.stereotype.Component
import ru.tecforce.worktime.clients.RedmineClient

@Component
class UserRepository(val redmineClient: RedmineClient) {
    fun findAll() = redmineClient.fetchUsers()
}