package tz.co.asoft.logging

import tz.co.asoft.logging.tools.Cause
import tz.co.asoft.persist.repo.PaginatedRepo

expect open class Logger(source: String = "anonymous", repo: PaginatedRepo<Log>? = null) {
    protected val source: String
    protected val repo: PaginatedRepo<Log>?
    fun d(msg: String)
    fun e(msg: String, c: Cause? = null)
    fun e(c: Cause? = null)
    fun f(msg: String, c: Cause? = null)
    fun f(c: Cause? = null)
    fun w(msg: String)
    fun i(msg: String)
    fun obj(vararg o: Any?)
    fun obj(o: Any?)
}