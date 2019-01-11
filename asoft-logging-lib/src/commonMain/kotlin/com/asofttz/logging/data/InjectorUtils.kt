package com.asofttz.logging.data

object InjectorUtils {
    fun provideQuotesViewModalFactory(): LogViewModalFactory {
        val logRepository = LogRepository.getInstance(LogDatabase.instance.logDao)
        return LogViewModalFactory(logRepository)
    }

    fun test() {
        val factory = provideQuotesViewModalFactory()
        factory.create().getLogs().observe { oldValue, newValue ->
            //piga kelele
        }
    }
}