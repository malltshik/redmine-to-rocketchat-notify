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
import ru.tecforce.worktime.exceptions.ConflictException;
import ru.tecforce.worktime.exceptions.NotFoundException;
import ru.tecforce.worktime.persistance.entities.Employee;
import ru.tecforce.worktime.services.EmployeeService;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyDouble;
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
                new Employee(RMUSER).edit(x -> { x.setId(1L);x.setUsername("employee1"); }),
                new Employee(RMUSER).edit(x -> { x.setId(2L);x.setUsername("employee2"); }),
                new Employee(RMUSER).edit(x -> { x.setId(3L);x.setUsername("employee3"); })
        );

        when(employeeService.findAll()).thenReturn(EMPLOYEES);

        when(employeeService.findOne(anyLong())).then(i -> EMPLOYEES.stream()
                .filter(x -> x.getId() == i.getArgument(0)).findFirst()
                .orElseThrow(() -> new NotFoundException("Employee not found")));

        when(employeeService.setRequiredTimeToLog(anyLong(), anyDouble())).then(i -> {
            Double time = i.getArgument(1);
            if(time < 0 || time > 8) throw new ConflictException("Conflict time");
            Optional<Employee> emp = EMPLOYEES.stream().filter(x -> x.getId() == i.getArgument(0)).findFirst();
            return emp.orElseThrow(() -> new NotFoundException("Employee not found"))
                    .edit(e -> e.setRequiredTimeToLog(time));
        });

        when(employeeService.toggleNotify(anyLong())).then(i -> {
            Optional<Employee> emp = EMPLOYEES.stream().filter(x -> x.getId() == i.getArgument(0)).findFirst();
            return emp.orElseThrow(() -> new NotFoundException("Employee not found"))
                    .edit(e -> e.setNotificationEnable(!e.getNotificationEnable()));
        });

    }

    @Test
    public void getAllemployees() throws Exception {
        mvc.perform(get("/api/employees/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void findOneUserById() throws Exception {
        mvc.perform(get("/api/employees/1/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username", equalTo("employee1")))
                .andExpect(jsonPath("firstName", equalTo("firstname")))
                .andExpect(jsonPath("lastName", equalTo("lastname")))
                .andExpect(jsonPath("notificationEnable", equalTo(false)))
                .andExpect(jsonPath("requiredTimeToLog", equalTo(8.0)));

        mvc.perform(get("/api/employees/42/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", equalTo("Employee not found")))
                .andExpect(jsonPath("status", equalTo(404)));
    }

    @Test
    public void toggleNotifications() throws Exception {
        mvc.perform(get("/api/employees/1/toggleNotify/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username", equalTo("employee1")))
                .andExpect(jsonPath("firstName", equalTo("firstname")))
                .andExpect(jsonPath("lastName", equalTo("lastname")))
                .andExpect(jsonPath("notificationEnable", equalTo(true)))
                .andExpect(jsonPath("requiredTimeToLog", equalTo(8.0)));
    }

    @Test
    public void setRequiredTimeToLog() throws Exception {
        mvc.perform(get("/api/employees/1/setRequiredTimeToLog/?timeToLog=4.0").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username", equalTo("employee1")))
                .andExpect(jsonPath("firstName", equalTo("firstname")))
                .andExpect(jsonPath("lastName", equalTo("lastname")))
                .andExpect(jsonPath("notificationEnable", equalTo(false)))
                .andExpect(jsonPath("requiredTimeToLog", equalTo(4.0)));

        mvc.perform(get("/api/employees/42/setRequiredTimeToLog/?timeToLog=4.0").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", equalTo("Employee not found")))
                .andExpect(jsonPath("status", equalTo(404)));

        mvc.perform(get("/api/employees/1/setRequiredTimeToLog/?timeToLog=9.0").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message", equalTo("Conflict time")))
                .andExpect(jsonPath("status", equalTo(409)));
    }


}
