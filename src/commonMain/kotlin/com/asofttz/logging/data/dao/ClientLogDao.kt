package com.asofttz.logging.data.dao

import com.asofttz.logging.Log
import com.asofttz.logging.Logger
import com.asofttz.persist.DataSourceConfig
import com.asofttz.persist.PaginatedDao
import com.asofttz.persist.Singleton
import com.asofttz.persist.memory.Memory
import com.asofttz.rx.ObservableList
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import kotlinx.serialization.json.Json
import kotlinx.serialization.list

class ClientLogDao private constructor(private val config: DataSourceConfig) : PaginatedDao<Log>() {

    private val logger = Logger("log-engine")

    private val client = HttpClient {
        expectSuccess = false
    }

    companion object : Singleton<DataSourceConfig, PaginatedDao<Log>>({ ClientLogDao(it) })

    override suspend fun create(log: Log): Log? {
        memory.add(log)
        val url = "${config.url}/log"
        val logJson = Json.stringify(Log.serializer(), log)
        try {
            val response = client.post<HttpResponse>(url) {
                body = TextContent(logJson, ContentType.Application.Json)
            }
            if (response.status != HttpStatusCode.OK) {
                logger.w("Failed to send log {${log.msg}} to server")
                return null
            }
        } catch (e: Throwable) {
            logger.e("Failed to send log \"${log.msg}\" to server")
        }
        return log
    }

    override suspend fun edit(t: Log): Log? {
        return null
    }

    override suspend fun delete(t: Log): Log? {
        return null
    }

    override suspend fun load(id: Int): Log? {
        return memory.value.getAll().getOrNull(id)
    }

    override suspend fun loadAll(): MutableList<Log> {
        if (memory.size == 0) {
            try {
                val url = "${config.url}/logs"
                val response = client.get<HttpResponse>(url)
                if (response.status == HttpStatusCode.OK) {
                    val jsonLogs = response.readText()
                    val logs = Json.parse(Log.serializer().list, jsonLogs)
                    memory.value.addAll(logs) //=  as MutableList<Log>
                } else {
                    logger.e("Couldn't retrieve logs from server")
                }
            } catch (e: Throwable) {
                logger.e(e.message.toString())
            }
        }
        return memory.getAll()
    }
}