package com.asofttz.logging.data

import com.asofttz.logging.Log
import com.asofttz.rx.ObservableList

abstract class LogDao {
    protected var cachedLogs = ObservableList<Log>()

    abstract fun saveLog(log: Log)

    abstract fun getLogs(): ObservableList<Log>
}