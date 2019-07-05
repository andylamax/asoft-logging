package tz.co.asoft.logging

import tz.co.asoft.logging.tools.Cause
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tz.co.asoft.persist.repo.PaginatedRepo

actual open class Logger actual constructor(protected actual val source: String, protected actual val repo: PaginatedRepo<Log>?) {
    actual fun d(msg: String) {
        val log = Log(Log.Level.DEBUG.name, msg, source)
        console.log("$log")
        log.send()
    }

    actual fun e(msg: String, c: Cause?) {
        val log = Log(Log.Level.ERROR.name, msg, source)
        console.error("$log")
        log.send()
    }

    actual fun e(c: Cause?) {
        val log = Log(Log.Level.ERROR.name, c?.message ?: "No Message", source)
        console.error("$log")
        log.send()
    }

    actual fun f(msg: String, c: Cause?) {
        val log = Log(Log.Level.FAILURE.name, msg, source)
        console.error("$log")
        log.send()
    }

    actual fun f(c: Cause?) {
        val log = Log(Log.Level.FAILURE.name, c?.message ?: "No Message", source)
        console.error("$log")
        log.send()
    }

    actual fun w(msg: String) {
        val log = Log(Log.Level.WARNING.name, msg, source)
        console.warn("$log")
        log.send()
    }

    actual fun i(msg: String) {
        val log = Log(Log.Level.INFO.name, msg, source)
        console.info("$log")
        log.send()
    }

    actual fun obj(vararg o: Any?) {
        o.forEach {
            console.log(it)
        }
    }

    actual fun obj(o: Any?) = console.log(o)

    private fun Log.send() = GlobalScope.launch {
        repo?.create(this@send)
    }
}