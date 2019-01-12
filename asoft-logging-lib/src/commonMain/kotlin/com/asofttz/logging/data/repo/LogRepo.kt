package com.asofttz.logging.data.repo

import com.asofttz.logging.Log
import com.asofttz.logging.data.LogDao
import com.asofttz.rx.ObservableList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LogRepo private constructor(private val logDao: LogDao) {
    companion object {
        private val instance: LogRepo? = null
        fun getInstance(logDao: LogDao) = instance ?: LogRepo(logDao)
    }

    fun saveLog(log: Log) = logDao.saveLog(log)

    fun getLogs() = logDao.getLogs()
}