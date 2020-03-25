package me.ethanbrews.utils.block

import me.ethanbrews.utils.block.entity.FairyLightBlockEntity
import me.ethanbrews.utils.block.entity.MutableBlockEntityType
import me.ethanbrews.utils.block.entity.StorageNetworkBlockEntity
import me.ethanbrews.utils.block.entity.StorageNetworkControllerBlockEntity
import me.ethanbrews.utils.modid
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry

object BlockEntities {
    val FAIRY_LIGHT_BLOCK_ENTITY: BlockEntityType<FairyLightBlockEntity> = register("fairy_light_block_entity", ::FairyLightBlockEntity)
    val STORAGE_CONTROLLER_ENTITY: BlockEntityType<StorageNetworkControllerBlockEntity> = register("storage_network_block_entity", ::StorageNetworkControllerBlockEntity)

    private fun <T : BlockEntity> register(name: String, supplier: () -> T): BlockEntityType<T> =
        Registry.register(Registry.BLOCK_ENTITY, modid+name, MutableBlockEntityType(supplier))

    fun init() {}
}