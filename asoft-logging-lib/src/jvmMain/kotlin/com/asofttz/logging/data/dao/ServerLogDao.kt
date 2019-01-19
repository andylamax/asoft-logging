package com.asofttz.logging.data.dao

import com.asofttz.logging.Log
import com.asofttz.logging.data.LogDao
import com.asofttz.logging.data.db.LogDataSourceConfig
import com.asofttz.rx.ObservableList
import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.SessionFactory

class ServerLogDao private constructor(config: LogDataSourceConfig) : LogDao() {
    companion object {
        private var instance: LogDao? = null
        fun getInstance(config: LogDataSourceConfig): LogDao {
            synchronized(this) {
                if (instance == null) {
                    instance = ServerLogDao(config)
                }
                return instance!!
            }
        }
    }

    private val configuration = Configuration.Builder()
            .uri(config.url)
            .credentials(config.username, config.password)
            .build()

    private val sessionFactory = SessionFactory(configuration,
            Log::class.java.`package`.name
    )

    override fun saveLog(log: Log) = synchronized(this) {
        with(sessionFactory.openSession()) {
            save(log)
            cachedLogs.add(0, log)
            clear()
        }
    }

    override fun getLogs() = synchronized(this) {
        with(sessionFactory.openSession()) {
            val logs = loadAll(Log::class.java)
            ObservableList<Log>().apply {
                value = logs.toMutableList()
            }
        }
    }
}