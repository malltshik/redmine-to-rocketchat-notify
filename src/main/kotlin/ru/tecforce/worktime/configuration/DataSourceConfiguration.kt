package ru.tecforce.worktime.configuration

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import ru.tecforce.worktime.properties.DatabaseProperties

@Configuration
class DataSourceConfiguration(private val props: DatabaseProperties) {

    @Bean @Primary
    fun dataSourceProperties(): DataSourceProperties = DataSourceProperties().also{
        it.url = "jdbc:h2:${props.path};DB_CLOSE_ON_EXIT=FALSE"
        it.driverClassName = "org.h2.Driver"
    }

}