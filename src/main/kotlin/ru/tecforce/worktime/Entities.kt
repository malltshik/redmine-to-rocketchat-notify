package ru.tecforce.worktime

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import java.util.*


data class User(val id: Long, val login: String, val firstname: String, val lastname: String, val mail: String)
data class UsersMessage(val users: Array<User>)
data class Worklog(
    val id: Long,
    val project: Project,
    val hours: Float,
    val comment: String,
    @JsonProperty("spent_on")
    @DateTimeFormat(pattern = "YYYY-mm-dd")
    val spentOn: Date)
data class Project(val id: Long, val name: String)

data class WorklogMessage(@JsonProperty("time_entries") val worklogs: Array<Worklog>)
