package com.asofttz.logging

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.response.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JSON

open class LogServer(private val url: String? = null) {
    private val client = HttpClient()
    private val logger = Logger("Log Server")

    fun getLogger(source: String) = Logger(source, this)

    fun pushToServer(log: Log) = GlobalScope.launch(Dispatchers.Unconfined) {
        val logJson = JSON.stringify(Log.serializer(), log)
        client.post<Unit>("$url/log") {
            body = TextContent(logJson, ContentType.Application.Json)
        }
    }
}