package com.asofttz.logging.data.repo

import com.asofttz.logging.Log
import com.asofttz.logging.data.LogDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ClientLogRepo private constructor(private val logDao: LogDao) : Repo(logDao) {

    companion object {
        private val instance: ClientLogRepo? = null
        fun getInstance(logDao: LogDao) = instance ?: ClientLogRepo(logDao)
    }

    override fun saveLog(log: Log) {
        GlobalScope.launch {
            logDao.saveLog(log)
        }
    }

    fun deleteLog(log: Log) {}

    override fun getLogs() = cachedLogs.also {
        GlobalScope.launch {
            cachedLogs = logDao.getLogs()
        }
    }
}