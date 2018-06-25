package ru.tecforce.worktime.clients

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import ru.tecforce.worktime.persistance.entities.User
import ru.tecforce.worktime.properties.RocketChatProperties

@Component
class RocketChatClient(private val props: RocketChatProperties,
                       private val restTemplateBuilder: RestTemplateBuilder) {

    private val API = "${props.address}/api/v1"
    private val restTemplate: RestTemplate = restTemplateBuilder.build()
    private var credential: CredentialResponseData? = null

    fun sendMessage(user: User, message: String) {
        if (this.credential == null || !isLoggined()) login()
        restTemplate.exchange("$API/chat.postMessage",
                HttpMethod.POST,
                HttpEntity(Message("@${user.login}", message), headers()),
                Message::class.java)
    }

    private fun headers(): HttpHeaders {
        return HttpHeaders().also {
            it.add("X-Auth-Token", this.credential?.authToken)
            it.add("X-User-Id", this.credential?.userId)
        }
    }

    private fun isLoggined(): Boolean {
        return try {
            restTemplate.exchange("$API/me", HttpMethod.GET, HttpEntity<Any>(headers()), String::class.java).body != null
        } catch (e: Exception) {
            false
        }
    }

    private fun login() {
        val credentials = mapOf("username" to props.username, "password" to props.password);
        val credentialResponse = restTemplate.postForEntity(
                "${props.address}/api/v1/login", credentials, CredentialResponse::class.java).body
        this.credential = credentialResponse?.data
    }

    private data class Message(val channel: String, val text: String? = "")
    private data class CredentialResponse(val data: CredentialResponseData)
    private data class CredentialResponseData(val authToken: String, val userId: String)

}
