package com.asofttz.logging

import com.asofttz.logging.data.viewmodal.LogViewModal

open class LogServer(private val logViewModal: LogViewModal) {

    fun getLogger(source: String) = Logger(source, this)

    fun pushToServer(log: Log) = logViewModal.saveLog(log)
}