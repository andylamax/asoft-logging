package com.asofttz.logging

import com.asofttz.date.Date
import com.asofttz.date.DateFactory
import com.asofttz.date.string

open class Log(var level: Level = Level.DEBUG, val msg: String = "", var source: String = "source") {
    var id: Int = 0
    var time: Date = DateFactory.today()

    enum class Level {
        ERROR, WARNING, DEBUG, FAILURE, INFO
    }

    override fun toString(): String {
        return "${time.string()} $source [$level] - $msg"
    }
}