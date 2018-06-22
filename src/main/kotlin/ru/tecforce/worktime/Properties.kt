package ru.tecforce.worktime

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

@ConfigurationProperties("redmine")
@Validated
class RedmineProperties {

    /**
     * Redmine server address
     */
    @NotEmpty(message = "You should have set redmine address property to application.properties")
    @Pattern(regexp = "^(http|https)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
            message = "Address should pass the URL regexp pattern")
    lateinit var address:String


    /**
     *  Redmine API token (40 characters hash)
     */
    @NotEmpty(message = "You should have set redmine API token property to application.properties")
    @Pattern(regexp = "(.*){40,}", message = "The API key is not valid")
    lateinit var token:String






}