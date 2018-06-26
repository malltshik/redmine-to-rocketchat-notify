package ru.tecforce.worktime.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

@ConfigurationProperties("rocketchat")
@Validated
class RocketChatProperties {

    /**
     * Rocket chat server address
     */
    @NotEmpty(message = "You should have set rocket chat address property to application.properties")
    @Pattern(regexp = "^(http|https)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
            message = "Address should pass the URL regexp pattern")
    lateinit var address: String


    /**
     *  Rocket chat sender username
     */
    @NotEmpty(message = "You should have set rocket chat sender username to application.properties")
    lateinit var username: String

    /**
     *  Rocket chat sender password
     */
    @NotEmpty(message = "You should have set rocket chat sender password to application.properties")
    lateinit var password: String


}