package dev.drzepka.smarthome.terminal.test

import dev.drzepka.smarthome.terminal.client.TerminalClient
import dev.drzepka.smarthome.terminal.client.manager.ClientManager
import dev.drzepka.smarthome.terminal.client.manager.ScreenManager
import dev.drzepka.smarthome.terminal.common.api.element.ScreenModel

private class CategoriesTestClientManager : ClientManager() {
    override val clientName: String = "categories test"

    init {
        registerScreenManager(FirstCategoryScreen())
        registerScreenManager(SecondCategoryScreen())
    }

    override fun onInitialize() = Unit
}

private class FirstCategoryScreen : ScreenManager("First screen", null) {
    override fun getScreen(): ScreenModel = throw NotImplementedError()
}

private class SecondCategoryScreen : ScreenManager("Second screen", "secound screen description") {
    override fun getScreen(): ScreenModel = throw NotImplementedError()
}

fun main() {
    val client = TerminalClient(TestCommons.getIdentity(), CategoriesTestClientManager())
    client.register()

    // Keep the JVM alive
    Thread.sleep(999999999)
}