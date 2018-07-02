package ru.tecforce.worktime.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tecforce.worktime.clients.RedmineClient;
import ru.tecforce.worktime.clients.RedmineUser;
import ru.tecforce.worktime.exceptions.ConflictException;
import ru.tecforce.worktime.exceptions.NotFoundException;
import ru.tecforce.worktime.persistance.entities.Employee;
import ru.tecforce.worktime.persistance.repositories.EmployeeRepository;

import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@Import(EmployeeService.class)
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private RedmineClient redmineClient;

    private final static RedmineUser RMUSER = new RedmineUser(42L, "username", "firstname", "lastname", "mail");

    private final static List<Employee> EMPLOYEES = asList(
            new Employee(RMUSER).edit(x -> { x.setId(1L); x.setUsername("employee1"); }),
            new Employee(RMUSER).edit(x -> { x.setId(2L); x.setUsername("employee2"); }),
            new Employee(RMUSER).edit(x -> { x.setId(3L); x.setUsername("employee3"); })
    );

    private ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);


    @Before
    public void before() {
        when(redmineClient.fetchUsers()).thenReturn(singletonList(RMUSER));

        when(employeeRepository.findAll()).thenReturn(EMPLOYEES);

        when(employeeRepository.findById(anyLong())).then(i -> EMPLOYEES.stream()
                .filter(e -> Objects.equals(e.getId(), i.getArgument(0))).findFirst());

        when(employeeRepository.findByUsername(anyString())).then(i -> EMPLOYEES.stream()
                .filter(e -> Objects.equals(e.getUsername(), i.getArgument(0))).findFirst());

        when(employeeRepository.save(any(Employee.class))).then(i -> i.getArgument(0));

    }

    @Test
    public void syncRedmineUsersTest() {

        employeeService.syncRedmineUsers();

        verify(employeeRepository).save(employeeCaptor.capture());
        Employee employee = employeeCaptor.getValue();
        assertNull(employee.getId());
        assertEquals(employee.getUsername(), "username");
        assertEquals(employee.getFirstName(), "firstname");
        assertEquals(employee.getLastName(), "lastname");
        assertEquals(employee.getRedmineId(), 42L);
        assertEquals(employee.getNotificationEnable(), false);
        assertEquals(employee.getRequiredTimeToLog(), 8.0);
    }

    @Test
    public void findAllTest() {
        List<Employee> all = employeeService.findAll();
        assertThat(all, hasSize(3));
        assertThat(all.stream().filter(e -> e.getId() == null).collect(toList()), hasSize(0));
    }

    @Test
    public void findOneTest() {
        assertEquals(employeeService.findOne(1L).getId(), (Long) 1L);
        assertEquals(employeeService.findOne(2L).getId(), (Long) 2L);
        try {
            employeeService.findOne(42L);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Employee with ID: 42 not found!");
            assertEquals(e.getClass(), NotFoundException.class);
        }
    }

    @Test
    public void findByUsernameTest() {
        assertEquals(employeeService.findOne("employee1").getUsername(), "employee1");
        assertEquals(employeeService.findOne("employee3").getUsername(), "employee3");
        try {
            employeeService.findOne("unknown");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Employee with username 'unknown' not found!");
            assertEquals(e.getClass(), NotFoundException.class);
        }
    }

    @Test
    public void toggleNotifyTest() {
        Employee employee = employeeService.toggleNotify(1L);
        assertEquals(employee.getId(), (Long) 1L);
        assertEquals(employee.getUsername(), "employee1");
        assertEquals(employee.getNotificationEnable(), true);

        employee = employeeService.toggleNotify(1L);
        assertEquals(employee.getId(), (Long) 1L);
        assertEquals(employee.getUsername(), "employee1");
        assertEquals(employee.getNotificationEnable(), false);
    }

    @Test
    public void setUpRequirementTimeToLog() {
        Employee employee = employeeService.setRequiredTimeToLog(1L, 7.0);
        assertEquals(employee.getId(), (Long) 1L);
        assertEquals(employee.getUsername(), "employee1");
        assertEquals(employee.getRequiredTimeToLog(), 7.0);
        try {
            employeeService.setRequiredTimeToLog(1L, 9.0);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Required time should have bean more then 0 and less 8 hours for day");
            assertEquals(e.getClass(), ConflictException.class);
        }

    }

}
