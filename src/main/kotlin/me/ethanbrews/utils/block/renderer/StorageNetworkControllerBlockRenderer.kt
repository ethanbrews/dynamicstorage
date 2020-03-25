package me.ethanbrews.utils.block.renderer

import me.ethanbrews.utils.block.entity.StorageNetworkControllerBlockEntity
import me.ethanbrews.utils.lib.colour
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector3f
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import kotlin.math.sin


@Environment(EnvType.CLIENT)
class StorageNetworkControllerBlockRenderer(dispatcher: BlockEntityRenderDispatcher) : BlockEntityRenderer<StorageNetworkControllerBlockEntity>(dispatcher) {
    override fun render(
        be: StorageNetworkControllerBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        val colour = if (be.isNetworkValid) colour(0x00AA00) else colour(0xFF5555)
        matrices.push();

        val offset = sin((be.world!!.time + tickDelta) / 8.0) / 4.0
        matrices.translate(0.5, 1.25 + offset, 0.5)
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((be.world!!.time + tickDelta) * 4))
        val lightAbove = WorldRenderer.getLightmapCoordinates(be.world, be.pos.up())

        if (be.isNetworkValid)
            MinecraftClient.getInstance().itemRenderer.renderItem(stackRedstone, ModelTransformation.Type.GROUND, lightAbove, overlay, matrices, vertexConsumers);
        else
            MinecraftClient.getInstance().itemRenderer.renderItem(stackGold, ModelTransformation.Type.GROUND, lightAbove, overlay, matrices, vertexConsumers);

        /*
        MinecraftClient.getInstance().blockRenderManager.renderBlock(
            MinecraftClient.getInstance().world?.getBlockState(be.pos)!!,
            be.pos,
            MinecraftClient.getInstance().world!!,
            matrices,
            vertexConsumers.getBuffer(RenderLayer.getSolid()).color(255, 0, 0, 255),
            true,
            Random()
        )
        */
        matrices.pop()

    }

    companion object {
        val stackRedstone = ItemStack(Items.REDSTONE)
        val stackGold = ItemStack(Items.GOLD_INGOT)
    }
}