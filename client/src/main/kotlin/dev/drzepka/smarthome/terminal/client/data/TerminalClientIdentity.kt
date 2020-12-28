package dev.drzepka.smarthome.terminal.client.data

import java.net.URL

data class TerminalClientIdentity(val apiUrl: URL, val name: String, val password: String)