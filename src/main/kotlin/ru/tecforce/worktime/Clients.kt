package ru.tecforce.worktime

import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustStrategy
import org.apache.http.impl.client.HttpClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.text.SimpleDateFormat
import java.util.*

@Component
class RedmineClient(restTemplateBuilder: RestTemplateBuilder) {

    @Value("\${redmine.address}")
    val address: String? = null

    @Value("\${redmine.token}")
    val token: String? = null

    val restTemplate: RestTemplate = restTemplateBuilder.requestFactory {
        val requestFactory = HttpComponentsClientHttpRequestFactory()
        try {
            val acceptingTrustStrategy = TrustStrategy {
                chain: Array<out java.security.cert.X509Certificate>?,
                authType: String -> true
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
        val body = this.restTemplate.getForEntity("${address}/users.json?key=${token}", UsersMessage::class.java).body
        return body?.users ?: emptyArray<User>()
    }

    // TODO worklogs clould be more then ${limit} default 100
    fun fetchWorklogs(id: Long, date: Date): Array<Worklog> {
        val spendOn = SimpleDateFormat("YYYY-mm-dd").format(date);
        val body = this.restTemplate.getForEntity(
                "${address}/time_entries.json?id=${id}&spent_on=${spendOn}&limit=100&key=${token}",
                WorklogMessage::class.java).body
        return body?.worklogs ?: emptyArray<Worklog>()
    }

}

