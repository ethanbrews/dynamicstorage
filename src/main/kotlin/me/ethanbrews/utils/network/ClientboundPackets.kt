package me.ethanbrews.utils.network

import me.ethanbrews.utils.modid
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.fabricmc.fabric.api.network.PacketContext
import net.minecraft.util.Identifier
import net.minecraft.util.PacketByteBuf
import java.util.function.BiConsumer


object ClientboundPackets {

    val FAIRY_LIGHT_UPDATE = Identifier(modid, "fairylight")

    private fun registerPacketHandler(
        identifier: Identifier,
        consumer: BiConsumer<PacketByteBuf, PacketContext>
    ) {
        ClientSidePacketRegistry.INSTANCE.register(
            identifier
        ) { packetContext: PacketContext?, packetByteBuf: PacketByteBuf? ->
            consumer.accept(
                packetByteBuf!!,
                packetContext!!
            )
        }
    }
    fun init() {
        /*
        registerPacketHandler(AESU, (extendedPacketBuffer, context) -> {
            BlockPos pos = extendedPacketBuffer.readBlockPos();
            int buttonID = extendedPacketBuffer.readInt();
            boolean shift = extendedPacketBuffer.readBoolean();
            boolean ctrl = extendedPacketBuffer.readBoolean();

            context.getTaskQueue().execute(() -> {
            BlockEntity blockEntity = context.getPlayer().world.getBlockEntity(pos);
            if (blockEntity instanceof AdjustableSUBlockEntity) {
                ((AdjustableSUBlockEntity) blockEntity).handleGuiInputFromClient(buttonID, shift, ctrl);
            }
        });
        });
         */
    }


}