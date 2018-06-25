package ru.tecforce.worktime.web.controllers

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.tecforce.worktime.persistance.entities.User
import ru.tecforce.worktime.persistance.repositories.UserRepository

@RestController
@RequestMapping("/api/users")
class UserController(private val userRepository: UserRepository) {

    @RequestMapping("/")
    fun users(): List<User>  = userRepository.findAll();

}