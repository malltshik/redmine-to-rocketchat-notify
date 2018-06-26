package ru.tecforce.worktime.clients;

import com.fasterxml.jackson.annotation.JsonProperty
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustStrategy
import org.apache.http.impl.client.HttpClients
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.tecforce.worktime.properties.RedmineProperties
import java.text.SimpleDateFormat
import java.util.*

@Component
class RedmineClient(private val props: RedmineProperties, private val restTemplateBuilder: RestTemplateBuilder) {

    private val MAX_LIMIT = 100

    private val restTemplate: RestTemplate = restTemplateBuilder.requestFactory {
        val requestFactory = HttpComponentsClientHttpRequestFactory()
        try {
            val acceptingTrustStrategy = TrustStrategy {
                _: Array<out java.security.cert.X509Certificate>?,
                _: String -> true
            }
            val sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build()
            val csf = SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier())
            val httpClient = HttpClients.custom().setSSLSocketFactory(csf).build()
            requestFactory.httpClient = httpClient
        } catch (ignored: Exception) {
        }
        requestFactory
    }.build()

    fun fetchUsers(): List<RedmineUser> {
        val users: MutableList<RedmineUser> = mutableListOf()
        var offset = 0
        while (true) {
            val message = this.restTemplate.getForEntity(
                    url("users", mapOf("status" to "1", "offset" to "$offset")), UsersMessage::class.java).body
            if (message != null) {
                users.addAll(message.users)
                if (message.users.size < message.limit) break
                else offset += message.limit
            } else break
        }
        return users
    }

    fun fetchWorklogs(id: Long, date: Date): List<Worklog> {
        val spendOn = SimpleDateFormat("YYYY-MM-dd").format(date)
        val worklogs: MutableList<Worklog> = mutableListOf()
        var offset = 0
        while (true) {
            val message = this.restTemplate.getForEntity(url("time_entries",
                    mapOf("spent_on" to spendOn, "id" to "$id")), WorklogMessage::class.java).body
            if (message != null) {
                worklogs.addAll(message.worklogs)
                if (message.worklogs.size < message.limit) break
                else offset += message.limit
            } else break
        }
        return worklogs
    }

    private fun url(path: String, params: Map<String, String> = emptyMap()): String {
        var url = "${props.address}/$path.json?key=${props.token}&limit=${MAX_LIMIT}&"
        params.forEach { k, v -> url += "$k=$v&" }
        return url
    }

    private data class UsersMessage(
            val users: Array<RedmineUser>,
            @JsonProperty("total_count") val totalCount: Int,
            val offset: Int,
            val limit: Int
    )

    private data class WorklogMessage(
            @JsonProperty("time_entries") val worklogs: Array<Worklog>,
            @JsonProperty("total_count") val totalCount: Int,
            val offset: Int,
            val limit: Int
    )

}

data class RedmineUser(val id: Long, val login: String, val firstname: String, val lastname: String, val mail: String)
data class Worklog(val id: Long, val hours: Float, val comments: String,
                   @JsonProperty("spent_on")
                   @DateTimeFormat(pattern = "YYYY-mm-dd")
                   val spentOn: Date)