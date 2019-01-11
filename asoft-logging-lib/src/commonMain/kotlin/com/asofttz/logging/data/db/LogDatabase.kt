package com.asofttz.logging.data

import com.asofttz.logging.Log
import com.asofttz.logging.data.dao.ClientLogDao
import com.asofttz.rx.ObservableList

class LogDatabase private constructor() {

    var logDao: LogDao = ClientLogDao()
    var logs = ObservableList<Log>()

    companion object {
        val instance by lazy { LogDatabase() }
    }
}