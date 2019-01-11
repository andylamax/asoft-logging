package com.asofttz.logging.data

import com.asofttz.logging.Log

class LogViewModal(private val repository: LogRepository) {

    fun getLogs() = repository.getLogs()

    fun saveLog(log: Log) = repository.saveLog(log)
}