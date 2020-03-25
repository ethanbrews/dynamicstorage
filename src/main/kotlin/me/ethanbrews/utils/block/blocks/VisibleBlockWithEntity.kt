package me.ethanbrews.utils.block.blocks

import me.ethanbrews.utils.block.entity.BlockEntityTypeProvider
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity

abstract class VisibleBlockWithEntity(settings: Block.Settings) : BlockWithEntity(settings), BlockEntityTypeProvider {
    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL
}