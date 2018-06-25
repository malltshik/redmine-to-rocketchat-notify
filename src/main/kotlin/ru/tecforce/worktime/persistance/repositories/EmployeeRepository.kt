package ru.tecforce.worktime.persistance.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.tecforce.worktime.persistance.entities.Employee

@Repository
interface EmployeeRepository : CrudRepository<Employee, Long>