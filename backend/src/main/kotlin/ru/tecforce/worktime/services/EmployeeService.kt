package ru.tecforce.worktime.services

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.tecforce.worktime.clients.RedmineClient
import ru.tecforce.worktime.exceptions.NotFoundException
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
            if (emp == null) employeeRepository.save(Employee(user))
            else employeeRepository.save(emp.also {
                it.firstName = user.firstname
                it.lastName = user.lastname
                it.username = user.login
            })
        }
    }

    fun findAll(): MutableIterable<Employee> = employeeRepository.findAll()
    fun findOne(id: Long): Employee = employeeRepository.findById(id).orElseThrow {
        NotFoundException("Employee with ID: $id not found!")
    }

    fun userToggleNotify(id: Long): Employee = employeeRepository.save(findOne(id).also {
        it.notificationEnable = !it.notificationEnable
    })

}