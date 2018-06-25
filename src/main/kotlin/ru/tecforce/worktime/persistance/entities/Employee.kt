package ru.tecforce.worktime.persistance.entities

import javax.persistence.*
import javax.persistence.GenerationType.SEQUENCE

@Entity
data class Employee(
        @Id
        @GeneratedValue(strategy = SEQUENCE)
        private val id: Long,
        @Column(unique = true)
        private val username: String
)