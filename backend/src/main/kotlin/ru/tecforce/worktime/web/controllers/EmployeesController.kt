package ru.tecforce.worktime.web.controllers

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.tecforce.worktime.persistance.entities.Employee
import ru.tecforce.worktime.services.EmployeeService

@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
@RequestMapping("/api/users")
class EmployeesController(private val employeeService: EmployeeService) {

    @RequestMapping("/")
    fun users(): MutableIterable<Employee> = employeeService.findAll()

    @RequestMapping("/{id}/")
    fun user(@PathVariable id: Long): Employee = employeeService.findOne(id)

    @RequestMapping("/{id}/toggleNotify")
    fun userToggleNotify(@PathVariable id: Long): Employee = employeeService.userToggleNotify(id)

}