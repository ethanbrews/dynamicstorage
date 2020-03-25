package me.ethanbrews.utils.block

import me.ethanbrews.utils.block.blocks.FairyLightBlock
import me.ethanbrews.utils.block.blocks.StorageNetworkCableBlock
import me.ethanbrews.utils.block.blocks.StorageNetworkControllerBlock
import me.ethanbrews.utils.block.entity.StorageNetworkControllerBlockEntity
import me.ethanbrews.utils.block.renderer.FairyLightRenderer
import me.ethanbrews.utils.block.renderer.StorageNetworkControllerBlockRenderer
import me.ethanbrews.utils.modid
import net.minecraft.block.Block
import me.ethanbrews.utils.lib.RegistryHelper
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.color.block.BlockColorProvider


@Suppress("UNUSED", "MemberVisibilityCanBePrivate")
object BlockInitialiser : RegistryHelper(modid) {
    val FAIRY_LIGHT_BLOCK: Block = registerBlock("fairy_light_block", FairyLightBlock())
    val STORAGE_NETWORK_CONTROLLER_BLOCK: Block = registerBlock("storage_network_controller_block", StorageNetworkControllerBlock())
    val STORAGE_NETWORK_CABLE: Block = registerBlock("storage_network_cable", StorageNetworkCableBlock())
    //val CONTROLLER: Block = registerBlock("controller", )

    fun init() {
        // we can register block callback events here
        // using UseBlockCallback.EVENT.register( ... )
    }

    fun initClient() {
        BlockEntityRendererRegistry.INSTANCE.register(
            BlockEntities.FAIRY_LIGHT_BLOCK_ENTITY,
            ::FairyLightRenderer
        )


        BlockEntityRendererRegistry.INSTANCE.register(
            BlockEntities.STORAGE_CONTROLLER_ENTITY,
            ::StorageNetworkControllerBlockRenderer
        )


        /*
        ColorProviderRegistry.BLOCK.register(BlockColorProvider { state, view, pos, tintIndex ->
            // Use an explicit 'x == true' check in case x is null;
            if ((MinecraftClient.getInstance().world?.getBlockEntity(pos) as StorageNetworkControllerBlockEntity?)?.isNetworkValid == true)
                return@BlockColorProvider 0x00AA00
            else
                return@BlockColorProvider 0xFF5555
        }, STORAGE_NETWORK_CONTROLLER_BLOCK)
        */
    }
}