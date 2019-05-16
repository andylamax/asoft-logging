package com.asofttz.logging.data.repo

import com.asofttz.logging.Log
import com.asofttz.persist.Dao
import com.asofttz.persist.Repo

class LogRepo(private val dao: Dao<Log>) : Repo<Log>(dao) {

}