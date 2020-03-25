package me.ethanbrews.utils.command

object CommandInitialiser {
    fun init() {
        Debug.loadIfEnabled()
        StorageNetworkDebug.loadIfEnabled()
    }
}