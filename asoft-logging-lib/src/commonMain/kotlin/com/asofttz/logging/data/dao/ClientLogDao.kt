package com.asofttz.logging.data.dao

import com.asofttz.logging.data.LogDao
import com.asofttz.logging.Log
import com.asofttz.logging.data.db.LogDataSourceConfig
import com.asofttz.rx.ObservableList
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JSON
import kotlinx.serialization.list

class ClientLogDao private constructor(private val config: LogDataSourceConfig) : LogDao() {

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

            client.post<Unit>(url) {
                body = TextContent(logJson, ContentType.Application.Json)
            }
        }
        cachedLogs.add(log)
    }

    override fun getLogs(): ObservableList<Log> {
        GlobalScope.launch {
            val url = with(config) { fullUrl(route_get_logs) }
            val jsonLogs = client.get<String>(url)
            val logs = JSON.parse(Log.serializer().list, jsonLogs)
            cachedLogs.value = logs as MutableList<Log>
        }
        return cachedLogs
    }
}