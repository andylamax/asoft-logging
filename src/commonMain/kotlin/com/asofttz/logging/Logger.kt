package com.asofttz.logging

import com.asofttz.persist.PaginatedRepo

expect class Logger(source: String = "anonymous", repo: PaginatedRepo<Log>? = null) {
    val source: String
    val repo: PaginatedRepo<Log>?
    fun d(msg: String)
    fun e(msg: String)
    fun f(msg: String)
    fun w(msg: String)
    fun i(msg: String)
    fun obj(vararg o: Any?)
    fun obj(o: Any?)
}