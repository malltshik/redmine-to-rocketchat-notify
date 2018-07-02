package ru.tecforce.worktime.configuration

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.jdbc.DataSourceInitializationMode
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import ru.tecforce.worktime.properties.DatabaseProperties

// TODO disable overriding database file every single start
@Configuration
class DataSourceConfiguration(private val props: DatabaseProperties) {

    @Bean @Primary
    fun dataSourceProperties(): DataSourceProperties = DataSourceProperties().also{
        it.url = "jdbc:h2:${props.path};DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1;"
        it.driverClassName = "org.h2.Driver"
        it.initializationMode = DataSourceInitializationMode.ALWAYS
        it.isContinueOnError = false
        it.username = "sa"
        it.password = null
    }

}