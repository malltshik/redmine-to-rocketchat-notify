package ru.tecforce.worktime.persistance.repositories

import jdk.nashorn.internal.ir.annotations.Immutable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.tecforce.worktime.persistance.entities.Employee

@Repository
interface EmployeeRepository : CrudRepository<Employee, Long> {
    fun findByUsername(login: String): Employee?
}