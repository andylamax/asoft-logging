package tz.co.asoft.logging

import tz.co.asoft.logging.tools.Cause
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tz.co.asoft.persist.repo.PaginatedRepo
import android.util.Log as ALog

actual open class Logger actual constructor(protected actual val source: String, protected actual val repo: PaginatedRepo<Log>?) {

    actual fun d(msg: String) {
        val log = Log(Log.Level.DEBUG.name, msg, source)
        ALog.d(source, log.msg)
        log.send()
    }

    actual fun e(msg: String, c: Cause?) {
        val log = Log(Log.Level.ERROR.name, msg, source)
        ALog.e(source, log.msg, c)
        log.send()
    }

    actual fun e(c: Cause?) {
        val log = Log(Log.Level.ERROR.name, c?.message ?: "No Message", source)
        ALog.e(source, log.msg, c)
        log.send()
    }

    actual fun f(msg: String, c: Cause?) {
        val log = Log(Log.Level.FAILURE.name, msg, source)
        ALog.wtf(source, msg, c)
        log.send()
    }

    actual fun f(c: Cause?) {
        val log = Log(Log.Level.FAILURE.name, c?.message ?: "No Message", source)
        ALog.wtf(source, log.msg, c)
        log.send()
    }

    actual fun w(msg: String) {
        val log = Log(Log.Level.WARNING.name, msg, source)
        ALog.w(source, log.msg)
        log.send()
    }

    actual fun i(msg: String) {
        val log = Log(Log.Level.INFO.name, msg, source)
        ALog.i(source, log.msg)
        log.send()
    }

    actual fun obj(vararg o: Any?) {
        o.forEach {
            ALog.i(source, it.toString())
        }
    }

    actual fun obj(o: Any?) {
        ALog.i(source, o.toString())
    }

    private fun Log.send() = GlobalScope.launch {
        repo?.create(this@send)
    }
}