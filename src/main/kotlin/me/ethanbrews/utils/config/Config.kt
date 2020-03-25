package me.ethanbrews.utils.config

import com.natpryce.konfig.*
import com.natpryce.konfig.ConfigurationProperties.Companion.systemProperties
import me.ethanbrews.utils.modid
import com.google.common.io.Resources;
import java.io.File

val configFile = File("config/$modid.config")

val Config = systemProperties() overriding
        EnvironmentVariables() overriding
        ConfigurationProperties.fromOptionalFile(configFile) overriding
        ConfigurationProperties.fromResource("defaults.properties")

object ConfigInitialiser {
    fun init() {
        if (!configFile.exists()) {
            val text = Resources.getResource("defaults.properties").readText()
            configFile.writeText(text)
        }
    }
}

@Suppress("unused", "ClassName")
object commands : PropertyGroup() {
    val debug by booleanType
    val live_config_edit by booleanType
    val storage_network_debug by booleanType
}

@Suppress("unused", "ClassName")
object debug : PropertyGroup() {
    val show_debug_tooltips by booleanType
}