package me.ethanbrews.utils.block.renderer

import me.ethanbrews.utils.block.entity.FairyLightBlockEntity
import me.ethanbrews.utils.lib.colour
import me.ethanbrews.utils.logger
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
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import kotlin.math.sin


@Environment(EnvType.CLIENT)
class FairyLightRenderer(dispatcher: BlockEntityRenderDispatcher) : BlockEntityRenderer<FairyLightBlockEntity>(dispatcher) {
    override fun render(be: FairyLightBlockEntity, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {
        val hitResult = this.dispatcher.crosshairTarget
        val lookingAtBlock = hitResult != null &&
                hitResult.type == HitResult.Type.BLOCK &&
                be.pos == (hitResult as BlockHitResult).blockPos

        matrices.push()

        val offset = sin((be.world!!.time + tickDelta) / 8.0) / 4.0
        matrices.translate(0.5, 1.25 + offset, 0.5)
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((be.world!!.time + tickDelta) * 4))

        val lightAbove = WorldRenderer.getLightmapCoordinates(be.world, be.pos.up())
        MinecraftClient.getInstance().itemRenderer.renderItem(
            stack,
            ModelTransformation.Type.GROUND,
            lightAbove,
            OverlayTexture.DEFAULT_UV,
            matrices,
            vertexConsumers
        )



        //MinecraftClient.getInstance().worldRenderer.render()

        // Mandatory call after GL calls
        matrices.pop();

        /*
        if (be.connectedTo == null)
            logger.info("connectedTo == null")
        else
            logger.info("connectedTo has a value")
        */
        renderLabel(be, "Connected To: "+be.connectedTo?.pos?.toShortString(), 0.0, .4,0.0, 16, matrices, vertexConsumers, lightAbove)
        renderLabel(be, "Connected From: "+be.connectedFrom?.pos?.toShortString(), 0.0, .2,0.0, 16, matrices, vertexConsumers, lightAbove)
    }


    private fun renderLabel(be: FairyLightBlockEntity, name: String, x: Double, y: Double, z: Double, maxDistance: Int, matrix: MatrixStack, vcp: VertexConsumerProvider, light: Int) {
        val camera = this.dispatcher.camera
        val dist = be.getSquaredDistance(camera.pos.x, camera.pos.y, camera.pos.z)
        if (dist < maxDistance * maxDistance) {
            matrix.push()
            matrix.translate(x + 0.5, y + 1.5, z + 0.5)
            matrix.multiply(camera.rotation)
            matrix.scale(-0.025f, -0.025f, +0.025f)

            val matrixModel = matrix.peek().model
            val opacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f)
            val backgroundColor = colour(0x000000, opacity)
            val textRenderer = dispatcher.textRenderer
            val textX = -textRenderer.getStringWidth(name) / 2f
            textRenderer.draw(name, textX, 0f, colour(0xFFFFFF), false, matrixModel, vcp, false, backgroundColor, light)

            matrix.pop()
        }
    }

    companion object {
        private val stack = ItemStack(Items.JUKEBOX, 1)
    }
}