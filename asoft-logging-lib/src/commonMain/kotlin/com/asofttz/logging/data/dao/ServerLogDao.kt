package com.asofttz.logging.data.dao

import com.asofttz.logging.data.LogDao
import com.asofttz.logging.Log

class ServerLogDao : LogDao() {

    override fun saveLog(log: Log) {

    }

    override fun getLogs() = cachedLogs
}