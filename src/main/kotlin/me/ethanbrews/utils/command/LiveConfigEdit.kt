package me.ethanbrews.utils.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.natpryce.konfig.Key
import me.ethanbrews.utils.config.Config
import me.ethanbrews.utils.config.commands
import me.ethanbrews.utils.modid
import net.fabricmc.fabric.api.registry.CommandRegistry
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class LiveConfigEdit : ICommandLoader {
    override fun canLoad(): Boolean = Config[commands.live_config_edit]

    override fun load() {
        CommandRegistry.INSTANCE.register(false) { dispatcher ->
            dispatcher.register(
                literal(modid).then(
                    literal("config").then(
                        argument("target", StringArgumentType.word()).then(
                            argument("name", StringArgumentType.string()).executes {context ->
                                var target = StringArgumentType.getString(context, "target")
                                var name = StringArgumentType.getString(context, "name")

                                if (target == "server") {
                                    if (!context.source.hasPermissionLevel(2)) {
                                        context.source.sendError(LiteralText("Only server operators can modify server configuration"))
                                    }

                                }
                                context.source.sendError(LiteralText("Command not implemented. Disable in config"))
                                return@executes 0
                            }
                        )

                    )
                )
            )
        }
    }
}