package ru.tecforce.worktime

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableConfigurationProperties(RedmineProperties::class)
@EnableScheduling
class WorktimeApplication

fun main(args: Array<String>) {
    runApplication<WorktimeApplication>(*args)
}
