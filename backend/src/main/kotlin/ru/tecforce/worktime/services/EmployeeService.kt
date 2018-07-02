package ru.tecforce.worktime.services

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.tecforce.worktime.clients.RedmineClient
import ru.tecforce.worktime.exceptions.ConflictException
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
            employeeRepository.findByUsername(user.login).map { employee ->
                employeeRepository.save(employee.also {
                    it.firstName = user.firstname
                    it.lastName = user.lastname
                    it.username = user.login
                })
            }.orElseGet {
                employeeRepository.save(Employee(user))
            }
        }
    }

    fun findAll(): MutableList<Employee> = employeeRepository.findAll().toMutableList()
    fun findOne(id: Long): Employee = employeeRepository.findById(id).orElseThrow {
        NotFoundException("Employee with ID: $id not found!")
    }
    fun findOne(username: String): Employee = employeeRepository.findByUsername(username).orElseThrow {
        NotFoundException("Employee with username '$username' not found!")
    }

    fun toggleNotify(id: Long): Employee = employeeRepository.save(findOne(id).also {
        it.notificationEnable = !it.notificationEnable
    })

    fun setRequiredTimeToLog(id: Long, time: Double): Employee {
        if(time in 0.0..8.0) return employeeRepository.save(findOne(id).also {
            it.requiredTimeToLog = time
        })
        else throw ConflictException("Required time should have bean more then 0 and less 8 hours for day")
    }

}