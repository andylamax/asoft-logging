package com.asofttz.logging.server

import com.asofttz.logging.Logger
import com.asofttz.logging.data.dao.ServerLogDao
import com.asofttz.logging.data.db.LogDataSourceConfig
import com.asofttz.logging.data.viewmodal.LogViewModal
import com.asofttz.persist.DataSourceConfig
import com.asofttz.persist.RepoFactory
import java.io.File

object injection {

    val logger = Logger("logging-server")

    val config = DataSourceConfig("bolt://localhost:9002").apply {
        username = "admin"
        password = "admin"
    }

    init {
        val classLoader = javaClass.classLoader
        try {
            val file = File(classLoader.getResource("credentials.txt").file)

            println("\n\n\n\n")
            logger.i("Loading Credentials")

            val line = file.readLines()
            config.apply {
                try {
                    username = line[0].split(" ")[0]
                    password = line[0].split(" ")[1]
                    logger.i("Success")
                } catch (e: Exception) {
                    logger.f("Failed getting credentials from file: $e")
                }
            }
        } catch (e: Exception) {
            println("\n\n\n\n")
            logger.e("Couldn't find credentials.txt file\n\n\n\n")
            throw Exception("No Credentials File")
        }
    }

    private val logDao = ServerLogDao.getInstance(config)

    private val logRepo = RepoFactory.getRepo(logDao)

    val viewModal: LogViewModal
        get() = LogViewModal(logRepo)
}