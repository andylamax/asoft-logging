package com.asofttz.logging

import com.asofttz.logging.data.dao.ClientLogDao
import com.asofttz.logging.data.viewmodal.LogViewModal
import com.asofttz.persist.DataSourceConfig
import com.asofttz.persist.PaginatedRepoFactory
import com.asofttz.persist.RepoFactory
import kotlin.js.JsName
import kotlin.test.Test

class InjectionTest {
    object injection {
        val dao = ClientLogDao.getInstance(DataSourceConfig())

        val repo = PaginatedRepoFactory.getRepo(dao)

        val logger = Logger("Some test", repo)
    }

    @Test
    @JsName("test1")
    fun `should attempt log and fail`() {
        injection.logger.w("This warns hard")
    }
}