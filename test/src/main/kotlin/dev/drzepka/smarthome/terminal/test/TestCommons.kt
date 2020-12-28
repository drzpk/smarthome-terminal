package dev.drzepka.smarthome.terminal.test

import dev.drzepka.smarthome.terminal.client.data.TerminalClientIdentity
import java.net.URL

object TestCommons {
    const val TEST_CLIENT_NAME = "test"
    const val TEST_CLIENT_PASSWORD = "test-client-password-123"

    fun getIdentity(): TerminalClientIdentity =
        TerminalClientIdentity(URL("http://localhost:8081/api"), TEST_CLIENT_NAME, TEST_CLIENT_PASSWORD)
}