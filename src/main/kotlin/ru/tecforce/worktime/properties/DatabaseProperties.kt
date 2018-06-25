package ru.tecforce.worktime.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

@Validated
@ConfigurationProperties("database")
class DatabaseProperties {

    /**
     * H2 database path to file
     */
    @NotEmpty(message = "You should have set database path property to application.properties")
    lateinit var path: String
}