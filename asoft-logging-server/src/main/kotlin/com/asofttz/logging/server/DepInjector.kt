package com.asofttz.logging.server

import com.asofttz.logging.data.dao.ServerLogDao
import com.asofttz.logging.data.db.LogDataSourceConfig
import com.asofttz.logging.data.repo.LogRepo
import com.asofttz.logging.data.viewmodal.LogViewModal

object injection {

    private val config = LogDataSourceConfig().apply {
        url = "bolt://localhost:8082"
        username = "andylamax"
        password = "andymamson"
    }

    private val logDao = ServerLogDao.getInstance(config)

    private val logRepo = LogRepo.getInstance(logDao)

    val viewModal = LogViewModal(logRepo)
}