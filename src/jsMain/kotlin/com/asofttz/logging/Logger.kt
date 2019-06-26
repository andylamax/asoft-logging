package com.asofttz.logging

import com.asofttz.persist.PaginatedRepo
import kotlinx.coroutines.*

actual class Logger actual constructor(actual val source: String, actual val repo: PaginatedRepo<Log>?) {
    actual fun d(msg: String) {
        val log = Log(Log.Level.DEBUG, msg, source)
        console.log("$log")
        log.send()
    }

    actual fun e(msg: String) {
        val log = Log(Log.Level.ERROR, msg, source)
        console.error("$log")
        log.send()
    }

    actual fun f(msg: String) {
        val log = Log(Log.Level.FAILURE, msg, source)
        console.error("$log")
        log.send()
    }

    actual fun w(msg: String) {
        val log = Log(Log.Level.WARNING, msg, source)
        console.warn("$log")
        log.send()
    }

    actual fun i(msg: String) {
        val log = Log(Log.Level.INFO, msg, source)
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