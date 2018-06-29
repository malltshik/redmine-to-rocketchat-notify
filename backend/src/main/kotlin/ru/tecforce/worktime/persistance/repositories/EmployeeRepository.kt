package ru.tecforce.worktime.persistance.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.tecforce.worktime.persistance.entities.Employee
import java.util.*

@Repository
interface EmployeeRepository : CrudRepository<Employee, Long> {
    fun findByUsername(login: String): Optional<Employee>
}