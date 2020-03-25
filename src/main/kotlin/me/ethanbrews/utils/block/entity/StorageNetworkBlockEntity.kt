package me.ethanbrews.utils.block.entity

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.CompoundTag

abstract class StorageNetworkBlockEntity(type: BlockEntityType<StorageNetworkControllerBlockEntity>) : BlockEntity(type),
    BlockEntityClientSerializable {

    abstract val name: String

    fun getController(): StorageNetworkBlockEntity? {
        return null;
    }

    override fun toClientTag(tag: CompoundTag?): CompoundTag {
        return this.toTag(tag)
    }

    override fun fromClientTag(tag: CompoundTag?) {
        this.fromTag(tag)
    }

}