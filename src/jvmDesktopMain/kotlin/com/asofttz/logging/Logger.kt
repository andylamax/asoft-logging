package com.asofttz.logging

import com.asofttz.logging.tools.Cause
import com.asofttz.persist.PaginatedRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

actual class Logger actual constructor(actual val source: String, actual val repo: PaginatedRepo<Log>?) {
    actual fun d(msg: String) {
        val log = Log(Log.Level.DEBUG, msg, source)
        Color.Blue.println(log)
        log.send()
    }

    actual fun e(msg: String, c: Cause?) {
        val log = Log(Log.Level.ERROR, msg, source)
        Color.Red.println(log)
        c?.printStackTrace()
        log.send()
    }

    actual fun e(c: Cause?) {
        val log = Log(Log.Level.ERROR, c?.message ?: "No Message", source)
        Color.Red.println(log)
        c?.printStackTrace()
        log.send()
    }

    actual fun f(msg: String, c: Cause?) {
        val log = Log(Log.Level.FAILURE, msg, source)
        Color.Red.println(log)
        c?.printStackTrace()
        log.send()
    }

    actual fun f(c: Cause?) {
        val log = Log(Log.Level.FAILURE, c?.message ?: "No Message", source)
        Color.Red.println(log)
        log.send()
    }

    actual fun w(msg: String) {
        val log = Log(Log.Level.WARNING, msg, source)
        Color.Yellow.println(log)
        log.send()
    }

    actual fun i(msg: String) {
        val log = Log(Log.Level.INFO, msg, source)
        Color.Normal.println(log)
        log.send()
    }

    actual fun obj(vararg o: Any?) = o.forEach {
        val log = Log(Log.Level.INFO, it.toString(), source)
        Color.Normal.println(log)
    }

    actual fun obj(o: Any?) {
        val log = Log(Log.Level.INFO, o.toString(), source)
        Color.Normal.println(log)
    }

    enum class Color(private val escape: String) {
        Red("\u001B[31m"),
        Maroon("\u001B[35m"),
        Yellow("\u001B[33m"),
        Blue("\u001B[36m"),
        Normal("\u001B[0m");

        fun println(log: Log) {
            println("$escape$log\u001B[0m")
        }
    }

    private fun Log.send() = GlobalScope.launch {
        repo?.create(this@send)
    }
}
