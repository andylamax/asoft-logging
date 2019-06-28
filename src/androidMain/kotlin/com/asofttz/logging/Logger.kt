package com.asofttz.logging

import com.asofttz.logging.tools.Cause
import com.asofttz.persist.PaginatedRepo
import kotlinx.coroutines.*
import android.util.Log as ALog

actual class Logger actual constructor(actual val source: String, actual val repo: PaginatedRepo<Log>?) {

    actual fun d(msg: String) {
        val log = Log(Log.Level.DEBUG, msg, source)
        ALog.d(source, log.msg)
        log.send()
    }

    actual fun e(msg: String, c: Cause?) {
        val log = Log(Log.Level.ERROR, msg, source)
        ALog.e(source, log.msg, c)
        log.send()
    }

    actual fun e(c: Cause?) {
        val log = Log(Log.Level.ERROR, c?.message ?: "No Message", source)
        ALog.e(source, log.msg, c)
        log.send()
    }

    actual fun f(msg: String, c: Cause?) {
        val log = Log(Log.Level.FAILURE, msg, source)
        ALog.wtf(source, msg, c)
        log.send()
    }

    actual fun f(c: Cause?) {
        val log = Log(Log.Level.FAILURE, c?.message ?: "No Message", source)
        ALog.wtf(source, log.msg, c)
        log.send()
    }

    actual fun w(msg: String) {
        val log = Log(Log.Level.WARNING, msg, source)
        ALog.w(source, log.msg)
        log.send()
    }

    actual fun i(msg: String) {
        val log = Log(Log.Level.INFO, msg, source)
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