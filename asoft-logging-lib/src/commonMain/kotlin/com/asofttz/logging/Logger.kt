package com.asofttz.logging

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JSON

expect class Logger(source: String = "anonymous") {
    val source: String
    fun d(msg: String)
    fun e(msg: String)
    fun f(msg: String)
    fun w(msg: String)
    fun i(msg: String)
}

fun pushToServer(url: String, log: Log) {
    GlobalScope.launch {
        val client = HttpClient()
        val logJson = JSON.stringify(Log.serializer(), log)
        client.post<Unit>("http://localhost:8080/log") {
            body = TextContent(logJson, ContentType.Application.Json)
        }
    }
}