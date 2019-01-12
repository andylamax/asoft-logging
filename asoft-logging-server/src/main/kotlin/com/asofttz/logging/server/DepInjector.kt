package com.asofttz.logging.server

import com.asofttz.logging.data.dao.ServerLogDao
import com.asofttz.logging.data.db.LogDataSourceConfig
import com.asofttz.logging.data.repo.LogRepo
import com.asofttz.logging.data.viewmodal.LogViewModal
import kotlinx.coroutines.runBlocking
import java.io.File

object injection {

    init {
        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource("credentials.txt").file)
        runBlocking {
            val line = file.readLines()
            config.apply {
                username = line[0].split(" ")[0]
                password = line[0].split(" ")[1]
            }
        }
    }

    private val credentials = File("")
    private val config = LogDataSourceConfig().apply {
        url = "bolt://localhost:8082"
        username = "admin"
        password = "admin"
    }

    private val logDao = ServerLogDao.getInstance(config)

    private val logRepo = LogRepo.getInstance(logDao)

    val viewModal: LogViewModal
        get() = LogViewModal(logRepo)
}