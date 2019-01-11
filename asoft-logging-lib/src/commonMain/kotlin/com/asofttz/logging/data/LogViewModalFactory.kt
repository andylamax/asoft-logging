package com.asofttz.logging.data

class LogViewModalFactory(private val logRepository: LogRepository) {
    private val viewModal by lazy { LogViewModal(logRepository) }

    fun create() = viewModal
}