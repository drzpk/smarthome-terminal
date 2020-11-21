package dev.drzepka.smarthome.terminal.server.application

import dev.drzepka.smarthome.terminal.server.application.configuration.koinModule
import org.junit.jupiter.api.Test
import org.koin.test.check.checkModules

class ModulesTest {

    @Test
    fun `should verify modules`() = checkModules {
        modules(koinModule)
    }
}