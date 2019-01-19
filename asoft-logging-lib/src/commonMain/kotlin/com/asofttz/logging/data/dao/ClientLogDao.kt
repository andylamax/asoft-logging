package com.asofttz.logging.data.dao

import com.asofttz.logging.data.LogDao
import com.asofttz.logging.Log
import com.asofttz.logging.Logger
import com.asofttz.logging.data.db.LogDataSourceConfig
import com.asofttz.rx.ObservableList
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.io.charsets.Charset
import kotlinx.io.charsets.Charsets
import kotlinx.serialization.json.JSON
import kotlinx.serialization.list

class ClientLogDao private constructor(private val config: LogDataSourceConfig) : LogDao() {
    private val logger = Logger("log-engine")

    companion object {
        private var instance: LogDao? = null
        fun getInstance(config: LogDataSourceConfig): LogDao {
            if (instance == null) {
                instance = ClientLogDao(config)
            }
            return instance!!
        }
    }

    private val client = HttpClient()

    override fun saveLog(log: Log) {
        GlobalScope.launch(Dispatchers.Unconfined) {
            val url = with(config) { fullUrl(route_post_log) }
            val logJson = JSON.stringify(Log.serializer(), log)

            val response = client.post<HttpResponse>(url) {
                body = TextContent(logJson, ContentType.Application.Json)
            }
            if (response.status != HttpStatusCode.OK) {
                logger.w("Failed to send log {${log.msg}} to server")
            }
        }
        cachedLogs.add(0, log)
    }

    override fun getLogs(): ObservableList<Log> {
        GlobalScope.launch {
            val url = with(config) { fullUrl(route_get_logs) }
            val response = client.get<HttpResponse>(url)
            if (response.status == HttpStatusCode.OK) {
                val jsonLogs = response.readText()
                val logs = JSON.parse(Log.serializer().list, jsonLogs)
                cachedLogs.value = logs as MutableList<Log>
            } else {
                logger.w("Couldn't retrieve logs from server")
            }
        }
        return cachedLogs
    }
}