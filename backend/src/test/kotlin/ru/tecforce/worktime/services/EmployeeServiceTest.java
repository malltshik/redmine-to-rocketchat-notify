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
import ru.tecforce.worktime.exceptions.NotFoundException;
import ru.tecforce.worktime.persistance.entities.Employee;
import ru.tecforce.worktime.persistance.repositories.EmployeeRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
import static org.mockito.BDDMockito.given;
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
            new Employee(RMUSER).edit(x -> { x.setId(1L); x.setUsername("user1"); }),
            new Employee(RMUSER).edit(x -> { x.setId(2L); x.setUsername("user2"); }),
            new Employee(RMUSER).edit(x -> { x.setId(3L); x.setUsername("user3"); })
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
        assertEquals(employeeService.findByUsername("user1").getUsername(), "user1");
        assertEquals(employeeService.findByUsername("user3").getUsername(), "user3");
        try {
            employeeService.findByUsername("unknown");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Employee with username 'unknown' not found!");
            assertEquals(e.getClass(), NotFoundException.class);
        }
    }

    @Test
    public void userToggleNotifyTest() {
        Employee employee = employeeService.userToggleNotify(1L);
        assertEquals(employee.getId(), (Long) 1L);
        assertEquals(employee.getUsername(), "user1");
        assertEquals(employee.getNotificationEnable(), true);

        employee = employeeService.userToggleNotify(1L);
        assertEquals(employee.getId(), (Long) 1L);
        assertEquals(employee.getUsername(), "user1");
        assertEquals(employee.getNotificationEnable(), false);
    }

}
