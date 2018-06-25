package ru.tecforce.worktime.persistance.entities

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import java.util.Date


data class Worklog(
        val id: Long,
        val hours: Float,
        val comments: String,
        @JsonProperty("spent_on")
        @DateTimeFormat(pattern = "YYYY-mm-dd")
        val spentOn: Date
)