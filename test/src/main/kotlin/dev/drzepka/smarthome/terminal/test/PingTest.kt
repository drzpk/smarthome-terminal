package dev.drzepka.smarthome.terminal.test

import dev.drzepka.smarthome.terminal.client.TerminalClient
import dev.drzepka.smarthome.terminal.client.manager.ClientManager
import dev.drzepka.smarthome.terminal.client.manager.ScreenManager
import dev.drzepka.smarthome.terminal.common.api.element.ScreenModel

class PingTestClientManager : ClientManager() {
    override val clientName: String = "client name"

    init {
        registerScreenManager(PingTestScreenManager())
    }

    override fun onInitialize() {

    }
}

class PingTestScreenManager : ScreenManager("Test screen", null) {
    override fun getScreen(): ScreenModel = throw NotImplementedError()
}

fun main(args: Array<String>) {
    val client = TerminalClient(TestCommons.getIdentity(), PingTestClientManager())
    client.register()

    // Keep the JVM alive
    Thread.sleep(999999999)
}