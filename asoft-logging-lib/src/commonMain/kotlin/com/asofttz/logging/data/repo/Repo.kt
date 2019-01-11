package com.asofttz.logging.data.repo

import com.asofttz.logging.data.LogDao
import com.asofttz.logging.Log
import com.asofttz.rx.ObservableList

abstract class Repo(private val logDao: LogDao) {
    protected var cachedLogs = ObservableList<Log>()
    open val max_cache = 50

    abstract fun saveLog(log: Log)
    abstract fun getLogs(): ObservableList<Log>
}