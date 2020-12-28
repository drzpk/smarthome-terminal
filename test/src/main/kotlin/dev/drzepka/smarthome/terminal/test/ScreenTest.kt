package dev.drzepka.smarthome.terminal.test

import dev.drzepka.smarthome.terminal.client.TerminalClient
import dev.drzepka.smarthome.terminal.client.data.UpdateResult
import dev.drzepka.smarthome.terminal.client.data.UpdateStatus
import dev.drzepka.smarthome.terminal.client.manager.ClientManager
import dev.drzepka.smarthome.terminal.client.manager.ScreenManager
import dev.drzepka.smarthome.terminal.common.api.element.ScreenModel
import dev.drzepka.smarthome.terminal.common.api.element.property.PropertyModel
import dev.drzepka.smarthome.terminal.common.api.element.property.simple.IntPropertyModel
import dev.drzepka.smarthome.terminal.common.api.element.property.simple.StringPropertyModel
import dev.drzepka.smarthome.terminal.common.builder.intProperty
import dev.drzepka.smarthome.terminal.common.builder.screen
import dev.drzepka.smarthome.terminal.common.builder.stringProperty

private class ScreenTestClientManager : ClientManager() {
    override val clientName: String = "screen test"

    init {
        registerScreenManager(ScreenTestManager())
    }

    override fun onInitialize() = Unit
}

private class ScreenTestManager : ScreenManager("First screen", null) {
    private lateinit var firstName: StringPropertyModel
    private lateinit var lastName: StringPropertyModel
    private lateinit var age: IntPropertyModel

    override fun getScreen(): ScreenModel {
        return screen(this) {
            firstName = stringProperty {

            }
            lastName = stringProperty {

            }
            age = intProperty {

            }
        }
    }

    override fun getTrackedProperties(): Collection<PropertyModel<*>> = listOf(firstName, lastName, age)

    override fun updateScreen(): UpdateResult {
        println("First name: ${firstName.value}")
        println("Last name: ${lastName.value}")
        println("age: ${age.value}")
        return UpdateResult(UpdateStatus.UPDATED, "update message")
    }
}

fun main() {
    val client = TerminalClient(TestCommons.getIdentity(), ScreenTestClientManager())
    client.register()

    // Keep the JVM alive
    Thread.sleep(999999999)
}