package ru.tecforce.worktime.persistance.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.SEQUENCE
import javax.persistence.Id

@Entity
data class Employee(
        @Id
        @GeneratedValue(strategy = SEQUENCE)
        val id: Long? = null,
        val redmineId: Long,
        @Column(unique = true)
        val username: String,
        val firstName: String? = null,
        val lastName: String? = null,
        val notificationEnable: Boolean = false
)