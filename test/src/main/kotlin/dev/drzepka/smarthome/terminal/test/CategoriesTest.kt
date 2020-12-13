package dev.drzepka.smarthome.terminal.test

import dev.drzepka.smarthome.terminal.client.manager.ClientManager
import dev.drzepka.smarthome.terminal.client.TerminalClient
import dev.drzepka.smarthome.terminal.client.manager.ScreenManager

private class CategoriesTestClientManager : ClientManager() {
    override val clientName: String = "categories test"

    init {
        registerScreenManager(FirstCategoryScreen())
        registerScreenManager(SecondCategoryScreen())
    }

    override fun onInitialize() = Unit
}

private class FirstCategoryScreen : ScreenManager("First screen", null)
private class SecondCategoryScreen : ScreenManager("Second screen", "secound screen description")

fun main() {
    val client = TerminalClient("http://localhost:8081/api", CategoriesTestClientManager())
    client.register()

    // Keep the JVM alive
    Thread.sleep(999999999)
}