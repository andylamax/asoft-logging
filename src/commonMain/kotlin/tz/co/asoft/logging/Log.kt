package tz.co.asoft.logging

import com.soywiz.klock.DateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
open class Log(
        var level: String = Level.DEBUG.name,
        var msg: String = "",
        var source: String = "anonymous"
) {

    var id: Long? = null

    var time = DateTime.nowUnixLong()

    @Transient
    private val logger: Logger
        get() = Logger(source)

    enum class Level {
        ERROR, WARNING, DEBUG, FAILURE, INFO
    }

    override fun toString() = DateTime.fromUnix(time).format("yyyy-MM-dd HH:mm:ss.XXX") + " [$level] $source - $msg"

    fun log() = when (level) {
        Level.ERROR.name -> logger.e(msg)
        Level.WARNING.name -> logger.w(msg)
        Level.DEBUG.name -> logger.d(msg)
        Level.FAILURE.name -> logger.f(msg)
        else -> logger.i(msg)
    }
}

