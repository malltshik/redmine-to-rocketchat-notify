package ru.tecforce.worktime.services

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import ru.tecforce.worktime.clients.RedmineClient
import ru.tecforce.worktime.clients.RedmineUser
import ru.tecforce.worktime.persistance.entities.Employee
import ru.tecforce.worktime.persistance.repositories.EmployeeRepository

@RunWith(SpringRunner::class)
@SpringBootTest
@DataJpaTest
// TODO should be done
class EmployeeServiceTest {


//    private lateinit var employeeService: EmployeeService
//
//    @MockBean
//    private lateinit var employeeRepository: EmployeeRepository
//    @MockBean
//    private lateinit var redmineClient: RedmineClient
//
//    private var argumentor: ArgumentCaptor<Employee>? = ArgumentCaptor.forClass(Employee::class.java)
//    private var redmineUser = RedmineUser(1, "login", "firstname", "lastname", "mail")
//
//    @Before
//    fun before() {
//        employeeService = EmployeeService(employeeRepository, redmineClient)
//        `when`(employeeRepository.findAll()).thenReturn(emptyList())
//        `when`(redmineClient.fetchWorklogs(any(), any())).thenReturn(emptyList())
//        `when`(redmineClient.fetchUsers()).thenReturn(listOf(redmineUser))
//    }
//
//    @Test
//    fun syncRedmineUsers() {
//        employeeService.syncRedmineUsers()
//        verify(employeeRepository).save(argumentor?.capture()!!)
//        val employee = argumentor!!.value
//        assertThat(employee.redmineId, equalTo(1L))
//    }

    @Test
    fun findAll() {
    }

    @Test
    fun findOne() {
    }

    @Test
    fun userToggleNotify() {
    }

}