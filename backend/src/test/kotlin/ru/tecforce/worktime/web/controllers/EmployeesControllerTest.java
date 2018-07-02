package ru.tecforce.worktime.web.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.tecforce.worktime.clients.RedmineUser;
import ru.tecforce.worktime.exceptions.NotFoundException;
import ru.tecforce.worktime.persistance.entities.Employee;
import ru.tecforce.worktime.services.EmployeeService;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeesController.class)
public class EmployeesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmployeeService employeeService;

    private final static RedmineUser RMUSER = new RedmineUser(42L, "username", "firstname", "lastname", "mail");

    @Before
    public void before() throws Exception {
        final List<Employee> EMPLOYEES = asList(
                new Employee(RMUSER).edit(x -> { x.setId(1L); x.setUsername("user1"); }),
                new Employee(RMUSER).edit(x -> { x.setId(2L); x.setUsername("user2"); }),
                new Employee(RMUSER).edit(x -> { x.setId(3L); x.setUsername("user3"); })
        );

        when(employeeService.findAll()).thenReturn(EMPLOYEES);

        when(employeeService.findOne(anyLong())).then(i -> EMPLOYEES.stream()
                .filter(x -> x.getId() == i.getArgument(0)).findFirst()
                .orElseThrow(() -> new NotFoundException("User not found!")));

        when(employeeService.userToggleNotify(anyLong())).then(i -> EMPLOYEES.stream()
                .filter(x -> x.getId() == i.getArgument(0))
                .peek(x -> x.setNotificationEnable(!x.getNotificationEnable())).findFirst()
                .orElseThrow(() -> new NotFoundException("User not found!")));
    }

    @Test
    public void users() throws Exception {
        mvc.perform(get("/api/users/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void user() throws Exception {
        mvc.perform(get("/api/users/1/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("username", equalTo("user1")))
            .andExpect(jsonPath("firstName", equalTo("firstname")))
            .andExpect(jsonPath("lastName", equalTo("lastname")))
            .andExpect(jsonPath("notificationEnable", equalTo(false)))
            .andExpect(jsonPath("requiredTimeToLog", equalTo(8.0)));
    }

    @Test
    public void userToggleNotifications() throws Exception {
        mvc.perform(get("/api/users/1/toggleNotify/").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("username", equalTo("user1")))
            .andExpect(jsonPath("firstName", equalTo("firstname")))
            .andExpect(jsonPath("lastName", equalTo("lastname")))
            .andExpect(jsonPath("notificationEnable", equalTo(true)))
            .andExpect(jsonPath("requiredTimeToLog", equalTo(8.0)));
    }

}
