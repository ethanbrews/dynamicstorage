package me.ethanbrews.utils.logging

import me.ethanbrews.utils.modname
import org.apache.logging.log4j.Logger

class Logger(private val logger: Logger) {

    fun getLogger(): Logger {
        return logger
    }

    private fun fmt(o: Any): String {
        return "[$modname] $o"
    }

    fun debug(o: Any) {
        logger.debug(fmt(o))
    }

    fun info(o: Any) {
        logger.info(fmt(o))
    }

    fun warn(o: Any) {
        logger.warn(fmt(o))
    }

    fun error(o: Any) {
        logger.error(fmt(o))
    }

    fun fatal(o: Any) {
        logger.error(fmt(o))
    }


}