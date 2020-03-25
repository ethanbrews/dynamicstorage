package me.ethanbrews.utils.block.entity

import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.world.BlockView

interface BlockEntityTypeProvider : BlockEntityProvider {
    val blockEntityType: BlockEntityType<*>

    override fun createBlockEntity(view: BlockView?) = blockEntityType.instantiate()
}