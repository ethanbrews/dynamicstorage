package me.ethanbrews.utils

import me.ethanbrews.utils.block.BlockInitialiser
import me.ethanbrews.utils.block.BlockEntities
import me.ethanbrews.utils.network.NetworkIdentifiers
import me.ethanbrews.utils.command.CommandInitialiser
import me.ethanbrews.utils.config.ConfigInitialiser
import me.ethanbrews.utils.item.ItemInitialiser
import net.fabricmc.api.ModInitializer
import me.ethanbrews.utils.logging.Logger
import org.apache.logging.log4j.LogManager

var modid = "dynamicstorage"
var modname = "Dynamic Storage"
var logger = Logger(LogManager.getLogger(modid))

class Main : ModInitializer {
    override fun onInitialize() {
        logger.info("Initialising Components")

        ItemInitialiser.init()
        BlockInitialiser.init()
        BlockEntities.init()
        CommandInitialiser.init()
        ConfigInitialiser.init()
        NetworkIdentifiers.init()
    }

    @Suppress("unused")
    fun initClient() {
        logger.info("We are running on a client")
        BlockInitialiser.initClient()
        ItemInitialiser.initClient()
        NetworkIdentifiers.initClient()
    }

    @Suppress("unused")
    fun initServer() {
        logger.info("We are running on a server")
    }

}
