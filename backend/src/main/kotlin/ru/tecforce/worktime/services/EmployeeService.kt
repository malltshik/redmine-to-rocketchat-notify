package ru.tecforce.worktime.services

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.tecforce.worktime.clients.RedmineClient
import ru.tecforce.worktime.persistance.entities.Employee
import ru.tecforce.worktime.persistance.repositories.EmployeeRepository

@Service
class EmployeeService(
        private val employeeRepository: EmployeeRepository,
        private val redmineClient: RedmineClient) {

    /**
     * Update redmine users every hours
     */
    @Scheduled(fixedDelay = 1000 * 3600)
    fun syncRedmineUsers() {
        redmineClient.fetchUsers().forEach { user ->
            val emp = employeeRepository.findByUsername(user.login)
            employeeRepository.save(Employee(emp?.id, user.id, user.login, user.firstname, user.lastname))
        }
    }

    fun findAll(): MutableIterable<Employee> = employeeRepository.findAll()

}