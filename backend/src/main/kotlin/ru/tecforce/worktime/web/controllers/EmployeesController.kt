package ru.tecforce.worktime.web.controllers

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.tecforce.worktime.persistance.entities.Employee
import ru.tecforce.worktime.services.EmployeeService

@RestController
@RequestMapping("/api/users")
class EmployeesController(private val employeeService: EmployeeService) {

    @RequestMapping("/")
    fun users(): MutableIterable<Employee> = employeeService.findAll()

}