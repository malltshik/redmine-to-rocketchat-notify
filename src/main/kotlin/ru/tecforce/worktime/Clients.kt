package ru.tecforce.worktime

import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustStrategy
import org.apache.http.impl.client.HttpClients
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.text.SimpleDateFormat
import java.util.*

@Component
class RedmineClient(val props: RedmineProperties, restTemplateBuilder: RestTemplateBuilder) {

    val restTemplate: RestTemplate = restTemplateBuilder.requestFactory {
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

    // TODO fetch all users with bunch by bunch with offset parameter
    fun fetchUsers(): Array<User> {
        val body = this.restTemplate.getForEntity(url("users"), UsersMessage::class.java).body
        return body?.users ?: emptyArray()
    }

    // TODO worklogs could be more then ${limit} default 100
    fun fetchWorklogs(id: Long, date: Date): Array<Worklog> {
        val spendOn = SimpleDateFormat("YYYY-mm-dd").format(date);
        val body = this.restTemplate.getForEntity(url("time_entries",
                mapOf("spent_on" to spendOn, "id" to "$id", "limit" to "100")),
                WorklogMessage::class.java).body
        return body?.worklogs ?: emptyArray()
    }

    private fun url(path: String, params: Map<String, String> = emptyMap()): String {
        var url = "${props.address}/$path.json?key=${props.token}&"
        params.forEach { k, v -> url += "$k=$v&" }
        return url
    }

}