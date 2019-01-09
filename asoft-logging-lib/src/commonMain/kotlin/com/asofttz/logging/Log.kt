package com.asofttz.logging

import com.asofttz.date.Date
import com.asofttz.date.DateFactory
import com.asofttz.date.DateSerializer
import com.asofttz.date.string
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
open class Log(
        var level: Level = Level.DEBUG,
        val msg: String = "",
        var source: String = "anonymous"
) {

    var id: Int = 0

    @Serializable(with = DateSerializer::class)
    var time: Date = DateFactory.today()

    @Transient
    private val logger: Logger
        get() = Logger(source)

    enum class Level {
        ERROR, WARNING, DEBUG, FAILURE, INFO
    }

    override fun toString(): String {
        return "${time.string()} $source [$level] - $msg"
    }

    fun log() = when (level) {
        Level.ERROR -> logger.e(msg)
        Level.WARNING -> logger.w(msg)
        Level.DEBUG -> logger.d(msg)
        Level.FAILURE -> logger.f(msg)
        Level.INFO -> logger.i(msg)
    }
}

