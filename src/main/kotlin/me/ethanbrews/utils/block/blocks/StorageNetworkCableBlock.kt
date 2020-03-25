package me.ethanbrews.utils.block.blocks

import me.ethanbrews.utils.block.entity.StorageNetworkControllerBlockEntity
import me.ethanbrews.utils.logger
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class StorageNetworkCableBlock : Block(Settings.of(Material.METAL)), StorageNetworkBlock {
    override val name = "StorageNetworkCableBlock"

    override fun onBreak(world: World?, pos: BlockPos?, state: BlockState?, player: PlayerEntity?) {
        super<StorageNetworkBlock>.onBreak(world, pos, state, player)
        super<Block>.onBreak(world, pos, state, player)
    }

    //override fun
}