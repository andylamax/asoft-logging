package com.asofttz.logging.data.viewmodal

import com.asofttz.logging.Log
import com.asofttz.logging.data.repo.LogRepo

class LogViewModal(private val repository: LogRepo) {

    fun getLogs() = repository.getLogs()

    fun saveLog(log: Log) = repository.saveLog(log)
}