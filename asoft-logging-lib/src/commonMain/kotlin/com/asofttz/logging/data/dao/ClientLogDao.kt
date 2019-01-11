package com.asofttz.logging.data.dao

import com.asofttz.logging.data.LogDao
import com.asofttz.logging.Log
import com.asofttz.rx.ObservableList
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JSON
import kotlinx.serialization.list

class ClientLogDao : LogDao() {
    private val client = HttpClient()

    override fun saveLog(log: Log) {
        cachedLogs.add(log)
    }

    override fun getLogs(): ObservableList<Log> {
        GlobalScope.launch {
            val jsonLogs = client.get<String>("http://192.168.43.218:8080/logs")
            val logs = JSON.parse(Log.serializer().list, jsonLogs)
            cachedLogs.value = logs as MutableList<Log>
        }
        return cachedLogs
    }
}