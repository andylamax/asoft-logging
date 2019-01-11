package com.asofttz.logging.data

import com.asofttz.logging.Log

class LogRepository private constructor(private val logDao: LogDao) {
    companion object {
        private var instance: LogRepository? = null
        fun getInstance(logDao: LogDao) = instance ?: LogRepository(logDao)
    }

    fun saveLog(log: Log) {
        logDao.saveLog(log)
    }

    fun getLogs() = logDao.getLogs()
}