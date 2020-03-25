package me.ethanbrews.utils.command

interface ICommandLoader {
    fun canLoad(): Boolean
    fun load()

    fun loadIfEnabled() {
        if (canLoad())
            load()
    }
}