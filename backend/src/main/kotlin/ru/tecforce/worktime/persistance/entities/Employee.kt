package ru.tecforce.worktime.persistance.entities

import ru.tecforce.worktime.clients.RedmineUser
import java.util.function.Consumer
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.SEQUENCE
import javax.persistence.Id

@Entity
data class Employee(
        @Id
        @GeneratedValue(strategy = SEQUENCE)
        var id: Long? = null,
        val redmineId: Long,
        @Column(unique = true)
        var username: String,
        var firstName: String? = null,
        var lastName: String? = null,
        var notificationEnable: Boolean = false,
        var requiredTimeToLog: Double = 8.0
) {
        constructor(ru: RedmineUser) : this(null, ru.id, ru.login, ru.firstname, ru.lastname)

        fun edit(f: Consumer<Employee>): Employee {
                f.accept(this)
                return this
        }
}