package dev.drzepka.smarthome.terminal.client.manager

import dev.drzepka.smarthome.terminal.common.api.screen.Screen
import dev.drzepka.smarthome.terminal.client.IdGenerator
import dev.drzepka.smarthome.terminal.client.utils.ScreenCategory

abstract class ScreenManager(name: String, description: String?) {
    val category by ScreenCategory(name, description)

    /**
     * Called when this screen is requested
     */
    open fun getScreen(): Screen {
        return Screen(IdGenerator.nextId(), null, null)
    }
}
